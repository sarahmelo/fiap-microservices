package br.com.fiap.aspersor.bdd;

import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/resources/features",
    glue = {"br.com.fiap.aspersor.bdd.steps"},
    plugin = {"pretty", "html:target/cucumber-reports"}
)
public class CucumberRunnerTest {
}
