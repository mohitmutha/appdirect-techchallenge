package com.mm.appdirect.techchallenge;


import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import static springfox.documentation.builders.PathSelectors.regex;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableAutoConfiguration // spring-boot auto-configuration
@EnableConfigurationProperties
@ComponentScan
@EnableSwagger2
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    
    //Swagger
    @Bean
    public Docket accountsApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("accounts")
                .apiInfo(apiInfo())
                .select()
                .paths(regex("/accounts.*"))
                .build();
    }
     
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Appdirect techchallenge implementation")
                .description("Appdirect techchallenge implementation")
                .contact(new Contact("Mohit", "", "mohitmutha@gmail.com"))
                .version("2.0")
                .build();
    }

}

