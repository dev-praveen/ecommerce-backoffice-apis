package com.praveen.jpa.resource;

import com.praveen.jpa.model.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public interface MockResourceData {

  static String customerJsonRequest() {

    return
    """
            {
                "firstName": "Alice",
                "lastName": "Smith",
                "email": "alice.smith@example.com",
                "contactNumber": "9876543210",
                "address": {
                    "houseNo": "456",
                    "street": "Broadway",
                    "landmark": "Next to Mall",
                    "pinCode": "54321",
                    "city": "New City"
                }
            }
            """;
  }

  static String invalidCustomerJsonRequest() {

    return
    """
            {
                "firstName": null,
                "lastName": "Smith",
                "email": "alice",
                "contactNumber": "9876543210",
                "address": {
                    "houseNo": "456",
                    "street": "Broadway",
                    "landmark": "Next to Mall",
                    "pinCode": "",
                    "city": "New City"
                }
            }
            """;
  }

  static String orderJsonRequest() {

    return
    """
            {
              "productName": "tiles",
              "quantity": 200,
              "amount": 35689.00
            }
            """;
  }

  static List<CustomerRepresentation> getMockCustomers() {

    CustomerRepresentation customer1 =
        CustomerRepresentation.builder()
            .id(1L)
            .email("john.deo@email.com")
            .firstName("john")
            .lastName("doe")
            .contactNumber("9491425216")
            .orders(Collections.emptyList())
            .address(
                AddressRepresentation.builder()
                    .city("hong kong")
                    .houseNo("D-22")
                    .landmark("chinese circle")
                    .pinCode("61-89438390")
                    .street("d street")
                    .build())
            .build();

    final List<OrderRepresentation> mockOrders = getMockOrders();
    CustomerRepresentation customer2 =
        CustomerRepresentation.builder()
            .id(2L)
            .email("john.cena@email.com")
            .firstName("john")
            .lastName("cena")
            .contactNumber("578475847")
            .orders(mockOrders)
            .address(
                AddressRepresentation.builder()
                    .city("hong kong")
                    .houseNo("D-22")
                    .landmark("chinese circle")
                    .pinCode("61-89438390")
                    .street("d street")
                    .build())
            .build();

    return List.of(customer1, customer2);
  }

  static List<OrderRepresentation> getMockOrders() {
    OrderRepresentation order1 =
        OrderRepresentation.builder()
            .orderId(1L)
            .orderTime(LocalDateTime.now())
            .customerId(1L)
            .amount(1200.00f)
            .productName("chair")
            .quantity(10)
            .build();

    OrderRepresentation order2 =
        OrderRepresentation.builder()
            .orderId(2L)
            .orderTime(LocalDateTime.now())
            .customerId(1L)
            .amount(190.00f)
            .productName("sofa")
            .quantity(5)
            .build();

    return List.of(order1, order2);
  }

  static CreateCustomerRequest getCustomerRequest() {

    return CreateCustomerRequest.builder()
        .firstName("praveen")
        .lastName("sana")
        .email("spraveen@email.com")
        .contactNumber("9848022338")
        .address(AddressRepresentation.builder().pinCode("600032").build())
        .build();
  }

  static OrderRequest getOrderRequest() {

    return OrderRequest.builder().amount(122.89f).productName("cooler").quantity(2).build();
  }
}
