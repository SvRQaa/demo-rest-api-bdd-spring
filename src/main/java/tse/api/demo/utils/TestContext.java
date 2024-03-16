package tse.api.demo.utils;

import lombok.Data;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import tse.api.demo.model.LastSavedModel;

/**
 * latestModel is for faster access to just created or previously created Entities
 * salt is used to make unique Users and Securities by adding random chars at the end of their names
 * it will improve per test isolation
 * regenerate() will force new salt generation.
 * Usage: trigger it in Background hook of the feature for better visibility
 */
@Component
@Data
public class TestContext {
    private LastSavedModel latestModel = new LastSavedModel();
    private String salt = "not set yet";
    private String baseUrl = "http://localhost:8080";

    private static final Logger log = LoggerFactory.getLogger(TestContext.class);

    public void regenerate() {
        regenerateSalt();
    }

    public void regenerateForRest() {
        regenerateSalt();
        latestModel = new LastSavedModel();
    }

    private void regenerateSalt() {
        log.info("on regenerate SALT: " + getSalt());
        setSalt(String.format("_%s", RandomStringUtils.randomAlphabetic(5)));
        log.info("after regenerate SALT: " + getSalt());
    }
}
