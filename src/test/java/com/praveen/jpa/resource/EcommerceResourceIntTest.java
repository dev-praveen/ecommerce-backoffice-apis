package com.praveen.jpa.resource;

import com.praveen.jpa.config.TestJwtConfig;
import com.praveen.jpa.dao.CustomerRepository;
import com.praveen.jpa.dao.OrderRepository;
import com.praveen.jpa.dto.TokenRequest;
import com.praveen.jpa.dto.TokenResponse;
import com.praveen.jpa.entity.Address;
import com.praveen.jpa.entity.Customer;
import com.praveen.jpa.entity.Order;
import com.praveen.jpa.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@ActiveProfiles("test")
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = TestJwtConfig.class)
class EcommerceResourceIntTest {

  @Container @ServiceConnection
  private static final PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:15-alpine");

  @Autowired CustomerRepository customerRepository;
  @Autowired OrderRepository orderRepository;
  @LocalServerPort private Integer port;
  private RestClient restClient;
  private String jwtToken;

  private static List<Order> getOrders() {

    final var order1 = new Order();
    order1.setAmount(1233.09f);
    order1.setOrderTime(LocalDateTime.now());
    order1.setProductName("Mac");
    order1.setQuantity(1);
    order1.setStatus("active");

    final var order2 = new Order();
    order2.setAmount(788.09f);
    order2.setOrderTime(LocalDateTime.now());
    order2.setProductName("Lenovo");
    order2.setQuantity(3);
    order2.setStatus("cancelled");

    return List.of(order1, order2);
  }

  @BeforeEach
  void setUp() {
    restClient = RestClient.builder().baseUrl("http://localhost:" + port).build();
    jwtToken = obtainJwtToken();
    customerRepository.deleteAll();
  }

  private String obtainJwtToken() {

    TokenRequest request = new TokenRequest("praveen", "password", "read");
    TokenResponse response =
        restClient
            .post()
            .uri("/auth/token")
            .contentType(MediaType.APPLICATION_JSON)
            .body(request)
            .retrieve()
            .body(TokenResponse.class);
    assertThat(response).isNotNull();
    assertThat(response.token()).isNotBlank();
    return response.token();
  }

  @Test
  void postgresAndRabbitMQShouldCreateAndRun() {

    assertThat(postgres.isCreated()).isTrue();
    assertThat(postgres.isRunning()).isTrue();
  }

  @Test
  void shouldCreateCustomer() {

    final var response =
        restClient
            .post()
            .uri("/ecommerce/customer")
            .body(MockResourceData.getCustomerRequest())
            .contentType(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
            .retrieve()
            .toEntity(Long.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(response.getBody()).isNotNull();
  }

  @Test
  void shouldThrowExceptionForSameCustomer() {

    createCustomer();
    try {
      restClient
          .post()
          .uri("/ecommerce/customer")
          .body(MockResourceData.getCustomerRequest())
          .contentType(MediaType.APPLICATION_JSON)
          .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
          .retrieve()
          .toEntity(ProblemDetail.class);
    } catch (HttpClientErrorException exception) {
      final var message = exception.getMessage();
      assertThat(message).contains("Found another customer with same details in database");
      assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }
  }

  @Test
  void shouldUpdateExistingCustomerInformation() {

    final var existingCustomer = createCustomer().getId();
    final var customerUpdateInfo = MockResourceData.getCustomerUpdateInfo();
    customerUpdateInfo.setEmail("updatedemail@email.com");
    final var response =
        restClient
            .patch()
            .uri("/ecommerce/customer/{customerId}", existingCustomer)
            .body(customerUpdateInfo)
            .contentType(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
            .retrieve()
            .toEntity(Void.class);

    final var customerOptional = customerRepository.findById(existingCustomer);
    assertThat(customerOptional).isPresent();
    final var customer = customerOptional.get();
    assertThat(customer.getEmail()).isNotEqualTo("spraveen@email.com");
    assertThat(customer.getEmail()).isEqualTo("updatedemail@email.com");
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
  }

  @Test
  void shouldFetchAllCustomersWithAssociatedOrders() {

    insertCustomersIntoDatabase();
    final var response =
        restClient
            .get()
            .uri(
                uriBuilder ->
                    uriBuilder
                        .path("/ecommerce/customers")
                        .queryParam("pageNo", 1)
                        .queryParam("pageSize", 2)
                        .queryParam("sortBy", "id")
                        .queryParam("sortDirection", "asc")
                        .build())
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
            .retrieve()
            .toEntity(CustomerResponse.class);

    assertThat(response).isNotNull();
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    if (Objects.nonNull(response.getBody())) {
      final var customers = response.getBody().getCustomers();
      assertThat(customers).hasSize(2);
      assertThat(customers).extracting("orders").hasSize(2);
    }
  }

  @Test
  void shouldPlaceOrderByCustomerId() {

    final var existingCustomer = createCustomer().getId();
    final var response =
        restClient
            .post()
            .uri("/ecommerce/placeOrder/{customerId}", existingCustomer)
            .body(MockResourceData.getOrderRequest())
            .contentType(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
            .retrieve()
            .toEntity(Long.class);

    assertThat(response).isNotNull();
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
  }

  @Test
  void shouldGetAllOrdersForAParticularCustomer() {

    final var customer = createCustomer();
    final var response =
        restClient
            .get()
            .uri("/ecommerce/orders/{customerId}", customer.getId())
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
            .retrieve()
            .toEntity(new ParameterizedTypeReference<List<OrderRepresentation>>() {});

    assertThat(response).isNotNull();
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).hasSize(2);
  }

  @Test
  void shouldDeleteCustomer() {

    final var customer = createCustomer();
    final var response =
        restClient
            .delete()
            .uri("/ecommerce/customer/{customerId}", customer.getId())
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
            .retrieve()
            .toEntity(Void.class);

    final var optionalCustomer = customerRepository.findById(customer.getId());
    assertThat(response).isNotNull();
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    assertThat(optionalCustomer).isEmpty();
  }

  @Test
  void shouldFetchAllOrdersForAllCustomers() {

    createCustomer();
    final var response =
        restClient
            .get()
            .uri(
                uriBuilder ->
                    uriBuilder
                        .path("/ecommerce/orders")
                        .queryParam("orderStatus", "active")
                        .queryParam("pageNo", 1)
                        .queryParam("pageSize", 2)
                        .queryParam("sortBy", "id")
                        .queryParam("sortDirection", "asc")
                        .build())
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
            .retrieve()
            .toEntity(OrderResponse.class);

    assertThat(response).isNotNull();
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    if (Objects.nonNull(response.getBody())) {
      assertThat(response.getBody().getOrders()).hasSize(1);
    }
  }

  @Test
  void shouldGetCustomerById() {

    final var customer = createCustomer();
    final var response =
        restClient
            .get()
            .uri("/ecommerce/customer/{customerId}", customer.getId())
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
            .retrieve()
            .toEntity(CustomerRepresentation.class);

    assertThat(response).isNotNull();
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isNotNull();
  }

  @Test
  void shouldThrowExceptionForInvalidCustomerId() {

    try {
      restClient
          .get()
          .uri("/ecommerce/customer/{customerId}", 12)
          .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
          .retrieve()
          .toEntity(ProblemDetail.class);
    } catch (HttpClientErrorException exception) {
      final var message = exception.getMessage();
      assertThat(message).contains("Customer not found in database with id 12");
      assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
  }

  @Test
  void shouldReturnTrueIfMoreThanOneCustomerExists() {

    final var customer = createCustomer();
    final var exists = customerRepository.existsBy("praveen", "spraveen@email.com", "9848022338");
    final var orders = orderRepository.findByCustomerId(customer.getId());
    assertThat(orders).hasSize(2);
    assertThat(exists).isTrue();
  }

  @Test
  void shouldUpdateAddressForCustomer() {

    final var customer = createCustomer();
    final var response =
        restClient
            .put()
            .uri("/ecommerce/customer/address/{customerId}", customer.getId())
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
            .body(MockResourceData.getAddressRequest())
            .retrieve()
            .toEntity(Void.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    final var optionalCustomer = customerRepository.findById(customer.getId());
    if (optionalCustomer.isPresent()) {
      final var address = optionalCustomer.get().getAddress();
      assertThat(address).isNotNull();
      assertThat(address.getCity()).isNotEqualTo("khansar");
      assertThat(address.getCity()).isEqualTo("baroda");
    }
  }

  @Test
  void shouldFetchOnlyCustomersInfo() {

    insertCustomersIntoDatabase();
    final var response =
        restClient
            .get()
            .uri(
                uriBuilder ->
                    uriBuilder
                        .path("/ecommerce/customersInfo")
                        .queryParam("pageNo", 1)
                        .queryParam("pageSize", 2)
                        .queryParam("sortBy", "id")
                        .queryParam("sortDirection", "asc")
                        .build())
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
            .retrieve()
            .toEntity(CustomerInfoData.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response).isNotNull();
    if (Objects.nonNull(response.getBody())) {
      assertThat(response.getBody().getCustomers()).hasSize(2);
    }
  }

  @Test
  void shouldCancelAnOrderForACustomer() {

    final var customer = createCustomer();
    final var order = customer.getOrders().stream().findFirst();
    final var orderId = order.map(Order::getId).orElseThrow();
    final var response =
        restClient
            .delete()
            .uri(
                "/ecommerce/cancelOrder/customer/{customerId}/order/{orderId}",
                customer.getId(),
                orderId)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
            .retrieve()
            .toEntity(Void.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    assertThat(response).isNotNull();

    final var orderOptional = orderRepository.findById(orderId);
    if (orderOptional.isPresent()) {
      final var dbOrder = orderOptional.get();
      assertThat(dbOrder.getStatus()).isEqualTo("cancelled");
      assertThat(dbOrder.getCancelledAt()).isNotNull();
    }
  }

  @Test
  void shouldThrowExceptionForNonExistentOrder() {

    final var customer = createCustomer();
    try {
      restClient
          .delete()
          .uri("/ecommerce/cancelOrder/customer/{customerId}/order/{orderId}", customer.getId(), 1)
          .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
          .retrieve()
          .toEntity(ProblemDetail.class);
    } catch (HttpClientErrorException exception) {
      final var message = exception.getMessage();
      assertThat(message).contains("Order not found in database with id 1");
      assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
  }

  @Test
  void shouldThrowExceptionForAlreadyCancelledOrder() {

    final var customer = createCustomer();
    final var order = customer.getOrders().get(1);
    final var orderId = order.getId();
    try {
      restClient
          .delete()
          .uri(
              "/ecommerce/cancelOrder/customer/{customerId}/order/{orderId}",
              customer.getId(),
              orderId)
          .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
          .retrieve()
          .toEntity(ProblemDetail.class);
    } catch (HttpClientErrorException exception) {
      final var message = exception.getMessage();
      assertThat(message).contains("order " + orderId + " is already cancelled");
      assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }
  }

  @Test
  void shouldSearchAndFetchCustomersByName() {

    insertCustomersIntoDatabase();
    final var response =
        restClient
            .get()
            .uri("/ecommerce/customer/search/{customerName}", "rana")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
            .retrieve()
            .toEntity(new ParameterizedTypeReference<List<CustomerInfo>>() {});

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody()).hasSize(1);
  }

  @Test
  void shouldThrowExceptionForSearchCustomersByName() {

    insertCustomersIntoDatabase();
    String searchName = "abc";
    try {
      restClient
          .get()
          .uri("/ecommerce/customer/search/{customerName}", searchName)
          .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
          .retrieve()
          .toEntity(ProblemDetail.class);
    } catch (HttpClientErrorException exception) {
      final var message = exception.getMessage();
      assertThat(message).contains("No customer(s) found in database with the name " + searchName);
      assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
  }

  private void insertCustomersIntoDatabase() {

    final var customer1 = new Customer();
    final var address1 = new Address();

    address1.setPinCode("4376437");
    customer1.setFirstName("rana");
    customer1.setLastName("naidu");
    customer1.setEmail("rana@email.com");
    customer1.setAddress(address1);
    customer1.setContactNumber("7647326427");
    customer1.setOrders(getOrders());

    final var customer2 = new Customer();
    final var address2 = new Address();
    address2.setPinCode("090889");
    customer2.setFirstName("mahesh");
    customer2.setLastName("babu");
    customer2.setEmail("mahesh@email.com");
    customer2.setAddress(address2);
    customer2.setContactNumber("111222");
    customer2.setOrders(Collections.emptyList());

    customerRepository.saveAll(List.of(customer1, customer2));
  }

  private Customer createCustomer() {

    final var customer = new Customer();
    final var address = new Address();
    address.setPinCode("600032");
    address.setCity("khansar");
    address.setLandmark("Dharavi");
    address.setHouseNo("#1-199/A");
    address.setStreet("gandhi road");
    customer.setFirstName("praveen");
    customer.setEmail("spraveen@email.com");
    customer.setAddress(address);
    customer.setContactNumber("9848022338");
    customer.setOrders(getOrders());

    return customerRepository.save(customer);
  }
}
