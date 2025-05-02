package br.com.fiap.users.bdd.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * DTO para requisições de usuário nos testes
 */
public class UserRequestDTO {
    private String name;
    private String email;
    private String password;
    private String role;
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    public UserRequestDTO() {
    }
    
    public UserRequestDTO(String name, String email, String password, String role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }
    
    /**
     * Cria um DTO para novo usuário com valores padrão
     */
    public static UserRequestDTO newUser(String name, String email) {
        return new UserRequestDTO(name, email, "senha123", "USER");
    }
    
    /**
     * Cria um DTO para administrador com valores padrão
     */
    public static UserRequestDTO newAdmin(String name, String email) {
        return new UserRequestDTO(name, email, "admin123", "ADMIN");
    }
    
    /**
     * Cria um DTO para atualização de usuário
     */
    public static UserRequestDTO updateUser(String name, String email) {
        return new UserRequestDTO(name, email, "novasenha123", "USER");
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
                "{ \"name\": \"%s\", \"email\": \"%s\", \"password\": \"%s\", \"role\": \"%s\" }",
                name, email, password, role
            );
        }
    }
    
    // Getters e Setters
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
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
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
}
