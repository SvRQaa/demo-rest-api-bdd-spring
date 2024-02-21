package tse.api.demo.steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tse.api.demo.steps.config.CucumberSpringConfiguration;

public class StepHooks {

    @Getter
    private static final Logger log = LoggerFactory.getLogger(CucumberSpringConfiguration.class);

//    private final PicoContainer picoContainer; ??? //todo dependency injection to steps file for global assertAll()

    @Before
    public void setUp() {
        log.info("rest steps global before test hook, i will manage precondition as alternative to Background");
    }

//    @After
//    public void tearDown() {
//        log.info("rest steps global after test hook, i will cleanup after test");
//    }
}
