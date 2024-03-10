package com.praveen.jpa.entity;

import com.praveen.jpa.model.CreateCustomerRequest;
import com.praveen.jpa.model.CustomerRepresentation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CUSTOMER")
public class Customer implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EID_GENERATOR_SEQUENCE")
  @SequenceGenerator(
      name = "EID_GENERATOR_SEQUENCE",
      sequenceName = "EID_GENERATOR_SEQUENCE",
      initialValue = 165850,
      allocationSize = 1)
  private Integer id;

  @Column(name = "FIRST_NAME")
  private String firstName;

  @Column(name = "LAST_NAME")
  private String lastName;

  @Column(name = "EMAIL")
  private String email;

  @Column(name = "CONTACT_NUMBER")
  private Long contactNumber;

  @Embedded private Address address;

  @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<Order> orders = new ArrayList<>();

  public void setOrders(List<Order> orders) {
    orders.forEach(order -> order.setCustomer(this));
    this.orders = orders;
  }

  public static Customer fromModel(CreateCustomerRequest customerRepresentation) {

    return Customer.builder()
        .email(customerRepresentation.getEmail())
        .firstName(customerRepresentation.getFirstName())
        .lastName(customerRepresentation.getLastName())
        .address(Address.fromModel(customerRepresentation.getAddress()))
        .contactNumber(customerRepresentation.getContactNumber())
        .build();
  }

  public CustomerRepresentation toModel() {

    return CustomerRepresentation.builder()
        .id(id)
        .firstName(firstName)
        .lastName(lastName)
        .email(email)
        .contactNumber(contactNumber)
        .address(address.toModel())
        .orders(orders.stream().map(Order::toModel).collect(Collectors.toList()))
        .build();
  }
}
