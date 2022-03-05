package com.customermappingservice.models;

import java.util.Date;

import io.jsondb.annotation.Document;
import io.jsondb.annotation.Id;

@Document(collection = "customers", schemaVersion= "1.0")
public class Customer {

    @Id
    private String customerId;
    private String firstName;
    private String lastName;
    private String email;
    private String dob;

    //@Secret
    //private String privateKey;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

}
