package tse.api.demo.dependency;

import io.cucumber.spring.ScenarioScope;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;

@Component
@ScenarioScope
@Scope(SCOPE_CUCUMBER_GLUE)
@Getter @Setter
public class ScenarioScoped {

    private boolean onInit = false;
    private boolean onCall = false;

    public ScenarioScoped() {
        setOnInit(true);
    }

    public String getStatus() {
        return String.format("onInit: %s, onCall: %s", isOnInit(), isOnCall());
    }
}
