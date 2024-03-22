package com.praveen.jpa.model;

import jakarta.validation.constraints.NotBlank;
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

  @NotBlank(message = "pincode can not be blank or null")
  private String pinCode;

  private String city;
}
