package com.terry.rest_demo;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class GreetingDemo {
    public static void main(String[] args) {
        SpringApplication.run(RestDemoApplication.class, args);
    }
}

@RestController
@RequestMapping("/greeting")
class GreatingController {
    @Value("${greeting-name: Mirage}")
    private String name;

    @GetMapping
    String getGreeting() {
        return name;
    }

    @Value("${greeting-coffee: ${greeting-name} is drinking Cafe Termopan}")
    private String coffee;

    @GetMapping("/coffee")
    String getNameAndCoffee() {
        return coffee;
    }
}