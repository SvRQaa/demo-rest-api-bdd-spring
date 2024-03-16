package tse.api.demo.runners;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"pretty", "json:target/cucumber-report/cucumber.json", "html:target/cucumber-report/cucumber.html"},
        features = "src/test/resources/tse/api/demo/feature/",
        glue = "tse.api.demo.steps",
        tags = "@CoreRegression or @TradeAll", // Trade1 Trade2 TradeAll CoreRegression API
        monochrome = true
)
public class CucumberIT {

}
