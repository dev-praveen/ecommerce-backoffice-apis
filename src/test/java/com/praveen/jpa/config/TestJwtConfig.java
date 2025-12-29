package com.praveen.jpa.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@TestConfiguration
public class TestJwtConfig {

  @Bean
  public JwtProperties jwtProperties() throws Exception {

    KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
    keyGen.initialize(2048);
    KeyPair keyPair = keyGen.generateKeyPair();
    JwtProperties props = new JwtProperties();
    props.setPublicKey((RSAPublicKey) keyPair.getPublic());
    props.setPrivateKey((RSAPrivateKey) keyPair.getPrivate());
    return props;
  }
}
