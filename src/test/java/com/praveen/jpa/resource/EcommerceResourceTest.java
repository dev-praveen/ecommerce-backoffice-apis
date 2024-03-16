package com.praveen.jpa.resource;

import com.praveen.jpa.model.CreateCustomerRequest;
import com.praveen.jpa.model.OrderRequest;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = EcommerceResource.class)
class EcommerceResourceTest {

  @Autowired private MockMvc mockMvc;
  @MockBean private EcommerceService ecommerceService;

  @BeforeEach
  void setUp() {}

  @AfterEach
  void tearDown() {}

  @Test
  void shouldCreateCustomer() throws Exception {

    when(ecommerceService.saveCustomer(any(CreateCustomerRequest.class))).thenReturn(1);

    final var resultActions =
        mockMvc
            .perform(
                post("/ecommerce/customer")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(MockResourceData.customerJsonRequest()))
            .andExpect(status().isCreated());

    final var response = resultActions.andReturn().getResponse();
    assertThat(response).isNotNull();
    assertThat(response.getContentAsString()).isEqualTo("1");
  }

  @Test
  void shouldUpdateCustomer() throws Exception {

    doNothing()
        .when(ecommerceService)
        .updateCustomer(any(Integer.class), any(CreateCustomerRequest.class));

    final var resultActions =
        mockMvc
            .perform(
                put("/ecommerce/customer/{customerId}", 2)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(MockResourceData.customerJsonRequest()))
            .andExpect(status().isAccepted());

    final var response = resultActions.andReturn().getResponse();
    assertThat(response).isNotNull();
  }

  @Test
  void getAllCustomers() throws Exception {

    when(ecommerceService.findAllCustomers()).thenReturn(MockResourceData.getMockCustomers());

    final var response =
        mockMvc
            .perform(get("/ecommerce/customers").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    assertThat(response).isNotNull();
  }

  @Test
  void placeOrderByCustomerId() throws Exception {

    when(ecommerceService.saveOrder(any(Integer.class), any(OrderRequest.class))).thenReturn(1l);
    final var resultActions =
        mockMvc
            .perform(
                post("/ecommerce/placeOrder/{customerId}", 100)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(MockResourceData.orderJsonRequest()))
            .andExpect(status().isCreated());
    final var response = resultActions.andReturn().getResponse();
    assertThat(response.getContentAsString()).isEqualTo("1");
  }

  @Test
  void getOrdersByCustomerId() throws Exception {

    when(ecommerceService.findAllOrders(1)).thenReturn(MockResourceData.getMockOrders());

    final var response =
        mockMvc
            .perform(
                get("/ecommerce/orders/{customerId}", 1).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    assertThat(response).isNotNull();
  }

  @Test
  void deleteCustomer() throws Exception {

    doNothing().when(ecommerceService).deleteCustomer(any(Integer.class));

    final var response =
        mockMvc
            .perform(
                delete("/ecommerce/customer/{customerId}", 1)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

    verify(ecommerceService, times(1)).deleteCustomer(1);
    assertThat(response).isNotNull();
  }

  @Test
  void fetchAllOrders() throws Exception {

    when(ecommerceService.fetchAllOrders()).thenReturn(MockResourceData.getMockOrders());

    final var response =
        mockMvc
            .perform(get("/ecommerce/orders").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    assertThat(response).isNotNull();
  }

  @Test
  void shouldFetchCustomerById() throws Exception {

    when(ecommerceService.getCustomer(any(Integer.class)))
        .thenReturn(MockResourceData.getMockCustomers().get(1));

    final var response =
        mockMvc
            .perform(
                get("/ecommerce/customer/{customerId}", 2).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    assertThat(response).isNotNull();
  }
}
