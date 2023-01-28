package com.praveen.jpa.resource;

import com.praveen.jpa.dao.CustomerRepository;
import com.praveen.jpa.dao.OrderRepository;
import com.praveen.jpa.entity.Customer;
import com.praveen.jpa.entity.Order;
import com.praveen.jpa.model.CreateCustomerRequest;
import com.praveen.jpa.model.CustomerRepresentation;
import com.praveen.jpa.model.OrderRepresentation;
import com.praveen.jpa.model.OrderRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ecommerce")
public class EcommerceResource {

  private final OrderRepository orderRepository;
  private final CustomerRepository customerRepository;

  @PostMapping(value = "/create-customer", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Void> createCustomer(
      @RequestBody CreateCustomerRequest customerRepresentation) {

    customerRepository.save(Customer.fromModel(customerRepresentation));
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @GetMapping(value = "/customers", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<CustomerRepresentation>> getAllCustomers() {

    final var customers =
        customerRepository.findAll().stream().map(Customer::toModel).collect(Collectors.toList());
    return ResponseEntity.ok(customers);
  }

  @PostMapping(value = "/place-order/{customerId}", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Void> placeOrderByCustomerId(
      @PathVariable Integer customerId, @RequestBody OrderRequest orderRequest) {

    final var customerOptional = customerRepository.findById(customerId);
    customerOptional.ifPresentOrElse(
        customer -> {
          final var order = Order.fromModel(orderRequest, customer);
          orderRepository.save(order);
        },
        customerOptional::orElseThrow);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @GetMapping(value = "/orders/{customerId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<OrderRepresentation>> getOrders(@PathVariable Integer customerId) {
    final var orders = orderRepository.findByCustomerId(customerId);
    final var orderRepresentationList =
        orders.stream().map(Order::toModel).collect(Collectors.toList());
    return ResponseEntity.ok(orderRepresentationList);
  }

  @DeleteMapping("/delete/{customerId}")
  public ResponseEntity<Void> deleteCustomer(@PathVariable Integer customerId) {

    final var customerOptional = customerRepository.findById(customerId);
    customerOptional.ifPresentOrElse(customerRepository::delete, customerOptional::orElseThrow);
    return ResponseEntity.ok().build();
  }

  @GetMapping(value = "/orders", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<OrderRepresentation>> fetchOrders() {

    final var orderRepresentations =
        orderRepository.findAll().stream().map(Order::toModel).collect(Collectors.toList());
    return ResponseEntity.ok(orderRepresentations);
  }
}
