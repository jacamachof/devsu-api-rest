package com.jacamachof.devsuapirest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
})
public class DevsuApiRestApplication {

    public static void main(String[] args) {
        SpringApplication.run(DevsuApiRestApplication.class, args);
    }
}
