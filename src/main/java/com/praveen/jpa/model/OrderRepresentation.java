package com.praveen.jpa.model;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class OrderRepresentation {

  private Long orderId;
  private String productName;
  private Integer quantity;
  private Float amount;
  private LocalDateTime orderTime;
  private Integer customerId;
}
