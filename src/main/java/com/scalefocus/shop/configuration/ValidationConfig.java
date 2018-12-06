package com.scalefocus.shop.configuration;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * <b>A configuration class for configuring the MessageSource used to validate input data.</b>
 */
@Configuration
public class ValidationConfig {

    /**
     * MessageSource configuration bean.
     */
    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames("message");

        return messageSource;
    }

    /**
     * LocalValidatorFactoryBean configuration.
     */
    @Bean
    public LocalValidatorFactoryBean validatorFactoryBean(MessageSource messageSource) {
        LocalValidatorFactoryBean factoryBean = new LocalValidatorFactoryBean();
        factoryBean.setValidationMessageSource(messageSource);

        return factoryBean;
    }
}
