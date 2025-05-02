package br.com.fiap.users.bdd.steps;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Então;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matchers;
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
public class AutenticacaoSteps {

    @LocalServerPort
    private int port;

    private Response response;
    private RequestSpecification request;

    private String baseUrl;
    private String email;
    private String senha;

    @Dado("que eu tenho um usuário cadastrado com email {string} e senha {string}")
    public void usuarioCadastrado(String email, String senha) {
        baseUrl = "http://localhost:" + port;
        RestAssured.baseURI = baseUrl;
        this.email = email;
        this.senha = senha;

        // Cadastrar o usuário para o teste
        given()
            .contentType("application/json")
            .body("{ \"email\": \"" + email + "\", \"password\": \"" + senha + "\", \"name\": \"Usuário Teste\" }")
        .when()
            .post("/auth/register");
    }

    @Dado("que não existe um usuário com email {string}")
    public void naoExisteUsuario(String email) {
        baseUrl = "http://localhost:" + port;
        RestAssured.baseURI = baseUrl;
        this.email = email;
        this.senha = "senha123";

        // Validar que o usuário não existe
        given()
            .contentType("application/json")
            .body("{ \"email\": \"" + email + "\", \"password\": \"wrongpass\" }")
        .when()
            .post("/auth/login")
        .then()
            .statusCode(401);
    }

    @Quando("eu faço uma requisição POST para {string} com as credenciais corretas")
    public void requisicaoLoginCorreta(String endpoint) {
        request = given()
            .contentType("application/json")
            .body("{ \"email\": \"" + email + "\", \"password\": \"" + senha + "\" }");

        response = request.when().post(endpoint);
    }

    @Quando("eu faço uma requisição POST para {string} com a senha incorreta")
    public void requisicaoLoginIncorreta(String endpoint) {
        request = given()
            .contentType("application/json")
            .body("{ \"email\": \"" + email + "\", \"password\": \"senhaerrada\" }");

        response = request.when().post(endpoint);
    }

    @Quando("eu faço uma requisição POST para {string} com dados válidos")
    public void requisicaoRegistro(String endpoint) {
        request = given()
            .contentType("application/json")
            .body("{ \"email\": \"" + email + "\", \"password\": \"" + senha + "\", \"name\": \"Novo Usuário\" }");

        response = request.when().post(endpoint);
    }

    @Então("o status da resposta deve ser {int}")
    public void verificarStatusResposta(int statusCode) {
        response.then().statusCode(statusCode);
    }

    @Então("a resposta deve conter um token JWT válido")
    public void verificarTokenJWT() {
        response.then()
            .body("token", notNullValue())
            .body("token", matchesPattern("^[A-Za-z0-9-_=]+\\.[A-Za-z0-9-_=]+\\.?[A-Za-z0-9-_.+/=]*$"));
    }

    @Então("a resposta deve conter uma mensagem de erro de autenticação")
    public void verificarMensagemErro() {
        response.then().body("$", hasKey("error"));
    }

    @Então("a resposta deve conter os dados do usuário criado")
    public void verificarDadosUsuario() {
        response.then()
            .body("email", equalTo(email))
            .body("name", notNullValue());
    }
}
