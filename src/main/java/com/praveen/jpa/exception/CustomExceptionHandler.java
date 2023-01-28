package com.praveen.jpa.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(NoSuchElementException.class)
  public final ResponseEntity<ErrorDetails> handleNoSuchElementException(
      NoSuchElementException ex, HttpServletRequest request) {
    String path = request.getRequestURI();
    ErrorDetails errorDetails =
        new ErrorDetails(LocalDateTime.now(), HttpStatus.NOT_FOUND, ex.getMessage(), path);
    return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
  }
}
