package com.customermappingservice;

import com.customermappingservice.constants.Path;
import com.customermappingservice.controllers.CustomerController;

import static spark.Spark.*;

public class Application {

    public static void main(String[] args) {

        // Configure Spark
        port(4568);

        //Routes
        get(Path.GETCUSTOMER,         CustomerController.handleGetCustomer);
        post(Path.POSTCUSTOMER,         CustomerController.handlePostCustomer);
        get("*",                     CustomerController.notFound);

    }

    public static void stopServer() {
        stop();
    }

}
