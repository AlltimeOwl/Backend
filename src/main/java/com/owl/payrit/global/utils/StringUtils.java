package com.owl.payrit.global.utils;

import org.springframework.stereotype.Component;

@Component
public class StringUtils {

    private static final String KOREA_CODE = "\\+82 ";

    public String parsedPhoneNumber(String phoneNumber) {

        return phoneNumber.contains(KOREA_CODE) ? phoneNumber.replaceAll(KOREA_CODE, "0") : phoneNumber;
    }

}
