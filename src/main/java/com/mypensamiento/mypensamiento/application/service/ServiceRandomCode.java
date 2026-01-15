package com.mypensamiento.mypensamiento.application.service;

import java.security.SecureRandom;

public class ServiceRandomCode {

    public  String generateSixDigitCode() {
        SecureRandom random = new SecureRandom();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }
}
