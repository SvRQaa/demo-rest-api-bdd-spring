package tse.api.demo.runners;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;
import org.junit.runner.RunWith;

import static io.cucumber.junit.platform.engine.Constants.*;

/**
 * tags = "@Smoke"
 * -Dtags parameter will allow to control the amount and the scope of execution
 */
// we have lost mvn verify reporting but we got control over tags parameter
    //todo mvn verify, make sure it produces reports
//@Suite //enables report generation
//@SelectClasspathResource("tse/api/demo")
//@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "tse.api.demo.steps")
//@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty, json:target/cucumber-report/cucumber.json, html:target/cucumber-report/cucumber.html")
//@ConfigurationParameter(key = PLUGIN_PUBLISH_QUIET_PROPERTY_NAME, value = "false")
@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"pretty", "json:target/cucumber-report/cucumber.json", "html:target/cucumber-report/cucumber.html"},
        features = "src/test/resources/tse/api/demo/feature",
        glue = "tse.api.demo.steps",
        tags = "@Smoke",
        monochrome = true
)
public class CucumberIT {

}
