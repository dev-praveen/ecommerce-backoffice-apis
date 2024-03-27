package com.praveen.jpa.resource;

import com.praveen.jpa.dao.CustomerRepository;
import com.praveen.jpa.entity.Address;
import com.praveen.jpa.entity.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.HttpClientErrorException;
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

  @Autowired CustomerRepository customerRepository;
  @LocalServerPort private Integer port;
  private RestClient restClient;

  @BeforeEach
  void setUp() {
    restClient = RestClient.create("http://localhost:" + port + "/ecommerce");
    customerRepository.deleteAll();
  }

  @Test
  @Order(1)
  void postgresShouldCreateAndRun() {

    assertThat(postgres.isCreated()).isTrue();
    assertThat(postgres.isRunning()).isTrue();
  }

  @Test
  void shouldCreateCustomer() {

    final var response =
        restClient
            .post()
            .uri("/customer")
            .body(MockResourceData.getCustomerRequest())
            .contentType(MediaType.APPLICATION_JSON)
            .retrieve()
            .toEntity(Long.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(response.getBody()).isNotNull();
  }

  @Test
  void shouldThrowExceptionForSameCustomer() {

    createCustomer();
    try {
      final var response =
          restClient
              .post()
              .uri("/customer")
              .body(MockResourceData.getCustomerRequest())
              .contentType(MediaType.APPLICATION_JSON)
              .retrieve()
              .toEntity(ProblemDetail.class);
    } catch (HttpClientErrorException exception) {
      final var message = exception.getMessage();
      assertThat(message).contains("Found another customer with same details in database");
      assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
  }

  @Test
  void shouldUpdateCustomer() {

    createCustomer();
    final var customerRequest = MockResourceData.getCustomerRequest();
    customerRequest.setEmail("updatedemail@email.com");
    final var response =
        restClient
            .put()
            .uri("/customer/{customerId}", 165850)
            .body(customerRequest)
            .contentType(MediaType.APPLICATION_JSON)
            .retrieve()
            .toEntity(Void.class);

    final var customerOptional = customerRepository.findById(165850L);
    assertThat(customerOptional).isPresent();
    final var customer = customerOptional.get();
    assertThat(customer.getEmail()).isNotEqualTo("spraveen@email.com");
    assertThat(customer.getEmail()).isEqualTo("updatedemail@email.com");
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
  }

  private void createCustomer() {

    final var customer = new Customer();
    final var address = new Address();
    address.setPinCode("600032");
    customer.setFirstName("praveen");
    customer.setEmail("spraveen@email.com");
    customer.setAddress(address);
    customer.setContactNumber("9848022338");
    customer.setOrders(null);

    customerRepository.save(customer);
  }
}
