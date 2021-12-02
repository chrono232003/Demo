package com.customermappingservice.apiutils;

import com.customermappingservice.constants.JsonDBConstants;
import com.customermappingservice.models.Customer;
import io.jsondb.JsonDBTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ApiUtils {

    //get handles
    public static boolean customerCollectionExists(JsonDBTemplate jsonDBTemplate) {
        if (jsonDBTemplate.collectionExists(JsonDBConstants.JSONDB_COLLECTION_NAME)) {
            return true;
        } else {
            System.out.println("The customer collection has not been created yet.");
            return false;
        }
    }

    public static Customer queryCustomerById(String id, JsonDBTemplate jsonDBTemplate) {
        String query = String.format("/.[customerId='%s']", id);
        List docs = jsonDBTemplate.find(query, Customer.class);
        return (!docs.isEmpty()) ? (Customer) docs.get(0) : null;
    }

    //post handles


    public static String isDateValidFutureDate(String date) {
        try {
            Date createdAtDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
            Date today = new Date();
            return (today.before(createdAtDate)) ?  JsonDBConstants.CREATE_AT_MUST_NOT_BE_FUTURE : "true";
        } catch (ParseException pe) {
            pe.printStackTrace();
            return JsonDBConstants.CREATE_AT_FORMAT_ERROR;
        }
    }

    public static String isCustomerIdValid(String id) {
        return "";
    }

}
