package com.praveen.jpa.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class CustomExceptionHandler {

  @ExceptionHandler(CustomerNotFoundException.class)
  public final ProblemDetail handleCustomerNotFoundException(CustomerNotFoundException e) {

    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
    problemDetail.setTitle("Customer not found");
    problemDetail.setProperty("timestamp", LocalDateTime.now());
    return problemDetail;
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ProblemDetail handleValidationExceptions(MethodArgumentNotValidException ex) {

    final var errors =
        ex.getBindingResult().getAllErrors().stream()
            .collect(
                Collectors.toMap(
                    error -> ((FieldError) error).getField(), error -> error.getDefaultMessage()));
    ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
    problemDetail.setTitle("Request payload is invalid");
    problemDetail.setProperty("timestamp", LocalDateTime.now());
    problemDetail.setProperty("errors", errors);
    return problemDetail;
  }
}
