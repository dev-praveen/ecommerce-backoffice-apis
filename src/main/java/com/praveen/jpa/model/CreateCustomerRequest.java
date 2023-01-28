package com.praveen.jpa.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateCustomerRequest {

  private String firstName;
  private String lastName;
  private String email;
  private Long contactNumber;
  private AddressRepresentation address;
}
