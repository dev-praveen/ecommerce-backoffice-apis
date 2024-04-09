package com.praveen.jpa;

import com.github.javafaker.Faker;
import com.praveen.jpa.dao.CustomerRepository;
import com.praveen.jpa.entity.Address;
import com.praveen.jpa.entity.Customer;
import com.praveen.jpa.entity.Order;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.ZoneId;
import java.util.List;
import java.util.Locale;

@SpringBootApplication
public class EcommerceBackofficeApplication {

  public static void main(String[] args) {
    SpringApplication.run(EcommerceBackofficeApplication.class, args);
  }

  @Bean
  CommandLineRunner runner(CustomerRepository customerRepository) {

    return args -> {
      for (int i = 0; i <= 2000; i++) {

        Faker faker = new Faker(new Locale("in-ID"));

        final var customer1 = new Customer();
        final var address1 = new Address();

        address1.setPinCode(faker.address().zipCode());
        address1.setStreet(faker.address().streetName());
        address1.setCity(faker.address().city());
        address1.setLandmark(faker.address().streetAddress());
        address1.setHouseNo(faker.address().buildingNumber());

        final var order1 = new Order();
        order1.setAmount(Float.valueOf(faker.commerce().price()));
        order1.setOrderTime(
            faker.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        order1.setProductName(faker.commerce().productName());
        order1.setQuantity(faker.number().randomDigit());

        final var firstName = faker.name().firstName();
        final var lastName = faker.name().lastName();
        customer1.setFirstName(firstName);
        customer1.setLastName(lastName);
        customer1.setEmail(firstName + "." + lastName + "@gmail.com");
        customer1.setAddress(address1);
        customer1.setContactNumber(faker.phoneNumber().phoneNumber());
        customer1.setOrders(List.of(order1));

        customerRepository.save(customer1);
      }
    };
  }
}
