package com.praveen.jpa.entity;

import com.praveen.jpa.model.AddressRepresentation;
import com.praveen.jpa.model.CustomerRepresentation;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Entity
@Builder
@ToString
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
  @Column(name = "ID", nullable = false, insertable = false, updatable = false)
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

  public static Customer fromModel(CustomerRepresentation customerRepresentation) {

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
        .build();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    Customer customer = (Customer) o;
    return id != null && Objects.equals(id, customer.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
