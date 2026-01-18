package com.praveen.jpa.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record UserDetailsRequest(
    @NotNull @Schema(name = "userName", requiredMode = Schema.RequiredMode.REQUIRED)
        String userName,
    @NotNull
        @Schema(
            name = "password",
            requiredMode = Schema.RequiredMode.REQUIRED,
            type = "string",
            format = "password",
            accessMode = Schema.AccessMode.WRITE_ONLY)
        String password) {}
