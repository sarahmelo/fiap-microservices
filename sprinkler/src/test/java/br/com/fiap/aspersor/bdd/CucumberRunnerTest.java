package br.com.fiap.aspersor.bdd;

// Mantendo compatibilidade com JUnit 4 para facilitar transição
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/resources/features",
    glue = {"br.com.fiap.aspersor.bdd.steps", "br.com.fiap.aspersor.bdd.config"},
    plugin = {"pretty", "html:target/cucumber-reports", "json:target/cucumber-reports/cucumber.json"}
)
public class CucumberRunnerTest {
}
