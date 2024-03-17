package com.praveen.jpa.resource;

import com.praveen.jpa.model.CreateCustomerRequest;
import com.praveen.jpa.model.CustomerRepresentation;
import com.praveen.jpa.model.OrderRepresentation;
import com.praveen.jpa.model.OrderRequest;
import com.praveen.jpa.service.EcommerceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ecommerce")
public class EcommerceResource {

  private final EcommerceService ecommerceService;

  @PostMapping(value = "/customer", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Long> createCustomer(
      @RequestBody CreateCustomerRequest customerRequest) {

    final Long customerId = ecommerceService.saveCustomer(customerRequest);
    return new ResponseEntity<>(customerId, HttpStatus.CREATED);
  }

  @PutMapping(value = "/customer/{customerId}", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Void> updateCustomer(
      @PathVariable Long customerId, @RequestBody CreateCustomerRequest customerRequest) {

    ecommerceService.updateCustomer(customerId, customerRequest);
    return new ResponseEntity<>(HttpStatus.ACCEPTED);
  }

  @GetMapping(value = "/customers", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<CustomerRepresentation>> getAllCustomers() {

    final var customers = ecommerceService.findAllCustomers();
    return ResponseEntity.ok(customers);
  }

  @PostMapping(value = "/placeOrder/{customerId}", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Long> placeOrderByCustomerId(
      @PathVariable Long customerId, @RequestBody OrderRequest orderRequest) {

    final Long orderId = ecommerceService.saveOrder(customerId, orderRequest);
    return new ResponseEntity<>(orderId, HttpStatus.CREATED);
  }

  @GetMapping(value = "/orders/{customerId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<OrderRepresentation>> getOrders(@PathVariable Long customerId) {
    final List<OrderRepresentation> allOrders = ecommerceService.findAllOrders(customerId);
    return ResponseEntity.ok(allOrders);
  }

  @DeleteMapping("/customer/{customerId}")
  public ResponseEntity<Void> deleteCustomer(@PathVariable Long customerId) {

    ecommerceService.deleteCustomer(customerId);
    return ResponseEntity.ok().build();
  }

  @GetMapping(value = "/orders", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<OrderRepresentation>> fetchOrders() {

    final List<OrderRepresentation> orders = ecommerceService.fetchAllOrders();
    return ResponseEntity.ok(orders);
  }

  @GetMapping(value = "/customer/{customerId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CustomerRepresentation> getCustomer(@PathVariable Long customerId) {

    final var customer = ecommerceService.getCustomer(customerId);
    return ResponseEntity.ok(customer);
  }
}
