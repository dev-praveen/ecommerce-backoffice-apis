package com.praveen.jpa.entity;

import com.praveen.jpa.model.*;
import lombok.*;
import jakarta.persistence.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CUSTOMERS")
@ToString(exclude = {"orders", "address"})
public class Customer implements Serializable {

  @Serial private static final long serialVersionUID = 904830748979595077L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EID_GENERATOR_SEQUENCE")
  @SequenceGenerator(
      name = "EID_GENERATOR_SEQUENCE",
      sequenceName = "EID_GENERATOR_SEQUENCE",
      initialValue = 165850,
      allocationSize = 1)
  private Long id;

  @Column(name = "FIRST_NAME", nullable = false)
  private String firstName;

  @Column(name = "LAST_NAME")
  private String lastName;

  @Column(name = "EMAIL")
  private String email;

  @Column(name = "CONTACT_NUMBER", nullable = false)
  private String contactNumber;

  @OneToOne(mappedBy = "customer", orphanRemoval = true, cascade = CascadeType.ALL)
  private Address address;

  @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Order> orders = new ArrayList<>();

  public static Customer fromModel(CreateCustomerRequest customerRepresentation) {

    final Customer customer = new Customer();
    customer.setEmail(customerRepresentation.getEmail());
    customer.setFirstName(customerRepresentation.getFirstName());
    customer.setLastName(customerRepresentation.getLastName());
    customer.setAddress(Address.fromModel(customerRepresentation.getAddress()));
    customer.setContactNumber(customerRepresentation.getContactNumber());
    customer.setOrders(Collections.emptyList());
    return customer;
  }

  public static Customer updateModel(Customer customer, CustomerUpdateInfo customerUpdateInfo) {

    if (StringUtils.isNotEmpty(customerUpdateInfo.getEmail())) {
      customer.setEmail(customerUpdateInfo.getEmail());
    }
    if (StringUtils.isNotEmpty(customerUpdateInfo.getFirstName())) {
      customer.setFirstName(customerUpdateInfo.getFirstName());
    }
    if (StringUtils.isNotEmpty(customerUpdateInfo.getLastName())) {
      customer.setLastName(customerUpdateInfo.getLastName());
    }
    if (StringUtils.isNotEmpty(customerUpdateInfo.getContactNumber())) {
      customer.setContactNumber(customerUpdateInfo.getContactNumber());
    }
    return customer;
  }

  public static CustomerInfoData fromCustomerInfoPage(Page<CustomerInfo> customerInfoPage) {

    return CustomerInfoData.builder()
        .customers(customerInfoPage.getContent())
        .totalElements(customerInfoPage.getTotalElements())
        .totalPages(customerInfoPage.getTotalPages())
        .currentPage(customerInfoPage.getNumber() + 1)
        .isFirst(customerInfoPage.isFirst())
        .isLast(customerInfoPage.isLast())
        .hasNext(customerInfoPage.hasNext())
        .hasPrevious(customerInfoPage.hasPrevious())
        .build();
  }

  public static CustomerResponse fromPageCustomer(Page<Customer> customerPage) {

    return CustomerResponse.builder()
        .customers(customerPage.getContent().stream().map(Customer::toModel).toList())
        .totalElements(customerPage.getTotalElements())
        .totalPages(customerPage.getTotalPages())
        .currentPage(customerPage.getNumber() + 1)
        .isFirst(customerPage.isFirst())
        .isLast(customerPage.isLast())
        .hasNext(customerPage.hasNext())
        .hasPrevious(customerPage.hasPrevious())
        .build();
  }

  public void setAddress(Address address) {
    address.setCustomer(this);
    this.address = address;
  }

  public void setOrders(List<Order> orders) {
    if (null != orders) {
      orders.forEach(order -> order.setCustomer(this));
      this.getOrders().addAll(orders);
    }
  }

  public CustomerRepresentation toModel() {

    return CustomerRepresentation.builder()
        .id(id)
        .firstName(firstName)
        .lastName(lastName)
        .email(email)
        .contactNumber(contactNumber)
        .address(address.toModel())
        .orders(orders.stream().map(Order::toModel).toList())
        .build();
  }
}
