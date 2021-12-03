package com.customermappingservice.constants;

public class JsonDBConstants {
    public static final String JSONDB_DBFILESLOCATION = "./src/main/code/com/customermappingservice/data/";
    public static final String JSONDB_BASEMODELSPACKAGE = "com.customermappingservice.models";
    public static final String JSONDB_COLLECTION_NAME = "customers";

    //response messages
    public static final String CUSTOMER_UPLOAD_SUCCESS = "Customer added to the database with id: ";

    public static final String MISSING_CUSTOMER_ID = "missing customerId";
    public static final String MISSING_CREATED_AT = "missing createdAt";
    public static final String MISSING_REQUEST_BODY = "missing request body";
    public static final String INTERNAL_SERVER_ERROR = "internal server error";
    public static final String INVALID_ENDPOINT = "invalid endpoint";
    public static final String CREATE_AT_FORMAT_ERROR = "CreatedAt must be in date format (yyyy-mm-dd)";
    public static final String CREATE_AT_MUST_NOT_BE_FUTURE = "CreatedAt must not be a future date.";
    public static final String CUSTOMER_NOT_FOUND = "Customer not found from the id provided";
    public static final String CUSTOMER_ALREADY_EXISTS = "Customer already exists with that id";
}

