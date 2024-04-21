package com.praveen.jpa.exception;

public class CancelOrderException extends RuntimeException {

  public CancelOrderException(String message) {
    super(message);
  }
}
