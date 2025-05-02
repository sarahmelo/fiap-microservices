package br.com.fiap.users.bdd.steps;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Então;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import java.util.List;
import java.util.Map;

public class UsuarioSteps {

    @LocalServerPort
    private int port;

    private Response response;
    private RequestSpecification request;
    private String token;
    private Long usuarioId;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Dado("que eu tenho um usuário cadastrado no sistema com ID {long}")
    public void usuarioCadastradoComId(Long id) {
        baseUrl();
        usuarioId = id;
        
        // Preparar dados do usuário
        String email = "usuario" + id + "@teste.com";
        String password = "senha123";
        
        try {
            // Verificar se o usuário já existe
            List<Map<String, Object>> users = jdbcTemplate.queryForList(
                "SELECT * FROM tbl_user WHERE id = ?", id);
            
            if (users.isEmpty()) {
                // Inserir usuário diretamente no banco
                jdbcTemplate.update(
                    "INSERT INTO tbl_user (id, email, name, password, role) VALUES (?, ?, ?, ?, ?)",
                    id, email, "Usuário Teste " + id, passwordEncoder.encode(password), "USER"
                );
                
                System.out.println("Usuário de teste criado com ID=" + id);
            } else {
                System.out.println("Usuário com ID=" + id + " já existe");
                // Atualizar o usuário
                jdbcTemplate.update(
                    "UPDATE tbl_user SET email = ?, name = ?, password = ? WHERE id = ?",
                    email, "Usuário Teste " + id, passwordEncoder.encode(password), id
                );
            }
            
            // Simular o login para obter token
            RestAssured.baseURI = "http://localhost:" + port;
            Response loginResponse = given()
                .contentType("application/json")
                .body("{ \"email\": \"" + email + "\", \"password\": \"" + password + "\" }")
            .when()
                .post("/auth/login");
                
            token = loginResponse.jsonPath().getString("token");
        } catch (Exception e) {
            System.err.println("Erro ao criar usuário de teste: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Dado("que não existe um usuário com ID {long}")
    public void naoExisteUsuarioComId(Long id) {
        baseUrl();
        usuarioId = id;
        
        try {
            // Verificar se o usuário existe e remover se necessário
            List<Map<String, Object>> users = jdbcTemplate.queryForList(
                "SELECT * FROM tbl_user WHERE id = ?", id);
            
            if (!users.isEmpty()) {
                // Remover o usuário se existir
                jdbcTemplate.update("DELETE FROM tbl_user WHERE id = ?", id);
                System.out.println("Usuário removido: ID=" + id);
            } else {
                System.out.println("Usuário já não existe: ID=" + id);
            }
            
            // Inserir o admin para ter autenticação nos testes
            String adminEmail = "admin@teste.com";
            String adminPassword = "admin123";
            
            List<Map<String, Object>> adminUsers = jdbcTemplate.queryForList(
                "SELECT * FROM tbl_user WHERE email = ?", adminEmail);
                
            if (adminUsers.isEmpty()) {
                jdbcTemplate.update(
                    "INSERT INTO tbl_user (id, email, name, password, role) VALUES (?, ?, ?, ?, ?)",
                    999L, adminEmail, "Admin Teste", passwordEncoder.encode(adminPassword), "ADMIN"
                );
                System.out.println("Admin criado para testes");
            }
            
            // Simular o login para obter token
            RestAssured.baseURI = "http://localhost:" + port;
            Response loginResponse = given()
                .contentType("application/json")
                .body("{ \"email\": \"" + adminEmail + "\", \"password\": \"" + adminPassword + "\" }")
            .when()
                .post("/auth/login");
                
            token = loginResponse.jsonPath().getString("token");
        } catch (Exception e) {
            System.err.println("Erro ao verificar usuário: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Quando("eu faço uma requisição GET para {string}")
    public void requisicaoGET(String endpoint) {
        request = given()
            .contentType("application/json")
            .header("Authorization", "Bearer " + token);

        response = request.when().get(endpoint);
    }
    
    @Quando("eu faço uma requisição PUT para {string} com novos dados")
    public void requisicaoPUT(String endpoint) {
        request = given()
            .contentType("application/json")
            .header("Authorization", "Bearer " + token)
            .body("{ \"name\": \"Nome Atualizado\", \"password\": \"novasenha123\" }");

        response = request.when().put(endpoint);
    }

    @Então("a resposta deve conter os dados do usuário com ID {long}")
    public void verificarDadosUsuarioComId(Long id) {
        response.then()
            .body("id", equalTo(id.intValue()))
            .body("name", notNullValue())
            .body("email", notNullValue());
    }

    @Então("a resposta deve conter uma mensagem de erro")
    public void verificarMensagemErro() {
        response.then()
            .body("$", hasKey("message"));
    }

    @Então("a resposta deve conter os dados atualizados do usuário")
    public void verificarDadosAtualizados() {
        response.then()
            .body("name", equalTo("Nome Atualizado"));
    }
    
    private void baseUrl() {
        RestAssured.baseURI = "http://localhost:" + port;
    }
}
