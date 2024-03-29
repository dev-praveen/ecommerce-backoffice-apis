package com.praveen.jpa.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateCustomerRequest {

  @NotBlank(message = "first name can not be blank or null")
  private String firstName;

  private String lastName;

  @Email(message = "not a valid email format")
  private String email;

  @NotBlank(message = "contact number can not be blank or null")
  private String contactNumber;

  @Valid private AddressRepresentation address;
}
