package com.praveen.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "APP_USER")
public class AppUser {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "APP_USER_SEQUENCE")
  @SequenceGenerator(
      name = "APP_USER_SEQUENCE",
      sequenceName = "APP_USER_SEQUENCE",
      initialValue = 40000,
      allocationSize = 1)
  private Long id;

  @Column(name = "USER_NAME", nullable = false, unique = true)
  private String username;

  @Column(name = "PASSWORD", nullable = false)
  private String password;

  private String authorities;
}
