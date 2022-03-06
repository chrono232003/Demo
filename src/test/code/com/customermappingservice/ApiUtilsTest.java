package com.customermappingservice;

import com.customermappingservice.apiutils.ApiUtils;
import com.customermappingservice.constants.JsonDBConstants;
import com.customermappingservice.models.Customer;
import io.jsondb.JsonDBTemplate;
import org.junit.Test;

public class ApiUtilsTest {

    @Test
    public void testCheckForPastDate() throws Exception {
        assert ApiUtils.isDateValidBirthDate("1983-09-01").equals("true");
        assert ApiUtils.isDateValidBirthDate("2021-12-07").equals(JsonDBConstants.DOB_MUST_NOT_BE_FUTURE);
        assert ApiUtils.isDateValidBirthDate("not a date").equals(JsonDBConstants.DOB_FORMAT_ERROR);
        assert ApiUtils.isDateValidBirthDate("2015-03-12").equals(JsonDBConstants.DOB_MUST_BE_18);
    }

    @Test
    public void testIsNameValid() throws Exception {
        assert ApiUtils.isNameValid("Joe").equals("true");
        assert ApiUtils.isNameValid("23423$#").equals(JsonDBConstants.NAME_INVALID_FORMAT);
        assert ApiUtils.isNameValid("John1231").equals(JsonDBConstants.NAME_INVALID_FORMAT);
    }

    @Test
    public void testIsEmailValid() throws Exception {

        assert ApiUtils.isEmailValid("johnsmith@company.com").equals("true");
        assert ApiUtils.isEmailValid("gdfgdf79").equals(JsonDBConstants.EMAIL_INVALID_FORMAT);
        assert ApiUtils.isEmailValid("test@.com").equals(JsonDBConstants.EMAIL_INVALID_FORMAT);
        assert ApiUtils.isEmailValid("test.com").equals(JsonDBConstants.EMAIL_INVALID_FORMAT);
    }

    @Test
    public void testCustomerCollectionExistsFalse() throws Exception {
        JsonDBTemplate jsonDBTemplate = new JsonDBTemplate(JsonDBConstants.JSONDB_DBFILESLOCATION, JsonDBConstants.JSONDB_BASEMODELSPACKAGE, null);
        assert !ApiUtils.customerCollectionExists(jsonDBTemplate);
    }

    @Test
    public void testCustomerCollectionExistsTrue() throws Exception {
        JsonDBTemplate jsonDBTemplate = new JsonDBTemplate(JsonDBConstants.JSONDB_DBFILESLOCATION, JsonDBConstants.JSONDB_BASEMODELSPACKAGE, null);
        jsonDBTemplate.createCollection(Customer.class);
        assert ApiUtils.customerCollectionExists(jsonDBTemplate);
        jsonDBTemplate.dropCollection(Customer.class);
    }

    @Test
    public void queryCustomerByIdNotFound() {
        JsonDBTemplate jsonDBTemplate = new JsonDBTemplate(JsonDBConstants.JSONDB_DBFILESLOCATION, JsonDBConstants.JSONDB_BASEMODELSPACKAGE, null);
        jsonDBTemplate.createCollection(Customer.class);
        Customer customer  = ApiUtils.queryCustomerById("12345", jsonDBTemplate);
        jsonDBTemplate.dropCollection(Customer.class);
        assert customer == null;
    }

//    @Test
//    public void queryCustomerByIdFound() {
//        JsonDBTemplate jsonDBTemplate = new JsonDBTemplate(JsonDBConstants.JSONDB_DBFILESLOCATION, JsonDBConstants.JSONDB_BASEMODELSPACKAGE, null);
//        jsonDBTemplate.createCollection(Customer.class);
//        Customer mockCustomer = new Customer();
//        mockCustomer.setCustomerId("sdfsdfsdf");
//        //mockCustomer.setExternalId("some external id");
//        jsonDBTemplate.insert(mockCustomer);
//
//        Customer customer  = ApiUtils.queryCustomerById("12323", jsonDBTemplate);
//        jsonDBTemplate.dropCollection(Customer.class);
//        assert customer != null;
//    }
}
