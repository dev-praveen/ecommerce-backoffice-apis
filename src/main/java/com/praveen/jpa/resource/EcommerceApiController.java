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
    return EcommerceApi.super.deleteCustomer(customerId);
  }

  @Override
  public ResponseEntity<List<OrderRepresentation>> fetchOrders() {
    return EcommerceApi.super.fetchOrders();
  }

  @Override
  public ResponseEntity<List<CustomerRepresentation>> getAllCustomers() {
    return EcommerceApi.super.getAllCustomers();
  }

  @Override
  public ResponseEntity<List<CustomerInfo>> getAllCustomersInfo() {
    return EcommerceApi.super.getAllCustomersInfo();
  }

  @Override
  public ResponseEntity<CustomerRepresentation> getCustomer(Long customerId) {
    return EcommerceApi.super.getCustomer(customerId);
  }

  @Override
  public ResponseEntity<List<OrderRepresentation>> getOrders(Long customerId) {
    return EcommerceApi.super.getOrders(customerId);
  }

  @Override
  public ResponseEntity<Long> placeOrderByCustomerId(Long customerId, OrderRequest orderRequest) {
    return EcommerceApi.super.placeOrderByCustomerId(customerId, orderRequest);
  }

  @Override
  public ResponseEntity<Void> updateCustomer(
      Long customerId, CreateCustomerRequest createCustomerRequest) {
    return EcommerceApi.super.updateCustomer(customerId, createCustomerRequest);
  }
}
