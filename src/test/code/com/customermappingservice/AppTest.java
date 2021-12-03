package com.customermappingservice;


import com.customermappingservice.constants.JsonDBConstants;
import com.customermappingservice.controllers.CustomerController;
import com.customermappingservice.models.ApiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import io.jsondb.JsonDBTemplate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;

public class AppTest {

    private static boolean serverStarted = false;

    @Before
    public void init() {

        if (serverStarted) {
            return;
        }

        //Start server
        String[] args = {"tst"};
        Application.main(args);
        serverStarted = true;
    }

    @After
    public void exit() {
        //stop server
        Application.stopServer();
    }

    @Test
    public void testStoreCustomerSuccess() throws JsonProcessingException, IOException, InterruptedException {

        var params = new HashMap<String, String>() {{
            put("customerId", "9876");
            put ("createdAt", "2021-11-01");
        }};

        var objectMapper = new ObjectMapper();
        String requestBody = objectMapper
                .writeValueAsString(params);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4568/postcustomer"))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        //drop the collection created for the test
        JsonDBTemplate jsonDBTemplate = new JsonDBTemplate(JsonDBConstants.JSONDB_DBFILESLOCATION, JsonDBConstants.JSONDB_BASEMODELSPACKAGE, null);
        CustomerController.dropCollection(jsonDBTemplate);

        System.out.println(response.body());
        Gson gson = new Gson();
        ApiResponse apiResponse = gson.fromJson(response.body(), ApiResponse.class);
        assert apiResponse.getStatusCode() == 201;
        assert apiResponse.getContent().equals("Customer added to the database with id: 9876");

    }

    @Test
    public void testGetCustomerSuccess() throws JsonProcessingException, IOException, InterruptedException {

        var params = new HashMap<String, String>() {{
            put("customerId", "9876");
            put ("createdAt", "2021-11-01");
        }};

        var objectMapper = new ObjectMapper();
        String requestBody = objectMapper
                .writeValueAsString(params);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4568/postcustomer"))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        client.send(request, HttpResponse.BodyHandlers.ofString());

        HttpClient client1 = HttpClient.newHttpClient();
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4568/getcustomer?customerId=9876"))
                .build();

        HttpResponse<String> response1 = client1.send(request1,
                HttpResponse.BodyHandlers.ofString());

        System.out.println(response1.body());
        Gson gson = new Gson();
        ApiResponse apiResponse = gson.fromJson(response1.body(), ApiResponse.class);
        assert apiResponse.getStatusCode() == 200;
        assert !apiResponse.getContent().equals("");

        //drop the collection created for the test
        JsonDBTemplate jsonDBTemplate = new JsonDBTemplate(JsonDBConstants.JSONDB_DBFILESLOCATION, JsonDBConstants.JSONDB_BASEMODELSPACKAGE, null);
        CustomerController.dropCollection(jsonDBTemplate);

    }

    @Test
    public void testPostFailNoRequestBody() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4568/postcustomer"))
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());
        Gson gson = new Gson();
        ApiResponse apiResponse = gson.fromJson(response.body(), ApiResponse.class);
        assert apiResponse.getStatusCode() == 400;
        assert apiResponse.getContent().equals("missing request body");
    }

    @Test
    public void testPostFailBadCreatedAtFormat() throws IOException, InterruptedException {

        var params = new HashMap<String, String>() {{
            put("customerId", "9876");
            put ("createdAt", "not a date");
        }};

        var objectMapper = new ObjectMapper();
        String requestBody = objectMapper
                .writeValueAsString(params);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4568/postcustomer"))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        //drop the collection created for the test
        JsonDBTemplate jsonDBTemplate = new JsonDBTemplate(JsonDBConstants.JSONDB_DBFILESLOCATION, JsonDBConstants.JSONDB_BASEMODELSPACKAGE, null);
        CustomerController.dropCollection(jsonDBTemplate);

        System.out.println(response.body());
        Gson gson = new Gson();
        ApiResponse apiResponse = gson.fromJson(response.body(), ApiResponse.class);
        assert apiResponse.getStatusCode() == 400;
        assert apiResponse.getContent().equals("CreatedAt must be in date format (yyyy-mm-dd)");

    }

}
