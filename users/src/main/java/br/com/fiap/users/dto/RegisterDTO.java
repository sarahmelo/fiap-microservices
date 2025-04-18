package br.com.fiap.users.dto;

import br.com.fiap.users.model.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterDTO(
        @NotBlank(message = "Name is mandatory")
        String name,

        @NotBlank(message = "Email is mandatory")
        String email,

        @NotBlank(message = "Password is mandatory")
        String password,

        @NotNull(message = "Role is mandatory")
        UserRole role
) {
}
