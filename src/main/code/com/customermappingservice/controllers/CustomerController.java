package com.customermappingservice.controllers;

import code.com.customermappingservice.models.SetCustomerResponse;
import com.customermappingservice.apiutils.ApiUtils;
import com.customermappingservice.constants.JsonDBConstants;
import com.customermappingservice.models.ApiRequest;
import com.customermappingservice.models.ApiResponse;
import com.customermappingservice.models.Customer;
import com.google.gson.Gson;
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

        ApiResponse apiResponse =  new ApiResponse();
        Gson gson = new Gson();

        if (request.queryParams("customerId") == null) {
            apiResponse.setStatusCode(400);
            apiResponse.setContent(JsonDBConstants.MISSING_CUSTOMER_ID);
        } else {
            try {

                //TODO encrypt the DB!
                JsonDBTemplate jsonDBTemplate = new JsonDBTemplate(JsonDBConstants.JSONDB_DBFILESLOCATION, JsonDBConstants.JSONDB_BASEMODELSPACKAGE, null);
                if (!ApiUtils.customerCollectionExists(jsonDBTemplate)) {
                    apiResponse.setStatusCode(200);
                    apiResponse.setContent(JsonDBConstants.CUSTOMER_NOT_FOUND);
                    return gson.toJson(apiResponse);
                }

                Customer customer = ApiUtils.queryCustomerById(request.queryParams("customerId"), jsonDBTemplate);
                if (customer != null) {
                    apiResponse.setStatusCode(200);
                    apiResponse.setContent(gson.toJson(customer));
                } else {
                    apiResponse.setContent(JsonDBConstants.CUSTOMER_NOT_FOUND);
                }
            } catch(Exception e) {
                e.printStackTrace();
                apiResponse.setStatusCode(500);
                apiResponse.setContent(JsonDBConstants.INTERNAL_SERVER_ERROR);
                return gson.toJson(apiResponse);
            }
        }
        return gson.toJson(apiResponse);
    };

    /**
     * Input params: customerId, createdAt (yyyy-mm-dd and cannot be in the future)
     * Store in db with a unique externalId
     */
    public static Route handlePostCustomer = (Request request, Response response) -> {
        try {
            Gson gson = new Gson();
            ApiResponse apiResponse = new ApiResponse();
            if (request.body().isEmpty()) {
                apiResponse.setStatusCode(400);
                apiResponse.setContent(JsonDBConstants.MISSING_REQUEST_BODY);
                return gson.toJson(apiResponse);
            }

            ApiRequest apiRequest = gson.fromJson(request.body(), ApiRequest.class);

            if (apiRequest.firstName == null) {
                apiResponse.setStatusCode(400);
                apiResponse.setContent(JsonDBConstants.MISSING_FIRST_NAME);
            } else if (apiRequest.lastName == null) {
                apiResponse.setStatusCode(400);
                apiResponse.setContent(JsonDBConstants.MISSING_LAST_NAME);
            } else if (apiRequest.email == null) {
                apiResponse.setStatusCode(400);
                apiResponse.setContent(JsonDBConstants.MISSING_EMAIL);
            } else {
                apiResponse = handleDataStore(apiRequest);
            }

            return gson.toJson(apiResponse);
        } catch (Exception e) {
            e.printStackTrace();
            Gson gson = new Gson();
            ApiResponse apiResponse = new ApiResponse();
            apiResponse.setStatusCode(500);
            apiResponse.setContent(JsonDBConstants.INTERNAL_SERVER_ERROR);
            return gson.toJson(apiResponse);
        }
    };

    public static Route notFound = (Request request, Response response) -> {
        Gson gson = new Gson();
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setStatusCode(404);
        apiResponse.setContent(JsonDBConstants.INVALID_ENDPOINT);
        return gson.toJson(apiResponse);
    };


    //HELPERS
    private static ApiResponse handleDataStore(ApiRequest requestBody) throws ParseException {
        Gson gson = new Gson();
        ApiResponse apiResponse = new ApiResponse();
        JsonDBTemplate jsonDBTemplate = new JsonDBTemplate(JsonDBConstants.JSONDB_DBFILESLOCATION, JsonDBConstants.JSONDB_BASEMODELSPACKAGE, null);

        if (!jsonDBTemplate.collectionExists(JsonDBConstants.JSONDB_COLLECTION_NAME)) {jsonDBTemplate.createCollection(Customer.class);}

        Customer customer = new Customer();

        String isCreatedAtValid = ApiUtils.isDateValidBirthDate(requestBody.dob);
        if (!isCreatedAtValid.equals("true")) {
            apiResponse.setStatusCode(400);
            apiResponse.setContent(isCreatedAtValid);
            return apiResponse;
        }

        String isFirstNameValid = ApiUtils.isNameValid(requestBody.firstName);
        if (!isFirstNameValid.equals("true")) {
            apiResponse.setStatusCode(400);
            apiResponse.setContent(isFirstNameValid);
            return apiResponse;
        }
        String isLastNameValid = ApiUtils.isNameValid(requestBody.lastName);
        if (!isLastNameValid.equals("true")) {
            apiResponse.setStatusCode(400);
            apiResponse.setContent(isLastNameValid);
            return apiResponse;
        }

        //customer.setDob(new SimpleDateFormat("yyyy-MM-dd").parse(requestBody.createdAt));
        customer.setDob(requestBody.dob);
        customer.setCustomerId(UUID.randomUUID().toString());
        customer.setFirstName(requestBody.firstName);
        customer.setLastName(requestBody.lastName);
        customer.setEmail(requestBody.email);
        customer.setDob(requestBody.dob);

        try {
            jsonDBTemplate.insert(customer);
        } catch (InvalidJsonDbApiUsageException invalidJsonDbApiUsageException) {
            apiResponse.setStatusCode(400);
            apiResponse.setContent(JsonDBConstants.CUSTOMER_ALREADY_EXISTS);
            return apiResponse;
        }

        apiResponse.setStatusCode(201);
        SetCustomerResponse setCustomerResponse = new SetCustomerResponse();
        setCustomerResponse.setCustomerId(customer.getCustomerId());
        apiResponse.setContent(gson.toJson(setCustomerResponse));
        return apiResponse;
    }

    public static void dropCollection(JsonDBTemplate jsonDBTemplate) {
        jsonDBTemplate.dropCollection(Customer.class);
    }

}
