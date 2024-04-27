package com.praveen.jpa.dao;

import com.praveen.jpa.entity.Customer;
import com.praveen.jpa.model.CustomerInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

  @Query(
      """
      select count(c) > 0 from Customer c where c.firstName = :firstName and c.email = :email and c.contactNumber = :contactNumber
      """)
  boolean existsBy(String firstName, String email, String contactNumber);

  @Query(
      """
         select new com.praveen.jpa.model.CustomerInfo(c.id, c.firstName, c.lastName, c.email, c.contactNumber) from Customer c
      """)
  Page<CustomerInfo> fetchAllCustomersInfo(Pageable pageable);

  @Query(
      """
          select new com.praveen.jpa.model.CustomerInfo(c.id, c.firstName, c.lastName, c.email, c.contactNumber)
          from Customer c
          where lower(concat(c.firstName, ' ', c.lastName)) like lower(concat(:searchName, '%'))
             or lower(c.firstName) like lower(concat('%', :searchName, '%'))
             or lower(c.lastName) like lower(concat('%', :searchName, '%'))
      """)
  List<CustomerInfo> searchByName(String searchName);
}
