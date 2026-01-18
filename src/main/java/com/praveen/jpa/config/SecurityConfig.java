package com.praveen.jpa.config;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.praveen.jpa.service.AppUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtProperties jwtProperties;
  private final AppUserDetailsService appUserDetailsService;

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) {

    return http.userDetailsService(appUserDetailsService)
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers(
                        "/swagger-ui.html",
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/swagger-resources/**",
                        "/webjars/**",
                        "/auth/token",
                        "/user/register")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .csrf(AbstractHttpConfigurer::disable)
        .exceptionHandling(
            ex -> {
              ex.authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint());
              ex.accessDeniedHandler(new BearerTokenAccessDeniedHandler());
            })
        .httpBasic(Customizer.withDefaults())
        .oauth2ResourceServer(c -> c.jwt(Customizer.withDefaults()))
        .build();
  }

  @Bean
  JwtDecoder jwtDecoder() {
    return NimbusJwtDecoder.withPublicKey(jwtProperties.getPublicKey()).build();
  }

  @Bean
  JwtEncoder jwtEncoder() {

    JWK jwk =
        new RSAKey.Builder(jwtProperties.getPublicKey())
            .privateKey(jwtProperties.getPrivateKey())
            .build();
    JWKSource<SecurityContext> jwkSources = new ImmutableJWKSet<>(new JWKSet(jwk));
    return new NimbusJwtEncoder(jwkSources);
  }

  @Bean
  AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) {
    return authConfig.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
