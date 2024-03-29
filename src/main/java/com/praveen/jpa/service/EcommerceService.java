package com.praveen.jpa.service;

import com.praveen.jpa.dao.CustomerRepository;
import com.praveen.jpa.dao.OrderRepository;
import com.praveen.jpa.entity.Customer;
import com.praveen.jpa.entity.Order;
import com.praveen.jpa.exception.CustomerNotFoundException;
import com.praveen.jpa.exception.DuplicateCustomerException;
import com.praveen.jpa.model.CreateCustomerRequest;
import com.praveen.jpa.model.CustomerRepresentation;
import com.praveen.jpa.model.OrderRepresentation;
import com.praveen.jpa.model.OrderRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EcommerceService {

  private final OrderRepository orderRepository;
  private final CustomerRepository customerRepository;

  @Transactional
  public Long saveCustomer(CreateCustomerRequest customerRepresentation) {

    final var isCustomerExist = isCustomerAlreadyExists(customerRepresentation);
    if (isCustomerExist) {
      throw new DuplicateCustomerException(
          "Found another customer with same details in database, please try with different first name, email, contact number");
    }
    final Customer customer = customerRepository.save(Customer.fromModel(customerRepresentation));
    return customer.getId();
  }

  @Transactional
  public void updateCustomer(Long customerId, CreateCustomerRequest customerRepresentation) {

    final var customer = findCustomer(customerId);
    customerRepository.save(Customer.updateModel(customer, customerRepresentation));
  }

  public List<CustomerRepresentation> findAllCustomers() {

    return customerRepository.findAll().stream().map(Customer::toModel).toList();
  }

  @Transactional
  public Long saveOrder(Long customerId, OrderRequest orderRequest) {

    final var customer = findCustomer(customerId);
    final var order = Order.fromModel(orderRequest, customer);
    return orderRepository.save(order).getId();
  }

  public List<OrderRepresentation> findAllOrders(Long customerId) {

    final var customer = findCustomer(customerId);
    final var orders = orderRepository.findByCustomerId(customer.getId());
    return orders.stream().map(Order::toModel).toList();
  }

  public CustomerRepresentation getCustomer(Long customerId) {
    return findCustomer(customerId).toModel();
  }

  private Customer findCustomer(Long customerId) {
    final var customerOptional = customerRepository.findById(customerId);
    if (customerOptional.isEmpty()) {
      throw new CustomerNotFoundException("Customer not found in database with id " + customerId);
    }
    return customerOptional.get();
  }

  @Transactional
  public void deleteCustomer(Long customerId) {

    final var customer = findCustomer(customerId);
    customerRepository.delete(customer);
  }

  public List<OrderRepresentation> fetchAllOrders() {

    return orderRepository.findAll().stream().map(Order::toModel).toList();
  }

  private boolean isCustomerAlreadyExists(CreateCustomerRequest customerRequest) {

    return customerRepository.existsBy(
        customerRequest.getFirstName(),
        customerRequest.getEmail(),
        customerRequest.getContactNumber());
  }
}
