package com.praveen.jpa.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.stream.Collectors;

@RestControllerAdvice
public class CustomExceptionHandler {

  private static final String TIMESTAMP = "timestamp";
  private static final String IST_TIMEZONE = "Asia/Kolkata";

  @ExceptionHandler(CustomerNotFoundException.class)
  public final ProblemDetail handleCustomerNotFoundException(CustomerNotFoundException e) {

    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
    problemDetail.setTitle("Customer not found");
    problemDetail.setProperty(TIMESTAMP, LocalDateTime.now(ZoneId.of(IST_TIMEZONE)));
    return problemDetail;
  }

  @ExceptionHandler(OrderNotFoundException.class)
  public final ProblemDetail handleOrderNotFoundException(OrderNotFoundException e) {

    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
    problemDetail.setTitle("Order not found");
    problemDetail.setProperty(TIMESTAMP, LocalDateTime.now(ZoneId.of(IST_TIMEZONE)));
    return problemDetail;
  }

  @ExceptionHandler(CancelOrderException.class)
  public final ProblemDetail handleCancelOrderException(CancelOrderException e) {

    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.getMessage());
    problemDetail.setTitle("Order cancelled already");
    problemDetail.setProperty(TIMESTAMP, LocalDateTime.now(ZoneId.of(IST_TIMEZONE)));
    return problemDetail;
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public final ProblemDetail handleValidationExceptions(MethodArgumentNotValidException ex) {

    final var errors =
        ex.getBindingResult().getAllErrors().stream()
            .collect(
                Collectors.toMap(
                    error -> ((FieldError) error).getField(), error -> error.getDefaultMessage()));
    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST, "Request payload is invalid, look for errors");
    problemDetail.setTitle("Request payload is invalid");
    problemDetail.setProperty(TIMESTAMP, LocalDateTime.now(ZoneId.of(IST_TIMEZONE)));
    problemDetail.setProperty("errors", errors);
    return problemDetail;
  }

  @ExceptionHandler(DuplicateCustomerException.class)
  public final ProblemDetail handleDuplicateCustomerException(DuplicateCustomerException e) {

    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.getMessage());
    problemDetail.setTitle("Duplicate input request");
    problemDetail.setProperty(TIMESTAMP, LocalDateTime.now(ZoneId.of(IST_TIMEZONE)));
    return problemDetail;
  }

  @ExceptionHandler(RuntimeException.class)
  public final ProblemDetail handleException(RuntimeException e) {

    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    problemDetail.setTitle("Something went wrong, please retry after sometime or contact admin");
    problemDetail.setProperty(TIMESTAMP, LocalDateTime.now(ZoneId.of(IST_TIMEZONE)));
    return problemDetail;
  }
}
