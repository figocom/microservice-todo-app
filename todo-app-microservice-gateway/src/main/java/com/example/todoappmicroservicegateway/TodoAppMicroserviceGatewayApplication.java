package com.example.todoappmicroservicegateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class TodoAppMicroserviceGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(TodoAppMicroserviceGatewayApplication.class, args);
    }

}
