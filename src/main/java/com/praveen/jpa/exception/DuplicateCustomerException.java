package com.praveen.jpa.exception;

public class DuplicateCustomerException extends RuntimeException {

  public DuplicateCustomerException() {
    super();
  }

  public DuplicateCustomerException(String message) {
    super(message);
  }

  public DuplicateCustomerException(Throwable throwable) {
    super(throwable);
  }

  public DuplicateCustomerException(String message, Throwable throwable) {
    super(message, throwable);
  }
}
