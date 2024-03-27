package com.praveen.jpa.entity;

import com.praveen.jpa.model.CreateCustomerRequest;
import com.praveen.jpa.model.CustomerRepresentation;
import lombok.*;
import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CUSTOMERS")
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

  @Embedded private Address address;

  @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Order> orders = new ArrayList<>();

  public static Customer fromModel(CreateCustomerRequest customerRepresentation) {

    final Customer customer = new Customer();
    return populateCustomerRequestData(customer, customerRepresentation);
  }

  public static Customer updateModel(
      Customer customer, CreateCustomerRequest customerRepresentation) {

    return populateCustomerRequestData(customer, customerRepresentation);
  }

  private static Customer populateCustomerRequestData(
      Customer customer, CreateCustomerRequest customerRepresentation) {

    customer.setEmail(customerRepresentation.getEmail());
    customer.setFirstName(customerRepresentation.getFirstName());
    customer.setLastName(customerRepresentation.getLastName());
    customer.setAddress(Address.fromModel(customerRepresentation.getAddress()));
    customer.setContactNumber(customerRepresentation.getContactNumber());
    customer.setOrders(null);
    return customer;
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
