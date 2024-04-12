package com.praveen.jpa.resource;

import com.praveen.jpa.api.EcommerceApi;
import com.praveen.jpa.exception.CustomerNotFoundException;
import com.praveen.jpa.model.AddressRepresentation;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = EcommerceApi.class)
class EcommerceResourceTest {

  @Autowired private MockMvc mockMvc;
  @MockBean private EcommerceService ecommerceService;

  @BeforeEach
  void setUp() {}

  @AfterEach
  void tearDown() {}

  @Test
  void shouldCreateCustomer() throws Exception {

    when(ecommerceService.saveCustomer(any(CreateCustomerRequest.class))).thenReturn(1L);

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
  void shouldThrowExceptionForInvalidCustomerRequest() throws Exception {

    final var resultActions =
        mockMvc
            .perform(
                post("/ecommerce/customer")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(MockResourceData.invalidCustomerJsonRequest()))
            .andExpect(status().isBadRequest());

    verify(ecommerceService, times(0)).saveCustomer(any(CreateCustomerRequest.class));
    final var response = resultActions.andReturn().getResponse();
    assertThat(response).isNotNull();
  }

  @Test
  void shouldUpdateCustomer() throws Exception {

    doNothing()
        .when(ecommerceService)
        .updateCustomer(any(Long.class), any(CreateCustomerRequest.class));

    final var resultActions =
        mockMvc
            .perform(
                put("/ecommerce/customer/{customerId}", 2)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(MockResourceData.customerJsonRequest()))
            .andExpect(status().isNoContent());

    final var response = resultActions.andReturn().getResponse();
    assertThat(response).isNotNull();
  }

  @Test
  void getAllCustomers() throws Exception {

    when(ecommerceService.findAllCustomers(anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(MockResourceData.getMockCustomersResponse());

    final var response =
        mockMvc
            .perform(get("/ecommerce/customers").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    assertThat(response).isNotNull();
  }

  @Test
  void placeOrderByCustomerId() throws Exception {

    when(ecommerceService.saveOrder(any(Long.class), any(OrderRequest.class))).thenReturn(1L);
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

    when(ecommerceService.findAllOrders(1L)).thenReturn(MockResourceData.getMockOrders());

    final var response =
        mockMvc
            .perform(
                get("/ecommerce/orders/{customerId}", 1).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    assertThat(response).isNotNull();
  }

  @Test
  void deleteCustomer() throws Exception {

    doNothing().when(ecommerceService).deleteCustomer(any(Long.class));

    final var response =
        mockMvc
            .perform(
                delete("/ecommerce/customer/{customerId}", 1)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

    verify(ecommerceService, times(1)).deleteCustomer(1L);
    assertThat(response).isNotNull();
  }

  @Test
  void fetchAllOrders() throws Exception {

    when(ecommerceService.fetchAllOrders(anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(MockResourceData.getMockOrdersResponse());

    final var response =
        mockMvc
            .perform(get("/ecommerce/orders").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    assertThat(response).isNotNull();
  }

  @Test
  void shouldFetchCustomerById() throws Exception {

    when(ecommerceService.getCustomer(any(Long.class)))
        .thenReturn(MockResourceData.getMockCustomers().get(1));

    final var response =
        mockMvc
            .perform(
                get("/ecommerce/customer/{customerId}", 2).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    assertThat(response).isNotNull();
  }

  @Test
  void shouldThrowCustomerNotFoundExceptionForGetCustomerById() throws Exception {

    when(ecommerceService.getCustomer(any(Long.class))).thenThrow(CustomerNotFoundException.class);

    final var response =
        mockMvc
            .perform(
                get("/ecommerce/customer/{customerId}", 2).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    assertThat(response).isNotNull();
  }

  @Test
  void shouldFetchAllCustomersInfo() throws Exception {

    when(ecommerceService.fetchAllCustomersInfo(anyInt(), anyInt(), anyString(), anyString()))
        .thenReturn(MockResourceData.getCustomersInfo());
    final var response =
        mockMvc
            .perform(get("/ecommerce/customersInfo").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    assertThat(response).isNotNull();
  }

  @Test
  void shouldUpdateAddressForCustomer() throws Exception {

    doNothing()
        .when(ecommerceService)
        .updateCustomerAddress(anyLong(), any(AddressRepresentation.class));

    final var response =
        mockMvc
            .perform(
                put("/ecommerce/customer/address/{customerId}", 100L)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(MockResourceData.getAddressRequestJson()))
            .andExpect(status().isNoContent());
    assertThat(response).isNotNull();
  }
}
