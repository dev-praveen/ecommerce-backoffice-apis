package com.praveen.jpa.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateCustomerRequest {

  private String firstName;
  private String lastName;
  private String email;
  private Long contactNumber;
  private AddressRepresentation address;
}
