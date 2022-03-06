package code.com.customermappingservice;


import code.com.customermappingservice.models.SetCustomerResponse;
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

    Gson gson = new Gson();
    private static boolean serverStarted = false;

    @Before
    public void init() {

        if (serverStarted) {
            return;
        }

        //Start server
        String[] args = {"tst"};
        com.customermappingservice.Application.main(args);
        serverStarted = true;
    }

    @After
    public void exit() {
        //stop server
        com.customermappingservice.Application.stopServer();
    }

    @Test
    public void testStoreCustomerSuccess() throws JsonProcessingException, IOException, InterruptedException {

        var params = new HashMap<String, String>() {{
            put("firstName", "John");
            put("lastName", "Smith");
            put("email", "john.smith@company.com");
            put("dob", "1983-04-10");
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
        //CustomerController.dropCollection(jsonDBTemplate);

        System.out.println(response.body());
        Gson gson = new Gson();
        ApiResponse postCustomerApiResponse = gson.fromJson(response.body(), ApiResponse.class);
        SetCustomerResponse setCustomerResponse = gson.fromJson(postCustomerApiResponse.getContent(), SetCustomerResponse.class);
        assert postCustomerApiResponse.getStatusCode() == 201;
        assert setCustomerResponse.getCustomerId() != null;
    }

    @Test
    public void testGetCustomerSuccess() throws JsonProcessingException, IOException, InterruptedException {

        HashMap params = new HashMap<String, String>() {{
            put("firstName", "John");
            put("lastName", "Smith");
            put("email", "john.smith@company.com");
            put("dob", "1983-04-10");
        }};

        var objectMapper = new ObjectMapper();
        String requestBody = objectMapper
                .writeValueAsString(params);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest postCustomerRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4568/postcustomer"))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse postCustomerResponse = client.send(postCustomerRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println(postCustomerResponse.body());
        ApiResponse postCustomerApiResponse = gson.fromJson(postCustomerResponse.body().toString(), ApiResponse.class);
        SetCustomerResponse setCustomerResponse = gson.fromJson(postCustomerApiResponse.getContent(), SetCustomerResponse.class);

        String getRequestURL = "http://localhost:4568/getcustomer?customerId=" + setCustomerResponse.getCustomerId();
        HttpClient client1 = HttpClient.newHttpClient();
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(URI.create(getRequestURL))
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
    public void testPostFailBadFirstNameFormat() throws IOException, InterruptedException {

        var params = new HashMap<String, String>() {{
            put("firstName", "John#$%#$23423;");
            put("lastName", "Smith");
            put("email", "john.smith@company.com");
            put("dob", "1983-04-10");
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
        assert apiResponse.getContent().equals("The provided name is in an invalid format");
    }

}
