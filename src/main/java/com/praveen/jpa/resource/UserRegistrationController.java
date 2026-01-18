package com.praveen.jpa.resource;

import com.praveen.jpa.dto.UserDetailsRequest;
import com.praveen.jpa.service.RegistrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "user-resource")
public class UserRegistrationController {

  private static final Logger logger = LoggerFactory.getLogger(UserRegistrationController.class);
  private final RegistrationService registrationService;

  @Operation(
      operationId = "register",
      summary = "Registers a new user",
      description = "Registers a new user with the provided username and password.",
      tags = {"user-resource"},
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Created",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = String.class))
            })
      })
  @SecurityRequirements
  @PostMapping("/register")
  public ResponseEntity<String> register(
      @Parameter(name = "UserDetailsRequest", required = true) @Valid @RequestBody
          UserDetailsRequest userDetailsRequest) {
    registrationService.register(userDetailsRequest.userName(), userDetailsRequest.password());
    logger.info("User registered: '{}'", userDetailsRequest.userName());
    return ResponseEntity.ok("User registered");
  }
}
