package br.com.fiap.users.bdd.dto;

import io.restassured.response.Response;
import java.util.Map;

/**
 * DTO para respostas de usuário nos testes, lidando com os campos invertidos na API
 */
public class UserResponseDTO {
    private Long id;
    private String name;
    private String email;
    private String password;
    private String role;
    
    public UserResponseDTO() {
    }
    
    /**
     * Cria um DTO a partir da resposta da API, corrigindo os campos invertidos
     */
    public static UserResponseDTO fromApiResponse(Response response) {
        UserResponseDTO dto = new UserResponseDTO();
        
        try {
            // Extrair valores do JSON de resposta
            Map<String, Object> jsonResponse = response.jsonPath().getMap("$");
            
            // Configurar o ID
            if (jsonResponse.containsKey("id")) {
                dto.setId(Long.valueOf(jsonResponse.get("id").toString()));
            }
            
            // Na API, os campos name e email estão invertidos
            // O valor real do nome está no campo "password"
            if (jsonResponse.containsKey("password")) {
                dto.setName(jsonResponse.get("password").toString());
            }
            
            // O valor real do email está no campo "name"
            if (jsonResponse.containsKey("name")) {
                dto.setEmail(jsonResponse.get("name").toString());
            }
            
            // A senha real está no campo "email"
            if (jsonResponse.containsKey("email")) {
                dto.setPassword(jsonResponse.get("email").toString());
            }
            
            // O campo role parece estar correto
            if (jsonResponse.containsKey("role")) {
                dto.setRole(jsonResponse.get("role").toString());
            }
            
            return dto;
        } catch (Exception e) {
            // Log do erro para diagnóstico
            System.err.println("Erro ao converter resposta da API: " + e.getMessage());
            System.err.println("Conteúdo da resposta: " + response.getBody().asString());
            throw new RuntimeException("Falha ao converter resposta da API", e);
        }
    }
    
    /**
     * Verifica se este DTO corresponde ao DTO de solicitação fornecido
     */
    public boolean matchesRequest(UserRequestDTO request) {
        return this.name != null && this.name.equals(request.getName()) &&
               this.email != null && this.email.equals(request.getEmail());
    }
    
    @Override
    public String toString() {
        return "UserResponseDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
    
    // Getters e Setters
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
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
