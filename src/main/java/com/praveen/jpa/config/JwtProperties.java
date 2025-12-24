package com.praveen.jpa.config;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Data
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

  @NotNull private RSAPublicKey publicKey;
  @NotNull private RSAPrivateKey privateKey;
}
