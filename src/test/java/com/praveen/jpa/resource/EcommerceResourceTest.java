package com.praveen.jpa.resource;

import com.praveen.jpa.model.AddressRepresentation;
import com.praveen.jpa.model.CreateCustomerRequest;
import com.praveen.jpa.service.EcommerceService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = EcommerceResource.class)
class EcommerceResourceTest {

  @Autowired private MockMvc mockMvc;
  @MockBean private EcommerceService ecommerceService;

  private static void createCustomer() {
    CreateCustomerRequest request =
        CreateCustomerRequest.builder()
            .firstName("Alice")
            .lastName("Smith")
            .email("alice.smith@example.com")
            .contactNumber(9876543210L)
            .address(
                AddressRepresentation.builder()
                    .houseNo("456")
                    .street("Broadway")
                    .landmark("Next to Mall")
                    .pinCode("54321")
                    .city("New City")
                    .build())
            .build();
  }

  @BeforeEach
  void setUp() {}

  @AfterEach
  void tearDown() {}

  @Test
  void shouldCreateCustomer() throws Exception {

    String jsonRequest =
        """
    {
        "firstName": "Alice",
        "lastName": "Smith",
        "email": "alice.smith@example.com",
        "contactNumber": 9876543210,
        "address": {
            "houseNo": "456",
            "street": "Broadway",
            "landmark": "Next to Mall",
            "pinCode": "54321",
            "city": "New City"
        }
    }
    """;

    when(ecommerceService.saveCustomer(any(CreateCustomerRequest.class))).thenReturn(1);

    final var resultActions =
        mockMvc
            .perform(
                post("/ecommerce/create-customer")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonRequest))
            .andExpect(status().isCreated());

    final var response = resultActions.andReturn().getResponse();
    assertThat(response).isNotNull();
  }

  @Test
  void getAllCustomers() {}

  @Test
  void placeOrderByCustomerId() {}

  @Test
  void getOrders() {}

  @Test
  void deleteCustomer() {}

  @Test
  void fetchOrders() {}
}
