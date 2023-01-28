package com.praveen.jpa.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequest {

  private String productName;
  private Integer quantity;
  private Float amount;
}
