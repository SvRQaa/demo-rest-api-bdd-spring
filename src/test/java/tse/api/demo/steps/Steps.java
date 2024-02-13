package tse.api.demo.steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.Getter;
import org.assertj.core.api.SoftAssertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import tse.api.demo.helpers.OrderHelper;
import tse.api.demo.model.Order;
import tse.api.demo.model.Trade;
import tse.api.demo.service.v1.ExchangeServiceTestHelper;
import tse.api.demo.steps.config.CucumberSpringConfiguration;
import tse.api.demo.steps.config.TestConfig;

import static tse.api.demo.utils.constants.ExCon.ORDER_TYPE_BUY;
import static tse.api.demo.utils.constants.ExCon.ORDER_TYPE_SELL;

@SpringBootTest(classes = TestConfig.class)
@ComponentScan(basePackages = {"tse.api.demo"})
public class Steps extends CucumberSpringConfiguration {

    private static final Logger log = LoggerFactory.getLogger(RestSteps.class);
    @Autowired
    ExchangeServiceTestHelper exchangeServiceTestHelper; //v1 implementation
    @Autowired
    OrderHelper orderHelper;
    @Getter
    private SoftAssertions softAssertions = new SoftAssertions();

    @Before
    public void setUp() {
        log.info("steps global before test hook, i will manage precondition as alternative to Background");
    }

    /**
     * will throw all non-critical errors after all asserts
     */
    @After
    public void tearDown() {
        log.info("steps global after test hook, i will cleanup after test");
        softAssertions.assertAll();
    }

    @Given("one security {string} and two users {string} and {string} exist")
    public void oneSecurityAndTwoUsersAndExist(String secName1, String user1, String user2) {
        exchangeServiceTestHelper.addUser(user1);
        exchangeServiceTestHelper.addUser(user2);
        exchangeServiceTestHelper.addSecurity(secName1);
    }

    @When("user {string} puts a buy order for security {string} with a price of {int} and quantity of {int}")
    public void userPutsABuyOrderForSecurityWithAPriceOfAndQuantityOf(
            String buyUser, String buySec, int buyPrice, int buyQuantity) {

        exchangeServiceTestHelper.addOrder(orderHelper.formOrder(
                exchangeServiceTestHelper.findUserByUsername(buyUser),
                exchangeServiceTestHelper.findSecurityByName(buySec),
                buyPrice,
                buyQuantity,
                ORDER_TYPE_BUY
        ));
    }

    @And("user {string} puts a sell order for security {string} with a price of {int} and a quantity of {int}")
    public void userPutsASellOrderForSecurityWithAPriceOfAndAQuantityOf(
            String sellUser, String sellSec, int sellPrice, int sellQuantity) {

        exchangeServiceTestHelper.addOrder(orderHelper.formOrder(
                exchangeServiceTestHelper.findUserByUsername(sellUser),
                exchangeServiceTestHelper.findSecurityByName(sellSec),
                sellPrice,
                sellQuantity,
                ORDER_TYPE_SELL
        ));
    }

    @Then("a trade occurs with the price of {int} and quantity of {int}")
    public void aTradeOccursWithThePriceOfAndQuantityOf(int price, int quantity) {
        exchangeServiceTestHelper.makeATrade(price, quantity);
    }

    @Given("regenerate context")
    public void restartContext() {
        exchangeServiceTestHelper.getContext().regenerate();
    }

    @And("after a trade buy order still open")
    public void buyOrderStillOpen() {
        Order buyOrder = exchangeServiceTestHelper.getContext().getLatestModel().getBuyOrder();
        Trade trade = exchangeServiceTestHelper.findTradeByBuyOrder(buyOrder);
        exchangeServiceTestHelper.getSoftAssertions().assertThat(trade.getBuyOrder()).isEqualTo(buyOrder);
        exchangeServiceTestHelper.getSoftAssertions().assertThat(buyOrder.isFulfilled()).isFalse();
        exchangeServiceTestHelper.getSoftAssertions().assertThat(buyOrder.getQuantity()).isGreaterThan(0);
    }
}
