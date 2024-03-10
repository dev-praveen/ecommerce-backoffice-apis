package com.praveen.jpa.entity;

import com.praveen.jpa.model.CreateCustomerRequest;
import com.praveen.jpa.model.CustomerRepresentation;
import lombok.*;
import jakarta.persistence.*;
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

  @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Order> orders = new ArrayList<>();

  public static Customer fromModel(CreateCustomerRequest customerRepresentation) {

    final Customer customer = new Customer();
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
    }
    this.orders = orders;
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
