package com.praveen.jpa.service;

import com.praveen.jpa.dao.AddressRepository;
import com.praveen.jpa.dao.CustomerRepository;
import com.praveen.jpa.dao.OrderRepository;
import com.praveen.jpa.entity.Address;
import com.praveen.jpa.entity.Customer;
import com.praveen.jpa.entity.Order;
import com.praveen.jpa.exception.CustomerNotFoundException;
import com.praveen.jpa.exception.DuplicateCustomerException;
import com.praveen.jpa.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EcommerceService {

  private final OrderRepository orderRepository;
  private final CustomerRepository customerRepository;
  private final AddressRepository addressRepository;

  @Transactional
  public Long saveCustomer(CreateCustomerRequest customerRepresentation) {

    return isCustomerAlreadyExists(customerRepresentation)
        .filter(customerExists -> Boolean.TRUE)
        .map(
            customerBoolean -> {
              final Customer customer =
                  customerRepository.save(Customer.fromModel(customerRepresentation));
              return customer.getId();
            })
        .orElseThrow(
            () ->
                new DuplicateCustomerException(
                    "Found another customer with same details in database, please try with different first name, email, contact number"));
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

    return customerRepository
        .findById(customerId)
        .orElseThrow(
            () ->
                new CustomerNotFoundException(
                    "Customer not found in database with id " + customerId));
  }

  @Transactional
  public void deleteCustomer(Long customerId) {

    final var customer = findCustomer(customerId);
    customerRepository.delete(customer);
  }

  public List<OrderRepresentation> fetchAllOrders() {

    return orderRepository.findAll().stream().map(Order::toModel).toList();
  }

  private Optional<Boolean> isCustomerAlreadyExists(CreateCustomerRequest customerRequest) {

    final var customerExists =
        customerRepository.existsBy(
            customerRequest.getFirstName(),
            customerRequest.getEmail(),
            customerRequest.getContactNumber());

    return Optional.of(customerExists);
  }

  @Transactional
  public void updateCustomerAddress(Long customerId, AddressRepresentation addressRequest) {

    final var customer = findCustomer(customerId);
    final var address = Address.updateModel(customer, addressRequest);
    addressRepository.save(address);
  }

  public List<CustomerInfo> fetchAllCustomersInfo() {

    return customerRepository.fetchAllCustomersInfo();
  }
}
