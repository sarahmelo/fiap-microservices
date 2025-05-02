package br.com.fiap.aspersor.bdd.steps;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Então;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.datasource.driverClassName=org.h2.Driver",
    "spring.datasource.username=sa",
    "spring.datasource.password=password",
    "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
public class AspersorSteps {

    @LocalServerPort
    private int port;

    private Response response;
    private RequestSpecification request;
    private String token;
    private Long aspersorId;

    @Dado("que eu estou autenticado no sistema")
    public void autenticacaoSistema() {
        baseUrl();
        
        String email = "admin_sprinkler@teste.com";
        String password = "admin123";
        
        // Criar um token JWT válido (versão simplificada para os testes)
        String header = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9";
        String payload = "eyJzdWIiOiJhZG1pbl9zcHJpbmtsZXJAdGVzdGUuY29tIiwibmFtZSI6IkFkbWluIiwicm9sZSI6IkFETUlOIiwiaWF0IjoxNjE5NTU2MzI5LCJleHAiOjI2MTk1NTk5Mjl9";
        String signature = "XvbQCYucn7KVXxd6MJUXIUw4LI6RKpXoFpoXsO8-fJw";
        
        token = header + "." + payload + "." + signature;
        
        // Criar usuário admin no banco se necessário
        criarUsuarioAdminParaTeste();
    }
    
    private void criarUsuarioAdminParaTeste() {
        try {
            org.springframework.jdbc.core.JdbcTemplate jdbcTemplate = new org.springframework.jdbc.core.JdbcTemplate(
                new org.springframework.jdbc.datasource.DriverManagerDataSource(
                    "jdbc:h2:mem:testdb",
                    "sa",
                    "password"
                )
            );
            
            Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM tbl_user WHERE email = 'admin_sprinkler@teste.com'", Integer.class);
                
            if (count == null || count == 0) {
                jdbcTemplate.update(
                    "INSERT INTO tbl_user (id, email, name, password, role) VALUES (?, ?, ?, ?, ?)",
                    2L, "admin_sprinkler@teste.com", "Admin Teste", "$2a$10$X/VPu1YQUzOQOvRusZxN8.XVjlqTzpn/quL9mnU.cFJZ1bGQUQXaG", "ADMIN"
                );
                
                System.out.println("Usuário admin de teste criado");
            }
        } catch (Exception e) {
            System.err.println("Erro ao criar usuário admin de teste: " + e.getMessage());
        }
    }

    @Dado("que existem aspersores cadastrados no sistema")
    public void existemAspersoresCadastrados() {
        baseUrl();
        autenticacaoSistema();
        
        given()
            .contentType("application/json")
            .header("Authorization", "Bearer " + token)
            .body("{ \"name\": \"Aspersor 1\", \"location\": \"Jardim\", \"waterFlow\": 10.5, \"active\": true }")
        .when()
            .post("/api/sprinkler");
            
        given()
            .contentType("application/json")
            .header("Authorization", "Bearer " + token)
            .body("{ \"name\": \"Aspersor 2\", \"location\": \"Horta\", \"waterFlow\": 8.3, \"active\": true }")
        .when()
            .post("/api/sprinkler");
    }
    
    @Dado("que eu tenho um aspersor cadastrado com ID {long}")
    public void aspersorCadastradoComId(Long id) {
        baseUrl();
        autenticacaoSistema();
        aspersorId = id;
        
        given()
            .contentType("application/json")
            .header("Authorization", "Bearer " + token)
            .body("{ \"name\": \"Aspersor " + id + "\", \"location\": \"Local de Teste\", \"waterFlow\": 9.7, \"active\": true }")
        .when()
            .post("/api/sprinkler");
    }

    @Quando("eu faço uma requisição POST para {string} com dados válidos")
    public void requisicaoPOST(String endpoint) {
        long userId = criarUsuarioParaTeste();
        
        request = given()
            .contentType("application/json")
            .header("Authorization", "Bearer " + token)
            .body("{ \"name\": \"Novo Aspersor\", \"location\": \"Gramado\", \"status\": \"INACTIVE\", \"operationMode\": \"AUTOMATIC\", \"userId\": " + userId + " }");

        String requestBody = "{ \"name\": \"Novo Aspersor\", \"location\": \"Gramado\", \"status\": \"INACTIVE\", \"operationMode\": \"AUTOMATIC\", \"userId\": " + userId + " }"; 
        System.out.println("----> registrationSprinkler" + requestBody);
        response = request.when().post(endpoint);
    }
    
    private long criarUsuarioParaTeste() {
        try {
            org.springframework.jdbc.core.JdbcTemplate jdbcTemplate = new org.springframework.jdbc.core.JdbcTemplate(
                new org.springframework.jdbc.datasource.DriverManagerDataSource(
                    "jdbc:h2:mem:testdb",
                    "sa",
                    "password"
                )
            );
            
            Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM tbl_user WHERE id = 1", Integer.class);
                
            if (count == null || count == 0) {
                jdbcTemplate.update(
                    "INSERT INTO tbl_user (id, email, name, password, role) VALUES (?, ?, ?, ?, ?)",
                    1L, "admin@teste.com", "Admin Teste", "$2a$10$X/VPu1YQUzOQOvRusZxN8.XVjlqTzpn/quL9mnU.cFJZ1bGQUQXaG", "USER"
                );
                
                System.out.println("Usuário de teste criado com ID=1");
            } else {
                System.out.println("Usuário com ID=1 já existe");
            }
            
            return 1L;
        } catch (Exception e) {
            System.err.println("Erro ao criar usuário de teste: " + e.getMessage());
            e.printStackTrace();
            
            return 1L;
        }
    }
    
    @Quando("eu faço uma requisição GET para {string}")
    public void requisicaoGET(String endpoint) {
        request = given()
            .contentType("application/json")
            .header("Authorization", "Bearer " + token);

        response = request.when().get(endpoint);
    }
    
    @Quando("eu faço uma requisição DELETE para {string}")
    public void requisicaoDELETE(String endpoint) {
        request = given()
            .contentType("application/json")
            .header("Authorization", "Bearer " + token);

        response = request.when().delete(endpoint);
    }

    @Então("o status da resposta deve ser {int}")
    public void verificarStatusResposta(int statusCode) {
        response.then().statusCode(statusCode);
    }

    @Então("a resposta deve conter os dados do aspersor criado")
    public void verificarDadosAspersorCriado() {
        response.then()
            .body("name", equalTo("Novo Aspersor"))
            .body("location", equalTo("Gramado"));
    }

    @Então("a resposta deve conter uma lista paginada de aspersores")
    public void verificarListaAspersores() {
        response.then()
            .body("content", notNullValue())
            .body("content.size()", greaterThan(0))
            .body("totalElements", greaterThan(0));
    }
    
    @Então("o aspersor deve ser removido do sistema")
    public void verificarAspersorRemovido() {
        Response response = RestAssured.given()
            .contentType("application/json")
            .header("Authorization", "Bearer " + token)
        .when()
            .get("/api/sprinkler/" + aspersorId);
            
        int statusCode = response.getStatusCode();
        org.junit.jupiter.api.Assertions.assertTrue(statusCode == 404 || statusCode == 500,
                "Esperava código 404 ou 500, mas recebeu " + statusCode);
    }
    
    private void baseUrl() {
        RestAssured.baseURI = "http://localhost:" + port;
    }
}
