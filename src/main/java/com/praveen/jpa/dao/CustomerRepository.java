package com.praveen.jpa.dao;

import com.praveen.jpa.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

  @Query(
      """
      select count(c) > 0 from Customer c where c.firstName = :firstName AND c.email = :email AND c.contactNumber = :contactNumber
      """)
  boolean existsBy(String firstName, String email, String contactNumber);
}
