package com.praveen.jpa.dto;

public record TokenResponse(String token, long expiresIn, String type, String scope) {}
