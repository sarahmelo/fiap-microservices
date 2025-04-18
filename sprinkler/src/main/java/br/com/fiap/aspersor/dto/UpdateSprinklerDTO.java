package br.com.fiap.aspersor.dto;

import br.com.fiap.aspersor.model.Sprinkler;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateSprinklerDTO(
        Long id,

        @NotBlank(message = "Location is mandatory")
        String location,

        @NotBlank(message = "Status is mandatory")
        String status,

        @NotBlank(message = "Name is mandatory")
        String name,

        @NotBlank(message = "Operation mode is mandatory")
        String operationMode,

        @NotNull(message = "User ID is mandatory")
        Long userId
) {

    public UpdateSprinklerDTO(Sprinkler sprinkler) {
        this(
                sprinkler.getId(),
                sprinkler.getLocation(),
                sprinkler.getStatus(),
                sprinkler.getName(),
                sprinkler.getOperationMode(),
                sprinkler.getUser().getId()
        );
    }
}
