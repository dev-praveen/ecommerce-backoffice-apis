package com.praveen.jpa.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDetails {
  private LocalDateTime timestamp;
  private HttpStatus status;
  private String error;
  private String path;
}
