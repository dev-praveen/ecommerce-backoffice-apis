package com.praveen.jpa.resource;

import com.praveen.jpa.dao.CustomerRepository;
import com.praveen.jpa.dao.OrderRepository;
import com.praveen.jpa.entity.Customer;
import com.praveen.jpa.entity.Order;
import com.praveen.jpa.model.CustomerRepresentation;
import com.praveen.jpa.model.OrderRepresentation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ecommerce")
public class EcommerceResource {

  private final CustomerRepository customerRepository;
  private final OrderRepository orderRepository;

  @PostMapping(value = "/create-customer", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Void> createCustomer(
      @RequestBody CustomerRepresentation customerRepresentation) {

    customerRepository.save(Customer.fromModel(customerRepresentation));
    return ResponseEntity.ok().build();
  }

  @GetMapping(value = "/customers", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<CustomerRepresentation>> getAllCustomers() {

    final var customers =
        customerRepository.findAll().stream().map(Customer::toModel).collect(Collectors.toList());
    return ResponseEntity.ok(customers);
  }

  @PostMapping(value = "/place-order/{customerId}", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Void> placeOrderByCustomerId(
      @PathVariable Integer customerId, @RequestBody OrderRepresentation orderRepresentation) {

    final var customerOptional = customerRepository.findById(customerId);
    customerOptional.ifPresentOrElse(
        customer -> {
          final var customerRepresentation = customer.toModel();
          final var order = Order.fromModel(orderRepresentation, customerRepresentation);
          orderRepository.save(order);
        },
        customerOptional::orElseThrow);
    return ResponseEntity.ok().build();
  }
}
