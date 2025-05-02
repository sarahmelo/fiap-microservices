package br.com.fiap.users.bdd.steps;

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
public class UsuarioSteps {

    @LocalServerPort
    private int port;

    private Response response;
    private RequestSpecification request;
    private String token;
    private Long usuarioId;

    @Dado("que eu tenho um usuário cadastrado no sistema com ID {long}")
    public void usuarioCadastradoComId(Long id) {
        baseUrl();
        usuarioId = id;
        
        // Registrar um usuário e obter token para autorização
        String email = "usuario" + id + "@teste.com";
        String password = "senha123";
        
        // Cadastrar o usuário
        given()
            .contentType("application/json")
            .body("{ \"email\": \"" + email + "\", \"password\": \"" + password + "\", \"name\": \"Usuário Teste " + id + "\" }")
        .when()
            .post("/auth/register");
            
        // Login para obter token
        Response loginResponse = given()
            .contentType("application/json")
            .body("{ \"email\": \"" + email + "\", \"password\": \"" + password + "\" }")
        .when()
            .post("/auth/login");
            
        token = loginResponse.jsonPath().getString("token");
    }

    @Dado("que não existe um usuário com ID {long}")
    public void naoExisteUsuarioComId(Long id) {
        baseUrl();
        usuarioId = id;
        
        // Obter token para autorização (usando um usuário existente)
        String email = "admin@teste.com";
        String password = "admin123";
        
        // Cadastrar o usuário admin
        given()
            .contentType("application/json")
            .body("{ \"email\": \"" + email + "\", \"password\": \"" + password + "\", \"name\": \"Admin Teste\" }")
        .when()
            .post("/auth/register");
            
        // Login para obter token
        Response loginResponse = given()
            .contentType("application/json")
            .body("{ \"email\": \"" + email + "\", \"password\": \"" + password + "\" }")
        .when()
            .post("/auth/login");
            
        token = loginResponse.jsonPath().getString("token");
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
