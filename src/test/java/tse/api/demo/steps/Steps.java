package tse.api.demo.steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import tse.api.demo.dependency.ScenarioScoped;
import tse.api.demo.helpers.OrderHelper;
import tse.api.demo.model.Order;
import tse.api.demo.model.Trade;
import tse.api.demo.service.v1.ExchangeServiceTestHelper;
import tse.api.demo.steps.config.CucumberSpringConfiguration;
import tse.api.demo.steps.config.TestConfig;

import static org.assertj.core.api.Assertions.assertThat;
import static tse.api.demo.utils.constants.ExCon.ORDER_TYPE_BUY;
import static tse.api.demo.utils.constants.ExCon.ORDER_TYPE_SELL;

@SpringBootTest(classes = TestConfig.class)
@ComponentScan(basePackages = {"tse.api.demo"})
public class Steps extends CucumberSpringConfiguration {

    @Autowired
    ExchangeServiceTestHelper exchangeServiceTestHelper; //v1 implementation
    @Autowired
    OrderHelper orderHelper;

    private ScenarioScoped scenarioScoped;

    @Autowired
    private Steps(ScenarioScoped scenarioScoped) {
        this.scenarioScoped = scenarioScoped;
    }

    @Given("one security {string} and two users {string} and {string} exist")
    public void oneSecurityAndTwoUsersAndExist(String secName1, String user1, String user2) {
        scenarioScoped.getStatus();
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
        assertThat(trade.getBuyOrder().isFulfilled()).isEqualTo(buyOrder.isFulfilled());
        assertThat(buyOrder.isFulfilled()).isFalse();
        assertThat(buyOrder.getQuantity()).isGreaterThan(0);
    }
}
