package tse.api.demo.steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.Getter;
import lombok.Setter;
import org.assertj.core.api.SoftAssertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import tse.api.demo.helpers.OrderHelper;
import tse.api.demo.helpers.RestHelper;
import tse.api.demo.helpers.SecurityHelper;
import tse.api.demo.helpers.UserHelper;
import tse.api.demo.model.Order;
import tse.api.demo.model.Security;
import tse.api.demo.model.Trade;
import tse.api.demo.model.User;
import tse.api.demo.service.v2.ExchangeService;
import tse.api.demo.steps.config.CucumberSpringConfiguration;
import tse.api.demo.steps.config.TestConfig;
import tse.api.demo.utils.TestContext;

@SpringBootTest(classes = TestConfig.class)
@ComponentScan(basePackages = {"tse.api.demo"})
public class RestSteps extends CucumberSpringConfiguration {

    @Getter
    private SoftAssertions softAssertions = new SoftAssertions();
    @Getter
    @Setter
    private TestContext context = new TestContext();
    @Autowired
    UserHelper userHelper;
    @Autowired
    SecurityHelper securityHelper;
    @Autowired
    OrderHelper orderHelper;
    @Autowired
    private ExchangeService service;
    @Autowired
    RestHelper restHelper;
    private ResponseEntity<User> userResponse;
    private ResponseEntity<Security> secResponse;
    private ResponseEntity<Order> orderResponse;
    private ResponseEntity<Trade> tradeResponse;

    @Given("rest one security {string} and two users {string} and {string} exist")
    public void restOneSecurityAndTwoUsersAndExist(String securityName, String username1, String username2) {
        addUser(username1);
        addUser(username2);
        addSecurity(securityName);
    }

    @Given("a user with {string} username exists")
    public void aUserWithUsernameExists(String username) {
        addUser(username);
    }

    @Given("a security with {string} name exists")
    public void aSecurityWithNameExists(String name) {
        addSecurity(name);
    }

    private void addUser(String username) {
        User user = userHelper.formUser(String.format("%s%s", username, context.getSalt()));
        User createdUser = service.createUser(user);
        context.getLatestModel().setUser(createdUser);
    }

    private void addSecurity(String name) {
        Security security = securityHelper.formSecurity(String.format("%s%s", name, context.getSalt()));
        Security createdSecurity = service.createSecurity(security);
        context.getLatestModel().setSecurity(createdSecurity);
    }

    @Then("the latest user response status code should be {int}")
    public void theLatestUserResponseStatusCodeShouldBe(int expectedStatusCode) {
        softAssertions.assertThat(HttpStatusCode.valueOf(expectedStatusCode)).isEqualTo(userResponse.getStatusCode());
    }

    @Then("the latest security response status code should be {int}")
    public void theLatestSecurityResponseStatusCodeShouldBe(int expectedStatusCode) {
        softAssertions.assertThat(HttpStatusCode.valueOf(expectedStatusCode)).isEqualTo(secResponse.getStatusCode());
    }

    @Then("the response should contain user {string} username")
    public void theResponseShouldContainUserDetails(String username) {
        String expectedUsername = String.format("%s%s", username, context.getSalt());
        User user = userResponse.getBody();
        softAssertions.assertThat(user).isNotNull();
        softAssertions.assertThat(user.getUsername()).isEqualTo(expectedUsername);
    }

    @When("the client requests GET users lastCreatedId")
    public void theClientRequestsGetUserById() {
        userResponse = restHelper.executeGetUser(context.getLatestModel().getUser().getId());
    }

    @Given("a user with {string} username exists via rest")
    public void aUserWithUsernameExistsViaRest(String username) {
        User user = userHelper.formUser(String.format("%s%s", username, context.getSalt()));
        userResponse = restHelper.executePostUsers(user);
        context.getLatestModel().setUser(userResponse.getBody());
    }

    @Given("security with {string} name exists")
    public void securityWithStringNameExistsViaRest(String name) {
        addSecurity(name);
    }

    @When("the client requests GET security lastCreatedId")
    public void theClientRequestsGETSecurityLastCreatedId() {
        secResponse = restHelper.executeGetSecurity(context.getLatestModel().getSecurity().getId());
    }

    @And("the response should contain security {string} name")
    public void theResponseShouldContainSecurityName(String name) {
        String expectedUsername = String.format("%s%s", name, context.getSalt());
        Security security = secResponse.getBody();
        softAssertions.assertThat(security).isNotNull();
        softAssertions.assertThat(security.getName()).isEqualTo(expectedUsername);
    }

    @Given("security with {string} name exists via rest")
    public void securityWithNameExistsViaRest(String name) {
        Security security = securityHelper.formSecurity(String.format("%s%s", name, context.getSalt()));
        secResponse = restHelper.executePostSecurities(security);
        context.getLatestModel().setSecurity(secResponse.getBody());
    }
}
