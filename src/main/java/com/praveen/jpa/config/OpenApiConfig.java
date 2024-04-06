package com.praveen.jpa.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  private static final String DESCRIPTION =
      """
               The E-Commerce API provides endpoints to manage customers, orders in an e-commerce system.
               It allows clients to retrieve customer information, place orders, manage customer accounts, and retrieve order details.
            """;

  @Bean(name = "apiInfo")
  public OpenAPI apiInfo() {
    return new OpenAPI()
        .info(new Info().title("E-Commerce API").description(DESCRIPTION).version("v1"));
  }
}
