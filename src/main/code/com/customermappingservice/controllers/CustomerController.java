package com.customermappingservice.controllers;

import code.com.customermappingservice.models.SetCustomerResponse;
import com.customermappingservice.apiutils.ApiUtils;
import com.customermappingservice.constants.JsonDBConstants;
import com.customermappingservice.models.ApiRequest;
import com.customermappingservice.models.ApiResponse;
import com.customermappingservice.models.Customer;
import com.google.gson.Gson;
import customermappingservice.constants.HttpStatus;
import io.jsondb.InvalidJsonDbApiUsageException;
import spark.Request;
import spark.Response;
import spark.Route;
import io.jsondb.JsonDBTemplate;
import java.text.ParseException;
import java.util.UUID;

public class CustomerController {

    /**
     * Accept a customerId query string and return the customer externalId
     */
    public static Route handleGetCustomer = (Request request, Response response) -> {

        Gson gson = new Gson();
        if (request.queryParams("customerId") == null) {
            return buildResponse(response, HttpStatus.BAD_REQUEST, JsonDBConstants.MISSING_CUSTOMER_ID);
        } else {
            try {
                JsonDBTemplate jsonDBTemplate = new JsonDBTemplate(JsonDBConstants.JSONDB_DBFILESLOCATION, JsonDBConstants.JSONDB_BASEMODELSPACKAGE, null);

                if (!ApiUtils.customerCollectionExists(jsonDBTemplate)) {
                    return buildResponse(response, HttpStatus.SUCCESS, JsonDBConstants.CUSTOMER_NOT_FOUND);
                }

                Customer customer = ApiUtils.queryCustomerById(request.queryParams("customerId"), jsonDBTemplate);
                if (customer != null) {
                    return buildResponse(response, HttpStatus.SUCCESS, gson.toJson(customer));
                } else {
                    return buildResponse(response, HttpStatus.SUCCESS, JsonDBConstants.CUSTOMER_NOT_FOUND);
                }
            } catch(Exception e) {
                e.printStackTrace();
                return buildResponse(response, HttpStatus.INTERNAL_ERROR, JsonDBConstants.INTERNAL_SERVER_ERROR);
            }
        }
    };

    /**
     * Input params: customerId, createdAt (yyyy-mm-dd and cannot be in the future)
     * Store in db with a unique externalId
     */
    public static Route handlePostCustomer = (Request request, Response response) -> {
        try {
            Gson gson = new Gson();
            if (request.body().isEmpty()) {
                return buildResponse(response, HttpStatus.BAD_REQUEST, JsonDBConstants.MISSING_REQUEST_BODY);
            }

            ApiRequest apiRequest = gson.fromJson(request.body(), ApiRequest.class);

            if (apiRequest.firstName == null) {
                return buildResponse(response, HttpStatus.BAD_REQUEST, JsonDBConstants.MISSING_FIRST_NAME);
            } else if (apiRequest.lastName == null) {
                return buildResponse(response, HttpStatus.BAD_REQUEST, JsonDBConstants.MISSING_LAST_NAME);
            } else if (apiRequest.email == null) {
                return buildResponse(response, HttpStatus.BAD_REQUEST, JsonDBConstants.MISSING_EMAIL);
            } else {
                return handleDataStore(response, apiRequest);
            }

            //return gson.toJson(apiResponse);
        } catch (Exception e) {
            e.printStackTrace();
            Gson gson = new Gson();
            ApiResponse apiResponse = new ApiResponse();
            apiResponse.setStatusCode(500);
            apiResponse.setContent(JsonDBConstants.INTERNAL_SERVER_ERROR);
            return gson.toJson(apiResponse);
        }
    };

    public static Route notFound = (Request request, Response response) -> buildResponse(response, HttpStatus.PAGE_NOT_FOUND, JsonDBConstants.INVALID_ENDPOINT);

    //HELPERS
    private static String handleDataStore(Response response, ApiRequest requestBody) throws ParseException {
        Gson gson = new Gson();
        ApiResponse apiResponse = new ApiResponse();

        //TODO encrypt the DB!
        JsonDBTemplate jsonDBTemplate = new JsonDBTemplate(JsonDBConstants.JSONDB_DBFILESLOCATION, JsonDBConstants.JSONDB_BASEMODELSPACKAGE, null);

        if (!jsonDBTemplate.collectionExists(JsonDBConstants.JSONDB_COLLECTION_NAME)) {jsonDBTemplate.createCollection(Customer.class);}

        Customer customer = new Customer();

        String isCreatedAtValid = ApiUtils.isDateValidBirthDate(requestBody.dob);
        if (!isCreatedAtValid.equals("true")) {
            return buildResponse(response, HttpStatus.BAD_REQUEST, isCreatedAtValid);
        }

        String isFirstNameValid = ApiUtils.isNameValid(requestBody.firstName);
        if (!isFirstNameValid.equals("true")) {
            return buildResponse(response, HttpStatus.BAD_REQUEST, isFirstNameValid);
        }
        String isLastNameValid = ApiUtils.isNameValid(requestBody.lastName);
        if (!isLastNameValid.equals("true")) {
            return buildResponse(response, HttpStatus.BAD_REQUEST, isLastNameValid);
        }

        customer.setDob(requestBody.dob);
        customer.setCustomerId(UUID.randomUUID().toString());
        customer.setFirstName(requestBody.firstName);
        customer.setLastName(requestBody.lastName);
        customer.setEmail(requestBody.email);
        customer.setDob(requestBody.dob);

        try {
            jsonDBTemplate.insert(customer);
        } catch (InvalidJsonDbApiUsageException invalidJsonDbApiUsageException) {
            return buildResponse(response, HttpStatus.BAD_REQUEST, JsonDBConstants.CUSTOMER_ALREADY_EXISTS);
        }
        SetCustomerResponse setCustomerResponse = new SetCustomerResponse();
        setCustomerResponse.setCustomerId(customer.getCustomerId());
        apiResponse.setContent(gson.toJson(setCustomerResponse));
        return buildResponse(response, HttpStatus.SUCCESS_CREATED, gson.toJson(setCustomerResponse));
    }

    private static String buildResponse(Response response, int status, String message) {
        Gson gson = new Gson();
        response.status(status);
        response.type("application/json");
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setStatusCode(status);
        apiResponse.setContent(message);
        return gson.toJson(apiResponse);
    }

    public static void dropCollection(JsonDBTemplate jsonDBTemplate) {
        jsonDBTemplate.dropCollection(Customer.class);
    }

}
