package com.praveen.jpa.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class OpenApiConfig {

  private static final String DESCRIPTION =
      """
               The E-Commerce API provides endpoints to manage customers, orders in an e-commerce system.
               It allows clients to retrieve customer information, place orders, manage customer accounts, and retrieve order details.
            """;

  @Bean(name = "apiInfo")
  public OpenAPI apiInfo() {

    final String securitySchemeName = "bearerAuth";

    Map<String, Object> extensions = new HashMap<>();
    extensions.put("Mobile number", "+91-6301892380");

    return new OpenAPI()
        .info(
            new Info()
                .title("E-Commerce API")
                .description(DESCRIPTION)
                .version("v1")
                .license(new License().name("Apache 2.0").url("https://springdoc.org"))
                .contact(
                    new Contact()
                        .email("spraveenebdeveloper@gmailcom")
                        .url("https://github.com/dev-praveen")
                        .name("Praveen")
                        .extensions(extensions))
                .termsOfService(
                    "https://docs.github.com/en/site-policy/github-terms/github-terms-of-service"))
        .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
        .components(
            new Components()
                .addSecuritySchemes(
                    securitySchemeName,
                    new SecurityScheme()
                        .name(securitySchemeName)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")));
  }
}
