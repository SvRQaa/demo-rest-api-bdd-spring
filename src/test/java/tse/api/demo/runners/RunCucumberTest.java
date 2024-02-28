package tse.api.demo.runners;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.core.options.Constants.*;

@Suite
@SelectClasspathResource(value = "tse/api/demo/feature/")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "tse.api.demo.steps")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty, json:target/cucumber-report/cucumber.json, html:target/cucumber-report/cucumber.html")
@ConfigurationParameter(key = PLUGIN_PUBLISH_QUIET_PROPERTY_NAME, value = "false")
@ConfigurationParameter(key = FILTER_TAGS_PROPERTY_NAME, value = "@API or @Trade1")
public class RunCucumberTest {
    // ./mvnw verify
}
