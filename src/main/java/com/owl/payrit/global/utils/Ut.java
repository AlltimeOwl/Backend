package com.owl.payrit.global.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Ut {

    private static final String KOREA_CODE = "\\+82 .*";

    public static class str {

        public static String parsedPhoneNumber(String phoneNumber) {

            return phoneNumber.matches(KOREA_CODE) ? phoneNumber.replaceAll("\\+82 ", "0") : phoneNumber;
        }

    }

}
