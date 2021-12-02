package com.customermappingservice.models;

import java.util.Date;

import io.jsondb.annotation.Document;
import io.jsondb.annotation.Id;

@Document(collection = "customers", schemaVersion= "1.0")
public class Customer {

    @Id
    private int customerId;
    private String externalId;
    private Date createdAt;

    //@Secret
    //private String privateKey;

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

}
