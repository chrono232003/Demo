package com.customermappingservice.apiutils;

import com.customermappingservice.constants.JsonDBConstants;
import com.customermappingservice.models.Customer;
import io.jsondb.JsonDBTemplate;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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


    public static String isDateValidBirthDate(String date) {
        try {
            Date birthDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
            Date today = new Date();
            return (today.before(birthDate)) ? JsonDBConstants.DOB_MUST_NOT_BE_FUTURE : checkIfAgeIsOver18(birthDate);
        } catch (ParseException pe) {
            pe.printStackTrace();
            return JsonDBConstants.DOB_FORMAT_ERROR;
        }
    }

    public static String isCustomerIdValid(String id) {
        return "";
    }

    public static String isNameValid(String name) {
        String checkExp = "^[a-zA-Z\\s]+";
        return name.matches(checkExp) ? "true" : JsonDBConstants.NAME_INVALID_FORMAT;
    }

    public static String isEmailValid(String email) {
        String checkExp = "^[^[a-zA-Z0-9_+&*-]+(?:\\\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\\\.)+[a-zA-Z]{2,7}$\\s]+";
        return email.matches(checkExp) ? "true" : JsonDBConstants.EMAIL_INVALID_FORMAT;
    }


    /**
     * Private utility methods
     */

    private static String checkIfAgeIsOver18(Date date) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) - 18);
        return (calendar.getTime().after(date)) ? "true" : JsonDBConstants.DOB_MUST_BE_18;
    }

}
