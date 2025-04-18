package br.com.fiap.aspersor.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegistrationSprinklerDTO(
        Long id,

        @NotBlank(message = "Name is mandatory")
        String name,

        @NotBlank(message = "Location is mandatory")
        String location,

        @NotBlank(message = "Status is mandatory")
        String status,

        @NotBlank(message = "Operation mode is mandatory")
        String operationMode,

        @NotNull(message = "User ID is mandatory")
        Long userId
) {
}
