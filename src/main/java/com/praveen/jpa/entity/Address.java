package com.praveen.jpa.entity;

import com.praveen.jpa.model.AddressRepresentation;
import jakarta.persistence.*;
import lombok.*;
import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ADDRESS")
@ToString(exclude = {"customer"})
public class Address implements Serializable {

  @Serial private static final long serialVersionUID = -6530186249441534714L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AID_GENERATOR_SEQUENCE")
  @SequenceGenerator(
      name = "AID_GENERATOR_SEQUENCE",
      sequenceName = "AID_GENERATOR_SEQUENCE",
      initialValue = 20000,
      allocationSize = 1)
  private Long id;

  @Column(name = "HOUSE_NO")
  private String houseNo;

  @Column(name = "STREET")
  private String street;

  @Column(name = "LANDMARK")
  private String landmark;

  @Column(name = "PIN_CODE", nullable = false)
  private String pinCode;

  @Column(name = "CITY")
  private String city;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "customer_id")
  private Customer customer;

  public static Address fromModel(AddressRepresentation addressRepresentation) {

    final var address = new Address();
    populateAddressData(address, addressRepresentation);
    return address;
  }

  public static Address updateModel(
      Customer customer, AddressRepresentation addressRepresentation) {

    final var address = customer.getAddress();
    populateAddressData(address, addressRepresentation);
    return address;
  }

  private static void populateAddressData(
      Address address, AddressRepresentation addressRepresentation) {

    address.setStreet(addressRepresentation.getStreet());
    address.setPinCode(addressRepresentation.getPinCode());
    address.setLandmark(addressRepresentation.getLandmark());
    address.setCity(addressRepresentation.getCity());
    address.setHouseNo(addressRepresentation.getHouseNo());
  }

  public AddressRepresentation toModel() {

    return AddressRepresentation.builder()
        .street(street)
        .pinCode(pinCode)
        .landmark(landmark)
        .city(city)
        .houseNo(houseNo)
        .build();
  }
}
