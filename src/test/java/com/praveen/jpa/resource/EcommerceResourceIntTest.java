package com.praveen.jpa.resource;

import com.praveen.jpa.model.AddressRepresentation;
import com.praveen.jpa.model.CreateCustomerRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EcommerceResourceIntTest {

  @Container @ServiceConnection
  private static final PostgreSQLContainer<?> postgres =
      new PostgreSQLContainer<>("postgres:15-alpine");

  @LocalServerPort private Integer port;
  private RestClient restClient;

  @BeforeEach
  void setUp() {
    restClient = RestClient.create("http://localhost:" + port + "/ecommerce");
  }

  @Test
  @Order(1)
  void postgresShouldCreateAndRun() {

    assertThat(postgres.isCreated()).isTrue();
    assertThat(postgres.isRunning()).isTrue();
  }

  @Test
  void shouldCreateCustomer() {

    final var customerRequest =
        CreateCustomerRequest.builder()
            .firstName("praveen")
            .lastName("sana")
            .email("spraveen@email.com")
            .contactNumber("7406120335")
            .address(AddressRepresentation.builder().pinCode("516362").build())
            .build();
    final var response =
        restClient
            .post()
            .uri("/customer")
            .body(customerRequest)
            .contentType(MediaType.APPLICATION_JSON)
            .retrieve()
            .toEntity(Long.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(response.getBody()).isEqualTo(165850L);
  }
}
