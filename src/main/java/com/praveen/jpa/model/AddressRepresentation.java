package com.praveen.jpa.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressRepresentation {

  private String houseNo;
  private String street;
  private String landmark;
  private String pinCode;
  private String city;
}
