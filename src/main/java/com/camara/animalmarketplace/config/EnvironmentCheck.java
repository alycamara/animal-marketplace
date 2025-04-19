package com.camara.animalmarketplace.config;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;



@Component
public class EnvironmentCheck {

    @Value("${DB_URL:NOT_SET}")
    private String dbUrl;

    @Value("${DB_USERNAME:NOT_SET}")
    private String dbUsername;

    @Value("${DB_PASSWORD:NOT_SET}")
    private String dbPassword;

    @PostConstruct
    public void checkEnvironmentVariables() {
        System.out.println("DB_URL: " + dbUrl);
        System.out.println("DB_USERNAME: " + dbUsername);
        System.out.println("DB_PASSWORD: " + dbPassword);
    }
}