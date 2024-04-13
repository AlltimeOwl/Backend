package com.owl.payrit.global.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Ut {

    private static final String KOREA_CODE = "\\+82 .*";

    public static class str {

        public static String parsedPhoneNumber(String phoneNumber) {

            if(phoneNumber.matches(KOREA_CODE)) {
                phoneNumber = phoneNumber.replaceAll("\\+82 ", "0");
            }

            if(phoneNumber.contains("-")) {
                phoneNumber = phoneNumber.replaceAll("-", "");
            }

            return phoneNumber;
        }

        public static String getCardNumberPrefix(String cardNumber) {

            return " (" + cardNumber.substring(0, 4) + ")";
        }

    }

}
