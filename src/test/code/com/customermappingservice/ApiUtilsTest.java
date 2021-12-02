package com.customermappingservice;

import com.customermappingservice.apiutils.ApiUtils;
import com.customermappingservice.constants.JsonDBConstants;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ApiUtilsTest {

    @Test
    public void checkForFutureDate() throws Exception {
        assert ApiUtils.isDateValidFutureDate("2021-09-01").equals("true");
        assert ApiUtils.isDateValidFutureDate("2021-12-07").equals(JsonDBConstants.CREATE_AT_MUST_NOT_BE_FUTURE);
        assert ApiUtils.isDateValidFutureDate("not a date").equals(JsonDBConstants.CREATE_AT_FORMAT_ERROR);
    }
}
