package com.praveen.jpa.entity;

import com.praveen.jpa.model.OrderRepresentation;
import com.praveen.jpa.model.OrderRequest;
import lombok.*;
import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ORDERS")
public class Order implements Serializable {

  @Serial private static final long serialVersionUID = -6373223943264489431L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ORDER_ID_GENERATOR_SEQUENCE")
  @SequenceGenerator(
      name = "ORDER_ID_GENERATOR_SEQUENCE",
      sequenceName = "ORDER_ID_GENERATOR_SEQUENCE",
      initialValue = 1145550,
      allocationSize = 1)
  @Column(name = "ID")
  private Long id;

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

    final Order order = new Order();
    order.setProductName(orderRequest.getProductName());
    order.setQuantity(orderRequest.getQuantity());
    order.setAmount(orderRequest.getAmount());
    order.setOrderTime(LocalDateTime.now());
    order.setCustomer(customerRep);

    return order;
  }

  public OrderRepresentation toModel() {

    return OrderRepresentation.builder()
        .orderId(id)
        .productName(productName)
        .quantity(quantity)
        .amount(amount)
        .orderTime(orderTime)
        .customerId(customer.getId())
        .build();
  }

  @Override
  public String toString() {
    return "Order{"
        + "id="
        + id
        + ", productName='"
        + productName
        + '\''
        + ", quantity="
        + quantity
        + ", amount="
        + amount
        + ", orderTime="
        + orderTime
        + '}';
  }
}
