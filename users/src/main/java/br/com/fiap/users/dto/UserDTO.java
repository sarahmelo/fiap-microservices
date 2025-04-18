package br.com.fiap.users.dto;

import br.com.fiap.users.model.User;
import br.com.fiap.users.model.UserRole;

public record UserDTO(
        Long id,
        String email,
        String password,
        String name,
        UserRole role
) {
    public UserDTO(User user) {
        this(
                user.getId(),
                user.getPassword(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
    }
}
