package com.praveen.jpa.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record TokenRequest(
    @NotNull @Schema(name = "userName", requiredMode = Schema.RequiredMode.REQUIRED)
        String userName,
    @NotNull @Schema(name = "password", requiredMode = Schema.RequiredMode.REQUIRED)
        String password,
    @NotNull
        @Schema(name = "scope", requiredMode = Schema.RequiredMode.REQUIRED, defaultValue = "read")
        String scope) {}
