package com.praveen.jpa.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CustomerRepresentation {

  private Long id;
  private String firstName;
  private String lastName;
  private String email;
  private Long contactNumber;
  private AddressRepresentation address;
  private List<OrderRepresentation> orders;
}
