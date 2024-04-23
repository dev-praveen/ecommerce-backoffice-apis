package com.praveen.jpa.service;

import com.praveen.jpa.dao.AddressRepository;
import com.praveen.jpa.dao.CustomerRepository;
import com.praveen.jpa.dao.OrderRepository;
import com.praveen.jpa.entity.Address;
import com.praveen.jpa.entity.Customer;
import com.praveen.jpa.entity.Order;
import com.praveen.jpa.exception.CancelOrderException;
import com.praveen.jpa.exception.CustomerNotFoundException;
import com.praveen.jpa.exception.DuplicateCustomerException;
import com.praveen.jpa.exception.OrderNotFoundException;
import com.praveen.jpa.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EcommerceService {

  private static final String CANCEL_ORDER_STATUS = "cancelled";
  private final OrderRepository orderRepository;
  private final CustomerRepository customerRepository;
  private final AddressRepository addressRepository;

  @Transactional
  public Long saveCustomer(CreateCustomerRequest customerRepresentation) {

    if (isCustomerAlreadyExists(customerRepresentation)) {
      throw new DuplicateCustomerException(
          "Found another customer with same details in database, please try with different first name, email, contact number");
    }
    final Customer customer = customerRepository.save(Customer.fromModel(customerRepresentation));
    return customer.getId();
  }

  @Transactional
  public void updateCustomer(Long customerId, CustomerUpdateInfo customerUpdateInfo) {

    final var customer = findCustomer(customerId);
    customerRepository.save(Customer.updateModel(customer, customerUpdateInfo));
  }

  public CustomerResponse fetchAllCustomers(
      Integer pageNo, Integer pageSize, String sortBy, String sortDirection) {

    Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
    Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
    final var customerPage = customerRepository.findAll(pageable);
    return Customer.fromPageCustomer(customerPage);
  }

  @Transactional
  public Long saveOrder(Long customerId, OrderRequest orderRequest) {

    final var customer = findCustomer(customerId);
    final var order = Order.fromModel(orderRequest, customer);
    return orderRepository.save(order).getId();
  }

  public List<OrderRepresentation> fetchAllOrders(Long customerId) {

    final var customer = findCustomer(customerId);
    final var orders = orderRepository.findByCustomerId(customer.getId());
    return orders.stream().map(Order::toModel).toList();
  }

  public CustomerRepresentation fetchCustomer(Long customerId) {
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

  public OrderResponse fetchAllOrders(
      String orderStatus, Integer pageNo, Integer pageSize, String sortBy, String sortDirection) {

    Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
    Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
    final var pageOrders = orderRepository.findAllByStatus(pageable, orderStatus);
    return Order.fromPageOrder(pageOrders);
  }

  private boolean isCustomerAlreadyExists(CreateCustomerRequest customerRequest) {

    return customerRepository.existsBy(
        customerRequest.getFirstName(),
        customerRequest.getEmail(),
        customerRequest.getContactNumber());
  }

  @Transactional
  public void updateCustomerAddress(Long customerId, AddressRepresentation addressRequest) {

    final var customer = findCustomer(customerId);
    final var address = Address.updateModel(customer, addressRequest);
    addressRepository.save(address);
  }

  public CustomerInfoData fetchAllCustomersInfo(
      Integer pageNo, Integer pageSize, String sortBy, String sortDirection) {

    Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
    Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
    final var customerInfoPage = customerRepository.fetchAllCustomersInfo(pageable);

    return Customer.fromCustomerInfoPage(customerInfoPage);
  }

  @Transactional
  public void cancelOrder(Long customerId, Long orderId) {

    final var customer = findCustomer(customerId);

    customer.getOrders().stream()
        .filter(order -> Objects.equals(order.getId(), orderId))
        .findFirst()
        .ifPresentOrElse(
            order -> updateOrder(orderId, order),
            () -> {
              throw new OrderNotFoundException(
                  "Order not found in database with id " + orderId + " for customer " + customerId);
            });
  }

  private void updateOrder(Long orderId, Order order) {

    if (CANCEL_ORDER_STATUS.equals(order.getStatus())) {
      throw new CancelOrderException(
          "order " + orderId + " is already cancelled, please check the order again");
    }
    orderRepository.updateStatus(CANCEL_ORDER_STATUS, orderId, LocalDateTime.now());
  }
}
