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

  static String customerUpdateJsonRequest() {

    return
    """
          {
              "firstName": "Alice",
              "lastName": "Smith",
              "email": "alice.smith@example.com",
              "contactNumber": "9876543210"
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
                    "pinCode": null,
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

  static CustomerResponse getMockCustomersResponse() {

    return CustomerResponse.builder()
        .customers(getMockCustomers())
        .totalElements(2L)
        .totalPages(1)
        .currentPage(1)
        .isFirst(true)
        .isLast(true)
        .hasNext(false)
        .hasPrevious(false)
        .build();
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

  static OrderResponse getMockOrdersResponse() {

    final var mockOrders = getMockOrders();

    return OrderResponse.builder()
        .orders(mockOrders)
        .totalElements(2L)
        .totalPages(1)
        .currentPage(1)
        .isFirst(true)
        .isLast(true)
        .hasNext(false)
        .hasPrevious(false)
        .build();
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

  static CustomerUpdateInfo getCustomerUpdateInfo() {

    return CustomerUpdateInfo.builder()
        .firstName("praveen")
        .lastName("sana")
        .email("spraveen@email.com")
        .contactNumber("9848022338")
        .build();
  }

  static OrderRequest getOrderRequest() {

    return OrderRequest.builder().amount(122.89f).productName("cooler").quantity(2).build();
  }

  static AddressRepresentation getAddressRequest() {

    return AddressRepresentation.builder()
        .street("kk colony")
        .pinCode("300632")
        .landmark("near temple")
        .city("baroda")
        .houseNo("#19-09/A")
        .build();
  }

  static String getAddressRequestJson() {

    return
    """
            {
                  "houseNo": "456",
                  "street": "Broadway",
                  "landmark": "Next to Mall",
                  "pinCode": "565656",
                  "city": "New City"
            }
           """;
  }

  static CustomerInfoData getCustomersInfo() {

    final var customerInfo1 =
        CustomerInfo.builder()
            .id(100L)
            .contactNumber("820-8942892")
            .firstName("kyle")
            .lastName("lessi")
            .email("kyle.lessi@email.com ")
            .build();

    final var customerInfo2 =
        CustomerInfo.builder()
            .id(120L)
            .contactNumber("576-956559")
            .firstName("neilson")
            .lastName("bore")
            .email("neilson.bore@email.com")
            .build();

    return CustomerInfoData.builder()
        .customers(List.of(customerInfo1, customerInfo2))
        .totalElements(2L)
        .totalPages(1)
        .currentPage(1)
        .isFirst(true)
        .isLast(true)
        .hasNext(false)
        .hasPrevious(false)
        .build();
  }
}
