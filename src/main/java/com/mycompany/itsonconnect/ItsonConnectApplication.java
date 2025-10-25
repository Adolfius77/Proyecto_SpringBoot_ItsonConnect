package com.mycompany.itsonconnect;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan; 
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
    "com.mycompany.itsonconnect",
    "controller",
    "service", 
    "Config"
})
@EntityScan(basePackages = {"model", "com.mycompany.itsonconnect"}) 
public class ItsonConnectApplication {

    public static void main(String[] args) {

        SpringApplication.run(ItsonConnectApplication.class, args);
    }

}