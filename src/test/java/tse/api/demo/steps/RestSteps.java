package tse.api.demo.steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import tse.api.demo.dependency.ScenarioScoped;
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

import static org.assertj.core.api.Assertions.assertThat;
import static tse.api.demo.utils.constants.ExCon.ORDER_TYPE_BUY;
import static tse.api.demo.utils.constants.ExCon.ORDER_TYPE_SELL;

@SpringBootTest(classes = TestConfig.class)
@ComponentScan(basePackages = {"tse.api.demo"})
public class RestSteps extends CucumberSpringConfiguration {

    @Autowired
    private ScenarioScoped scenarioScoped;
    @Getter
    @Setter
    @Autowired
    private TestContext context;
    @Autowired
    private UserHelper userHelper;
    @Autowired
    private SecurityHelper securityHelper;
    @Autowired
    private OrderHelper orderHelper;
    @Autowired
    private ExchangeService service;
    @Autowired
    private RestHelper restHelper;
    private ResponseEntity<User> userResponse;
    private ResponseEntity<Security> secResponse;
    private ResponseEntity<Order> buyOrderResponse;
    private ResponseEntity<Order> sellOrderResponse;
    private ResponseEntity<Trade> tradeResponse;
    private static final Logger log = LoggerFactory.getLogger(RestSteps.class);

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
        assertThat(HttpStatusCode.valueOf(expectedStatusCode)).isEqualTo(userResponse.getStatusCode());
    }

    @Then("the latest security response status code should be {int}")
    public void theLatestSecurityResponseStatusCodeShouldBe(int expectedStatusCode) {
        assertThat(HttpStatusCode.valueOf(expectedStatusCode)).isEqualTo(secResponse.getStatusCode());
    }

    @Then("the response should contain user {string} username")
    public void theResponseShouldContainUserDetails(String username) {
        String expectedUsername = String.format("%s%s", username, context.getSalt());
        User user = userResponse.getBody();
        assertThat(user).isNotNull();
        assertThat(user.getUsername()).isEqualTo(expectedUsername);
    }

    @When("the client requests GET users lastCreatedId")
    public void theClientRequestsGetUserById() {
        userResponse = restHelper.executeGetUserById(context.getLatestModel().getUser().getId());
    }

    @Given("a user with {string} username exists via rest")
    public void aUserWithUsernameExistsViaRest(String username) {
        User user = userHelper.formUser(String.format("%s%s", username, context.getSalt()));
        userResponse = restHelper.executePostUsers(user);
        context.getLatestModel().setUser(userResponse.getBody());
    }

    @Given("security with {string} name exists")
    public void securityWithStringNameExists(String name) {
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
        assertThat(security).isNotNull();
        assertThat(security.getName()).isEqualTo(expectedUsername);
    }

    @Given("security with {string} name exists via rest")
    public void securityWithNameExistsViaRest(String name) {
        Security security = securityHelper.formSecurity(String.format("%s%s", name, context.getSalt()));
        secResponse = restHelper.executePostSecurities(security);
        context.getLatestModel().setSecurity(secResponse.getBody());
    }

    @Then("check onCall value is true in RestSteps")
    public void checkOnCallValueIsTrueInRestSteps() {
        log.info("assert OnCall isTrue");
        assertThat(scenarioScoped.isOnCall()).isTrue();
    }

    @Then("check onCall value is false in RestSteps")
    public void checkOnCallValueIsFalseInRestSteps() {
        log.info("assert OnCall isFalse");
        assertThat(scenarioScoped.isOnCall()).isFalse();
    }

    @Given("regenerate context for rest")
    public void regenerateContextForRest() {
        context.regenerateForRest();
    }

    @When("user {string} puts a {string} order for security {string} with a price of {int} and a quantity of {int} via rest")
    public void userPutsABuyOrderForSecurityWithAPriceOfAndQuantityOfViaRest(String username, String orderType, String secName, int price, int quantity) {
        String salt = context.getSalt();
        User user = restHelper.executeGetUserByUsername(String.format("%s%s", username, salt)).getBody();
        Security security = restHelper.executeGetSecurityBySecurityName(String.format("%s%s", secName, salt)).getBody();
        String type = orderType.equalsIgnoreCase("buy") ? ORDER_TYPE_BUY : ORDER_TYPE_SELL;
        Order formedOrder = orderHelper.formOrder(user, security, price, quantity, type);

        if ("buy".equalsIgnoreCase(orderType)) {
            buyOrderResponse = restHelper.executePostOrder(formedOrder);
            context.getLatestModel().setBuyOrder(buyOrderResponse.getBody());
        } else if ("sell".equalsIgnoreCase(orderType)) {
            sellOrderResponse = restHelper.executePostOrder(formedOrder);
            context.getLatestModel().setSellOrder(sellOrderResponse.getBody());
        }
    }

    @When("trigger make a trade job")
    public void triggerMakeATradeJob() {
        tradeResponse = restHelper.executePostTradeJob();
        context.getLatestModel().setTrade(tradeResponse.getBody());
    }

    @Then("a trade occurs with the price of {int} and quantity of {int} via rest")
    public void aTradeOccursWithThePriceOfAndQuantityOfViaRest(int price, int quantity) {
        Trade trade = context.getLatestModel().getTrade();

        assertThat(trade.getPrice()).as("expected trade price").isEqualTo(price);
        assertThat(trade.getQuantity()).as("expected trade quantity").isEqualTo(quantity);
    }

    @And("User GET has {string} name")
    public void userGetByNameReturnTheRecord(String username) {
        User existingUser = context.getLatestModel().getUser();
        String formattedUsername = String.format("%s%s", username, context.getSalt());

        userResponse = restHelper.executeGetUserByUsername(formattedUsername);
        User actualUser = userResponse.getBody();

        assertThat(actualUser).isEqualTo(existingUser);
    }

    @And("Security GET has {string} name")
    public void securityGETHasName(String secName) {
        Security existingSecurity = context.getLatestModel().getSecurity();
        String formattedSecName = String.format("%s%s", secName, context.getSalt());

        secResponse = restHelper.executeGetSecurityBySecurityName(formattedSecName);
        Security actualSecurity = secResponse.getBody();

        assertThat(existingSecurity).isEqualTo(actualSecurity);
    }
}
