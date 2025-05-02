package br.com.fiap.aspersor.bdd;

import br.com.fiap.aspersor.SprinklerApplication;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@CucumberContextConfiguration
@SpringBootTest(
    classes = SprinklerApplication.class, 
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.datasource.driverClassName=org.h2.Driver",
    "spring.datasource.username=sa",
    "spring.datasource.password=password",
    "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
public class CucumberSpringConfiguration {
    // Esta classe serve apenas como configuração para o Cucumber Spring
}
