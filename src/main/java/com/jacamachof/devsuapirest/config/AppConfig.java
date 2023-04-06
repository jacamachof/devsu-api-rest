package com.jacamachof.devsuapirest.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
@ConfigurationProperties(prefix = "devsu")
@Getter
@Setter
public class AppConfig {

    private BigDecimal dailyLimit;
    private Integer maxDateRange;
}
