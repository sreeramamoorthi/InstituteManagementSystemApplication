package com.institute;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"com.institute", "controller", "service", "security", "exception"})
@EntityScan(basePackages = "entity")
@EnableJpaRepositories(basePackages = "repository")
public class InstituteManagementSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(InstituteManagementSystemApplication.class, args);
    }
}