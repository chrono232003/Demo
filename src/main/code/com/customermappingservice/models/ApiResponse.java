package com.customermappingservice.models;

public class ApiResponse {

    int statusCode;
    String content;

    public int getStatusCode() {
        return statusCode;
    }

    public String getContent() {
        return content;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
