package br.com.fiap.users.bdd.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * DTO para requisições de autenticação nos testes (login e registro)
 */
public class AuthRequestDTO {
    private String email;
    private String password;
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    public AuthRequestDTO() {
    }
    
    public AuthRequestDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }
    
    /**
     * Cria um DTO para login com credenciais
     */
    public static AuthRequestDTO login(String email, String password) {
        return new AuthRequestDTO(email, password);
    }
    
    /**
     * Converte o DTO para string JSON
     */
    public String toJson() {
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            // Fallback para string formatada manualmente se Jackson falhar
            return String.format(
                "{ \"email\": \"%s\", \"password\": \"%s\" }",
                email, password
            );
        }
    }
    
    // Getters e Setters
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
}
