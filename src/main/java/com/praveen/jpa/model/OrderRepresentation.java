package com.praveen.jpa.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderRepresentation {

  private String productName;
  private Integer quantity;
  private Float amount;
}
