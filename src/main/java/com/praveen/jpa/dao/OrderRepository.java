package com.praveen.jpa.dao;

import com.praveen.jpa.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

  List<Order> findByCustomerId(Long customerId);

  @Modifying
  @Query(
      """
          UPDATE Order o SET o.status = :newStatus WHERE o.id = :orderId
      """)
  void updateStatus(String newStatus, Long orderId);

  Page<Order> findAllByStatus(Pageable pageable, String status);
}
