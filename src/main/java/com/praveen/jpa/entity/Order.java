package com.praveen.jpa.entity;

import com.praveen.jpa.model.OrderRepresentation;
import com.praveen.jpa.model.OrderRequest;
import lombok.*;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ORDER")
public class Order implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ORDER_ID_GENERATOR_SEQUENCE")
  @SequenceGenerator(
      name = "ORDER_ID_GENERATOR_SEQUENCE",
      sequenceName = "ORDER_ID_GENERATOR_SEQUENCE",
      initialValue = 1145550,
      allocationSize = 1)
  @Column(name = "ORDER_ID", nullable = false, insertable = false, updatable = false)
  private Long orderId;

  @Column(name = "PRODUCT_NAME")
  private String productName;

  @Column(name = "QUANTITY")
  private Integer quantity;

  @Column(name = "AMOUNT")
  private Float amount;

  @Column(name = "ORDER_TIME")
  private LocalDateTime orderTime;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "customer_id")
  private Customer customer;

  public static Order fromModel(OrderRequest orderRequest, Customer customerRep) {

    return Order.builder()
        .productName(orderRequest.getProductName())
        .quantity(orderRequest.getQuantity())
        .amount(orderRequest.getAmount())
        .orderTime(LocalDateTime.now())
        .customer(customerRep)
        .build();
  }

  public OrderRepresentation toModel() {

    return OrderRepresentation.builder()
        .orderId(orderId)
        .productName(productName)
        .quantity(quantity)
        .amount(amount)
        .orderTime(orderTime)
        .customerId(customer.getId())
        .build();
  }
}
