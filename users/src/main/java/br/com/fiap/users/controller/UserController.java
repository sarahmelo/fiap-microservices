package br.com.fiap.users.controller;

import br.com.fiap.users.dto.RegisterDTO;
import br.com.fiap.users.dto.UserDTO;
import br.com.fiap.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    UserService service;

    @GetMapping("/user")
    public Page<UserDTO> findAll(@PageableDefault(size = 20, page = 0) Pageable pagination) {
        return service.findAll(pagination);
    }

    @GetMapping("/user/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO findById(@PathVariable Long id) {
        return service.findById(id);
    }


    @PutMapping("/user/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserDTO> update(@PathVariable Long id, @RequestBody RegisterDTO user) {
        try {
            UserDTO updatedUser = service.update(id, user);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/user/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
