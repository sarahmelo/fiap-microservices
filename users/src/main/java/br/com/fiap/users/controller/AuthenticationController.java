package br.com.fiap.users.controller;

import br.com.fiap.users.dto.AuthenticationDTO;
import br.com.fiap.users.dto.LoginResponseDTO;
import br.com.fiap.users.dto.RegisterDTO;
import br.com.fiap.users.dto.UserDTO;
import br.com.fiap.users.infra.security.TokenService;
import br.com.fiap.users.model.User;
import br.com.fiap.users.repository.UserRepository;
import br.com.fiap.users.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    UserService service;

    @Autowired
    TokenService tokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterDTO data){
        return service.save(data);
    }
}
