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
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import tse.api.demo.helpers.OrderHelper;
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

import java.util.Collections;

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
    RestTemplate restTemplate;
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
        String url = String.format("%s/users/%s", context.getBaseUrl(), context.getLatestModel().getUser().getId());

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        userResponse = restTemplate.getForEntity(url, User.class, entity);
    }

    @Given("a user with {string} username exists via rest")
    public void aUserWithUsernameExistsViaRest(String username) {
        User user = userHelper.formUser(String.format("%s%s", username, context.getSalt()));
        String createUserUrl = "http://localhost:8080" + "/users";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<User> requestEntity = new HttpEntity<>(user, headers);

        userResponse = restTemplate.postForEntity(createUserUrl, requestEntity, User.class);

        context.getLatestModel().setUser(userResponse.getBody());
    }

    @Given("security with {string} name exists")
    public void securityWithStringNameExistsViaRest(String name) {
        addSecurity(name);
    }

    @When("the client requests GET security lastCreatedId")
    public void theClientRequestsGETSecurityLastCreatedId() {
        String url = String.format("%s/securities/%s", context.getBaseUrl(), context.getLatestModel().getSecurity().getId());

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        secResponse = restTemplate.getForEntity(url, Security.class, entity);
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
        String createUserUrl = "http://localhost:8080" + "/securities";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Security> requestEntity = new HttpEntity<>(security, headers);

        secResponse = restTemplate.postForEntity(createUserUrl, requestEntity, Security.class);

        context.getLatestModel().setSecurity(secResponse.getBody());
    }
}
