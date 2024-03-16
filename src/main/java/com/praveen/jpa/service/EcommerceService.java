package com.praveen.jpa.service;

import com.praveen.jpa.dao.CustomerRepository;
import com.praveen.jpa.dao.OrderRepository;
import com.praveen.jpa.entity.Customer;
import com.praveen.jpa.entity.Order;
import com.praveen.jpa.model.CreateCustomerRequest;
import com.praveen.jpa.model.CustomerRepresentation;
import com.praveen.jpa.model.OrderRepresentation;
import com.praveen.jpa.model.OrderRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EcommerceService {

  private final OrderRepository orderRepository;
  private final CustomerRepository customerRepository;

  @Transactional
  public Integer saveCustomer(CreateCustomerRequest customerRepresentation) {

    final Customer customer = customerRepository.save(Customer.fromModel(customerRepresentation));
    return customer.getId();
  }

  @Transactional
  public void updateCustomer(Integer customerId, CreateCustomerRequest customerRepresentation) {

    final var isCustomerExist = customerRepository.existsById(customerId);
    if (!isCustomerExist) {
      throw new NoSuchElementException();
    }
    customerRepository.save(Customer.fromModel(customerRepresentation));
  }

  public List<CustomerRepresentation> findAllCustomers() {

    return customerRepository.findAll().stream().map(Customer::toModel).toList();
  }

  @Transactional
  public Long saveOrder(Integer customerId, OrderRequest orderRequest) {

    final var customer = findCustomer(customerId);
    final var order = Order.fromModel(orderRequest, customer);
    return orderRepository.save(order).getOrderId();
  }

  public List<OrderRepresentation> findAllOrders(Integer customerId) {

    final var customer = findCustomer(customerId);
    final var orders = orderRepository.findByCustomerId(customer.getId());
    return orders.stream().map(Order::toModel).toList();
  }

  public CustomerRepresentation getCustomer(Integer customerId) {
    return findCustomer(customerId).toModel();
  }

  private Customer findCustomer(Integer customerId) {
    final var customerOptional = customerRepository.findById(customerId);
    if (customerOptional.isEmpty()) {
      throw new NoSuchElementException();
    }
    return customerOptional.get();
  }

  @Transactional
  public void deleteCustomer(Integer customerId) {

    final var customer = findCustomer(customerId);
    customerRepository.delete(customer);
  }

  public List<OrderRepresentation> fetchAllOrders() {

    return orderRepository.findAll().stream().map(Order::toModel).toList();
  }
}
