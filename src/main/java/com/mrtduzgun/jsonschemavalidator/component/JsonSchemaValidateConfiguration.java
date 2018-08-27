package com.mrtduzgun.jsonschemavalidator.component;

import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JsonSchemaValidateConfiguration {

    @Bean
    static BeanPostProcessor jsonRequestBodyArgumentResolverRegisteringBeanPostProcessor() {
        return new JsonRequestBodyArgumentResolverRegisteringBeanPostProcessor();
    }
}
