package com.praveen.jpa.entity;

import com.praveen.jpa.model.AddressRepresentation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Data
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Address {

  @Column(name = "HOUSE_NO")
  private String houseNo;

  @Column(name = "STREET")
  private String street;

  @Column(name = "LANDMARK")
  private String landmark;

  @Column(name = "PIN_CODE")
  private String pinCode;

  @Column(name = "CITY")
  private String city;

  public static Address fromModel(AddressRepresentation addressRepresentation) {

    return Address.builder()
        .street(addressRepresentation.getStreet())
        .pinCode(addressRepresentation.getPinCode())
        .landmark(addressRepresentation.getLandmark())
        .city(addressRepresentation.getCity())
        .houseNo(addressRepresentation.getHouseNo())
        .build();
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
