package com.praveen.jpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class EcommerceBackofficeApplication {

  public static void main(String[] args) {
    SpringApplication.run(EcommerceBackofficeApplication.class, args);
  }
}
