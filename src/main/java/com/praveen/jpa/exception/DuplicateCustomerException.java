package com.praveen.jpa.exception;

public class DuplicateCustomerException extends RuntimeException {

  public DuplicateCustomerException(String message) {
    super(message);
  }
}
