package com.praveen.jpa.entity;

import com.praveen.jpa.event.OrderEvent;
import com.praveen.jpa.model.*;
import lombok.*;
import jakarta.persistence.*;
import org.springframework.data.domain.Page;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ORDERS")
@ToString(exclude = {"customer"})
public class Order implements Serializable {

  @Serial private static final long serialVersionUID = -6373223943264489431L;

  private static final String ACTIVE_STATUS = "active";

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

  @Column(name = "STATUS", nullable = false)
  private String status;

  @Column(name = "cancelled_at")
  private LocalDateTime cancelledAt;

  public static Order fromModel(OrderRequest orderRequest, Customer customerRep) {

    final Order order = new Order();
    order.setProductName(orderRequest.getProductName());
    order.setQuantity(orderRequest.getQuantity());
    order.setAmount(orderRequest.getAmount());
    order.setOrderTime(LocalDateTime.now(ZoneId.of("Asia/Kolkata")));
    order.setCustomer(customerRep);
    order.setStatus(ACTIVE_STATUS);

    return order;
  }

  public static OrderResponse fromPageOrder(Page<Order> pageOrder) {

    return OrderResponse.builder()
        .orders(pageOrder.getContent().stream().map(Order::toModel).toList())
        .totalElements(pageOrder.getTotalElements())
        .totalPages(pageOrder.getTotalPages())
        .currentPage(pageOrder.getNumber() + 1)
        .isFirst(pageOrder.isFirst())
        .isLast(pageOrder.isLast())
        .hasNext(pageOrder.hasNext())
        .hasPrevious(pageOrder.hasPrevious())
        .build();
  }

  public OrderRepresentation toModel() {

    return OrderRepresentation.builder()
        .orderId(id)
        .productName(productName)
        .quantity(quantity)
        .amount(amount)
        .orderTime(orderTime)
        .customerId(customer.getId())
        .status(status)
        .cancelledTimestamp(cancelledAt)
        .build();
  }

  public OrderEvent toOrderEvent() {

    return OrderEvent.builder()
        .orderId(id)
        .productName(productName)
        .quantity(quantity)
        .amount(amount)
        .orderTime(String.valueOf(orderTime))
        .status(status)
        .customerName(customer.getFirstName())
        .build();
  }
}
