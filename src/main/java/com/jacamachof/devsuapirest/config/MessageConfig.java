package com.jacamachof.devsuapirest.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.ResourceBundle;

@Configuration
@Getter
@Setter
public class MessageConfig {

    private String deposit;
    private String withdrawal;

    @Bean
    public ResourceBundle resourceBundle() {
        ResourceBundle bundle = ResourceBundle.getBundle("messages");
        setDeposit(bundle.getString("message.deposit"));
        setWithdrawal(bundle.getString("message.withdrawal"));
        return bundle;
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean
    public LocalValidatorFactoryBean getValidator() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource());
        return bean;
    }
}
