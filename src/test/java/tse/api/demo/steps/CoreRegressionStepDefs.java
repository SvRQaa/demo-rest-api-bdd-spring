package tse.api.demo.steps;

import io.cucumber.java.en.When;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import tse.api.demo.dependency.ScenarioScoped;
import tse.api.demo.steps.config.CucumberSpringConfiguration;

public class CoreRegressionStepDefs extends CucumberSpringConfiguration {

    private static final Logger log = LoggerFactory.getLogger(CoreRegressionStepDefs.class);
    @Autowired
    private ScenarioScoped scenarioScoped;

    @When("setOnCall true in CoreRegressionStepDefs")
    public void setOnCallTrueInCoreRegressionStepDefs() {
        log.info("setOnCall True");
        scenarioScoped.setOnCall(true);
    }

}
