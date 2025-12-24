package com.praveen.jpa.resource;

import com.praveen.jpa.dto.TokenRequest;
import com.praveen.jpa.dto.TokenResponse;
import com.praveen.jpa.service.TokenGenerator;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "jwt-resource")
public class AuthController {

  private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
  private final TokenGenerator tokenService;
  private final AuthenticationManager authenticationManager;

  @Operation(
      operationId = "generateToken",
      summary = "Issues a JWT token",
      description = "Generates a JWT token upon successful authentication of user credentials.",
      tags = {"jwt-resource"},
      responses = {
        @ApiResponse(
            responseCode = "201",
            description = "Created",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = TokenResponse.class))
            })
      })
  @PostMapping("/token")
  @SecurityRequirements
  public TokenResponse generateToken(
      @Parameter(name = "TokenRequest", required = true) @Valid @RequestBody
          TokenRequest tokenRequest) {

    logger.info("Token requested for user: '{}'", tokenRequest.userName());
    UsernamePasswordAuthenticationToken authRequest =
        new UsernamePasswordAuthenticationToken(tokenRequest.userName(), tokenRequest.password());
    Authentication authentication = authenticationManager.authenticate(authRequest);
    return tokenService.generateToken(authentication);
  }
}
