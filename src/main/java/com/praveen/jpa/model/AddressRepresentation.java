package com.praveen.jpa.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddressRepresentation {

  private String houseNo;
  private String street;
  private String landmark;
  private String pinCode;
  private String city;
}
