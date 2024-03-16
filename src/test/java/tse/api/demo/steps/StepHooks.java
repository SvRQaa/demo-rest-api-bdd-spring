package tse.api.demo.steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import tse.api.demo.dependency.ScenarioScoped;

import static org.assertj.core.api.Assertions.assertThat;

public class StepHooks {

    @Getter
    private static final Logger log = LoggerFactory.getLogger(StepHooks.class);

    @Autowired
    private ScenarioScoped scenarioScoped;

    @Before
    public void setUp() {
        log.info("Before step");
        log.info("ScenarioScoped status: " + scenarioScoped.getStatus());
    }

    @After
    public void tearDown() {
        log.info("rest steps global after test hook, i will cleanup after test");
    }

    @Given("access ScenarioScoped in StepHooks and check onInit is true")
    public void accessExtraGlueInBeforeStep() {
        log.info("extraGlue setOnInit TRUE");
        assertThat(scenarioScoped.isOnInit()).isTrue();
    }
}
