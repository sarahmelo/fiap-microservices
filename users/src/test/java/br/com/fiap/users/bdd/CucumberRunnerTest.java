package br.com.fiap.users.bdd;

import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/resources/features",
    glue = {"br.com.fiap.users.bdd.steps", "br.com.fiap.users.bdd.config"},
    plugin = {"pretty", "html:target/cucumber-reports"}
)
public class CucumberRunnerTest {
}
