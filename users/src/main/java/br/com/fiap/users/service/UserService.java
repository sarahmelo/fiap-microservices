package br.com.fiap.users.service;

import br.com.fiap.users.dto.RegisterDTO;
import br.com.fiap.users.dto.UserDTO;
import br.com.fiap.users.handlers.error.UserNotFoundException;
import br.com.fiap.users.model.User;
import br.com.fiap.users.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepository repository;

    public ResponseEntity save(RegisterDTO data) {
        if (repository.findByEmail(data.email()) != null) {
            throw new DataIntegrityViolationException("Email already in use.");
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        User newUser = new User(data.name(), data.email(), encryptedPassword, data.role());

        this.repository.save(newUser);

        return ResponseEntity.ok().build();
    }

    public Page<UserDTO> findAll(Pageable pagination) {
        return repository.findAll(pagination).map(UserDTO::new);
    }

    public UserDTO findById(Long id) {
        Optional<User> user = repository.findById(id);

        return user.map(UserDTO::new)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
    }

    public void delete(Long id) {
        Optional<User> user = repository.findById(id);

        if (user.isPresent()) {
            repository.delete(user.get());
        } else {
            throw new UserNotFoundException("User not found.");
        }
    }

    public UserDTO update(Long id, RegisterDTO registrationUserDTO) {
        Optional<User> optionalUser = repository.findById(id);

        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();
            BeanUtils.copyProperties(registrationUserDTO, existingUser, "id");

            User updatedUser = repository.save(existingUser);
            return new UserDTO(updatedUser);
        } else {
            throw new UserNotFoundException("User not found.");
        }
    }
}
