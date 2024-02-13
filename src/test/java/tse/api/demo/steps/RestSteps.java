package tse.api.demo.steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.Getter;
import lombok.Setter;
import org.assertj.core.api.SoftAssertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import tse.api.demo.helpers.OrderHelper;
import tse.api.demo.helpers.SecurityHelper;
import tse.api.demo.helpers.UserHelper;
import tse.api.demo.model.Security;
import tse.api.demo.model.User;
import tse.api.demo.service.v1.ExchangeServiceTestHelper;
import tse.api.demo.service.v2.ExchangeService;
import tse.api.demo.steps.config.TestConfig;
import tse.api.demo.utils.TestContext;

import java.util.Collections;

@SpringBootTest(classes = TestConfig.class)
@ComponentScan(basePackages = {"tse.api.demo"})
public class RestSteps {

    private static final Logger log = LoggerFactory.getLogger(RestSteps.class);

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
    private ExchangeServiceTestHelper service2;
    @Autowired
    RestTemplate restTemplate;
    private ResponseEntity<User> userResponse;

    @Before
    public void setUp() {
        log.info("rest steps global before test hook, i will manage precondition as alternative to Background");
    }

    /**
     * will throw all non-critical errors after all asserts
     */
    @After
    public void tearDown() {
        log.info("rest steps global after test hook, i will cleanup after test");
        softAssertions.assertAll();
    }

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
        service2.addUser(username+"blabla");
        context.getLatestModel().setUser(createdUser);
    }

    private void addSecurity(String name) {
        Security security = securityHelper.formSecurity(String.format("%s%s", name, context.getSalt()));
        Security createdSecurity = service.createSecurity(security);
        context.getLatestModel().setSecurity(createdSecurity);
    }

    @Then("the response status code should be {int}")
    public void theResponseStatusCodeShouldBe(int expectedStatusCode) {
        softAssertions.assertThat(HttpStatusCode.valueOf(expectedStatusCode)).isEqualTo(userResponse.getStatusCode());
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
}
