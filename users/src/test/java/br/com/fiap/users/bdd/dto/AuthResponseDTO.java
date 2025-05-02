package br.com.fiap.users.bdd.dto;

import io.restassured.response.Response;

/**
 * DTO para respostas de autenticação nos testes (token JWT)
 */
public class AuthResponseDTO {
    private String token;
    
    public AuthResponseDTO() {
    }
    
    public AuthResponseDTO(String token) {
        this.token = token;
    }
    
    /**
     * Cria um DTO a partir da resposta da API de autenticação
     */
    public static AuthResponseDTO fromApiResponse(Response response) {
        AuthResponseDTO dto = new AuthResponseDTO();
        
        try {
            // Extrair o token do JSON de resposta
            String token = response.jsonPath().getString("token");
            dto.setToken(token);
            return dto;
        } catch (Exception e) {
            // Log do erro para diagnóstico
            System.err.println("Erro ao extrair token da resposta: " + e.getMessage());
            System.err.println("Conteúdo da resposta: " + response.getBody().asString());
            throw new RuntimeException("Falha ao extrair token da resposta", e);
        }
    }
    
    /**
     * Verifica se o token está presente e válido
     */
    public boolean hasValidToken() {
        return token != null && !token.isEmpty();
    }
    
    // Getters e Setters
    
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
}
