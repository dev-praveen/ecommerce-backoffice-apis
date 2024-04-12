package com.praveen.jpa.resource;

import com.praveen.jpa.api.EcommerceApi;
import com.praveen.jpa.model.*;
import com.praveen.jpa.service.EcommerceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class EcommerceApiController implements EcommerceApi {

  private final EcommerceService ecommerceService;

  @Override
  public ResponseEntity<Long> createCustomer(CreateCustomerRequest createCustomerRequest) {

    final Long customerId = ecommerceService.saveCustomer(createCustomerRequest);
    return new ResponseEntity<>(customerId, HttpStatus.CREATED);
  }

  @Override
  public ResponseEntity<Void> deleteCustomer(Long customerId) {

    ecommerceService.deleteCustomer(customerId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @Override
  public ResponseEntity<List<OrderRepresentation>> fetchOrders(
      Integer pageNo, Integer pageSize, String sortBy, String sortDirection) {

    int pageNumber = pageNo < 1 ? 0 : pageNo - 1;
    final List<OrderRepresentation> orders =
        ecommerceService.fetchAllOrders(pageNumber, pageSize, sortBy, sortDirection);
    return ResponseEntity.ok(orders);
  }

  @Override
  public ResponseEntity<List<CustomerRepresentation>> getAllCustomers(
      Integer pageNo, Integer pageSize, String sortBy, String sortDirection) {

    int pageNumber = pageNo < 1 ? 0 : pageNo - 1;
    final var customers =
        ecommerceService.findAllCustomers(pageNumber, pageSize, sortBy, sortDirection);
    return ResponseEntity.ok(customers);
  }

  @Override
  public ResponseEntity<CustomerInfoData> getAllCustomersInfo(
      Integer pageNo, Integer pageSize, String sortBy, String sortDirection) {

    int pageNumber = pageNo < 1 ? 0 : pageNo - 1;
    final var customers =
        ecommerceService.fetchAllCustomersInfo(pageNumber, pageSize, sortBy, sortDirection);
    return ResponseEntity.ok(customers);
  }

  @Override
  public ResponseEntity<CustomerRepresentation> getCustomer(Long customerId) {

    final var customer = ecommerceService.getCustomer(customerId);
    return ResponseEntity.ok(customer);
  }

  @Override
  public ResponseEntity<List<OrderRepresentation>> getOrders(Long customerId) {

    final List<OrderRepresentation> allOrders = ecommerceService.findAllOrders(customerId);
    return ResponseEntity.ok(allOrders);
  }

  @Override
  public ResponseEntity<Long> placeOrderByCustomerId(Long customerId, OrderRequest orderRequest) {

    final Long orderId = ecommerceService.saveOrder(customerId, orderRequest);
    return new ResponseEntity<>(orderId, HttpStatus.CREATED);
  }

  @Override
  public ResponseEntity<Void> updateCustomer(
      Long customerId, CreateCustomerRequest createCustomerRequest) {

    ecommerceService.updateCustomer(customerId, createCustomerRequest);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @Override
  public ResponseEntity<Void> updateAddress(
      Long customerId, AddressRepresentation addressRepresentation) {

    ecommerceService.updateCustomerAddress(customerId, addressRepresentation);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
