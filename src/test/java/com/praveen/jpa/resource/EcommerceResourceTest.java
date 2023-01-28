package com.praveen.jpa.resource;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.praveen.jpa.dao.CustomerRepository;
import com.praveen.jpa.dao.OrderRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class EcommerceResourceTest {

  private MockMvc mockMvc;

  @Mock private OrderRepository orderRepository;

  @Mock private CustomerRepository customerRepository;

  @InjectMocks private EcommerceResource ecommerceResource;

  @BeforeEach
  public void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(ecommerceResource).build();
  }

  @Test
  void createCustomer_validInput_shouldReturn201() throws Exception {
    when(customerRepository.save(any())).thenReturn(null);
    String requestJson =
        "{\"firstName\":\"John\",\"lastName\":\"Doe\",\"email\":\"johndoe@example.com\",\"phone\":\"123-456-7890\",\"address\":{\"houseNo\":\"123\",\"street\":\"Main St\",\"landmark\":\"Near Park\",\"pinCode\":\"123456\",\"city\":\"Anytown\"}}";
    mockMvc
        .perform(
            post("/ecommerce/create-customer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
        .andExpect(status().isCreated());
  }

  @Test
  void getAllCustomers_shouldReturn200() throws Exception {
    when(customerRepository.findAll()).thenReturn(List.of());
    mockMvc.perform(get("/ecommerce/customers")).andExpect(status().isOk());
  }
}
