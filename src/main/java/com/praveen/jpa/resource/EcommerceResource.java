package com.praveen.jpa.resource;

import com.praveen.jpa.dao.CustomerRepository;
import com.praveen.jpa.entity.Customer;
import com.praveen.jpa.model.CustomerRepresentation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ecommerce")
public class EcommerceResource {

  private final CustomerRepository customerRepository;

  @PostMapping("/create-customer")
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
}
