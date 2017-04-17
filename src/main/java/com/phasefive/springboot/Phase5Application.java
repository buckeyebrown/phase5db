package com.phasefive.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Phase5Application {
    public static void main(String[] args) {
    	ApplicationContext ctx = SpringApplication.run(Phase5Application.class, args);
    	System.out.println("######################################");
    	System.out.println("Spring boot app for Phase 5 is now running. Visit http://localhost:8060 to find the index.");
    	System.out.println("######################################");
    }
}
