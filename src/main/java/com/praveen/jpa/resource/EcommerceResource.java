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
  public ResponseEntity<Integer> createCustomer(
      @RequestBody CreateCustomerRequest customerRequest) {

    final Integer customerId = ecommerceService.saveCustomer(customerRequest);
    return new ResponseEntity<>(customerId, HttpStatus.CREATED);
  }

  @PutMapping(value = "/customer/{customerId}", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Void> updateCustomer(
      @PathVariable Integer customerId, @RequestBody CreateCustomerRequest customerRequest) {

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
      @PathVariable Integer customerId, @RequestBody OrderRequest orderRequest) {

    final Long orderId = ecommerceService.saveOrder(customerId, orderRequest);
    return new ResponseEntity<>(orderId, HttpStatus.CREATED);
  }

  @GetMapping(value = "/orders/{customerId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<OrderRepresentation>> getOrders(@PathVariable Integer customerId) {
    final List<OrderRepresentation> allOrders = ecommerceService.findAllOrders(customerId);
    return ResponseEntity.ok(allOrders);
  }

  @DeleteMapping("/customer/{customerId}")
  public ResponseEntity<Void> deleteCustomer(@PathVariable Integer customerId) {

    ecommerceService.deleteCustomer(customerId);
    return ResponseEntity.ok().build();
  }

  @GetMapping(value = "/orders", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<OrderRepresentation>> fetchOrders() {

    final List<OrderRepresentation> orders = ecommerceService.fetchAllOrders();
    return ResponseEntity.ok(orders);
  }

  @GetMapping(value = "/customer/{customerId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CustomerRepresentation> getCustomer(@PathVariable Integer customerId) {

    final var customer = ecommerceService.getCustomer(customerId);
    return ResponseEntity.ok(customer);
  }
}
