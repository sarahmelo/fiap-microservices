package br.com.fiap.aspersor.bdd;

// Mantendo compatibilidade com JUnit 4 para facilitar transição
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/resources/features",
    glue = {"br.com.fiap.aspersor.bdd.steps", "br.com.fiap.aspersor.bdd"},
    plugin = {"pretty", "html:target/cucumber-reports/html", "json:target/cucumber-reports/json/cucumber.json"}
)
public class CucumberRunnerTest {
}
