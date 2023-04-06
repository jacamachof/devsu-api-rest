package com.jacamachof.devsuapirest.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.SecureRandom;

public class SecurityUtils {

    private SecurityUtils() {
        throw new AssertionError();
    }

    private static final BCryptPasswordEncoder PASSWORD_ENCODER =
            new BCryptPasswordEncoder(10, new SecureRandom());

    public static String encode(String raw) {
        return PASSWORD_ENCODER.encode(raw);
    }

    public static boolean matches(String raw, String encoded) {
        return PASSWORD_ENCODER.matches(raw, encoded);
    }
}
