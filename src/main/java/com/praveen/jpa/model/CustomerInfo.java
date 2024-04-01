package com.praveen.jpa.model;

public record CustomerInfo(
    Long id, String firstName, String lastName, String email, String contactNumber) {}
