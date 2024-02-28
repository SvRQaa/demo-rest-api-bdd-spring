package tse.api.demo.service.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import tse.api.demo.model.Order;
import tse.api.demo.model.Security;
import tse.api.demo.model.Trade;
import tse.api.demo.model.User;
import tse.api.demo.repository.OrderRepository;
import tse.api.demo.repository.SecurityRepository;
import tse.api.demo.repository.TradeRepository;
import tse.api.demo.repository.UserRepository;
import tse.api.demo.utils.TestContext;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static tse.api.demo.utils.constants.ExCon.ORDER_TYPE_BUY;
import static tse.api.demo.utils.constants.ExCon.ORDER_TYPE_SELL;

@Service
@ComponentScan(basePackages = {"tse.api.demo"})
public class ExchangeServiceTestHelper {
    private static final Logger log = LoggerFactory.getLogger(ExchangeServiceTestHelper.class);
    private final UserRepository userRepository;
    private final SecurityRepository securityRepository;
    private final TradeRepository tradeRepository;
    private final OrderRepository orderRepository;
    private ObjectMapper mapper = new ObjectMapper();
    @Getter
    @Setter
    private TestContext context;

    @Autowired
    public ExchangeServiceTestHelper(UserRepository userRepository, SecurityRepository securityRepository,
                                     TradeRepository tradeRepository, OrderRepository orderRepository,
                                     TestContext context) {
        this.userRepository = userRepository;
        this.securityRepository = securityRepository;
        this.tradeRepository = tradeRepository;
        this.orderRepository = orderRepository;
        this.context = context;
    }

    @Deprecated
    /**
     * create from stored payload/order.json
     */
    public void addOrder() {
        Order order;
        try {
            order = mapper.readValue(
                    new File(Objects.requireNonNull(
                            this.getClass().getClassLoader().getResource("payload/order.json")).getFile()
                    ), Order.class);
        } catch (IOException e) {
            throw new RuntimeException("failed to load default test data, message: " + e.getMessage());
        }

        order.setUser(userRepository.findById(order.getUser().getId()).get());
        order.setSecurity(securityRepository.findById(order.getSecurity().getId()).get());
    }

    public void addOrder(Order order) {
        log.info("addOrder : " + order);

        Order savedOrder = orderRepository.save(order);
        if (savedOrder.getType().equals(ORDER_TYPE_SELL)) {
            context.getLatestModel().setSellOrder(savedOrder);
        } else if (savedOrder.getType().equals(ORDER_TYPE_BUY)) {
            context.getLatestModel().setBuyOrder(savedOrder);
        }
    }

    public void addUser(String username) {
        String testUsername = String.format("%s%s", username, context.getSalt());
        User user = new User(testUsername);
        log.info("addUser : " + user);
        context.getLatestModel().setUser(userRepository.save(user));
    }

    public void addSecurity(String name) {
        String testName = String.format("%s%s", name, context.getSalt());
        Security security = new Security(testName);
        log.info("addSecurity : " + security);
        context.getLatestModel().setSecurity(securityRepository.save(security));
    }

    @Deprecated
    /**
     * replaced by OrderHelper.formOrder()
     */
    public void addBuyOrder(String buyUser, String buySec, int buyPrice, int buyQuantity) {

        Order buyOrder = Order.builder()
                .security(securityRepository.findByName(buySec))
                .user(userRepository.findByUsername(buyUser))
                .price(buyPrice)
                .quantity(buyQuantity)
                .type(ORDER_TYPE_BUY)
                .fulfilled(false)
                .build();

        log.info("addBuyOrder : " + buyOrder);

        context.getLatestModel().setBuyOrder(orderRepository.save(buyOrder));
    }

    @Deprecated
    /**
     * replaced by OrderHelper.formOrder()
     */
    public void addSellOrder(String buyUser, String buySec, int buyPrice, int buyQuantity) {

        Order sellOrder = Order.builder()
                .security(securityRepository.findByName(buySec))
                .user(userRepository.findByUsername(buyUser))
                .price(buyPrice)
                .quantity(buyQuantity)
                .type(ORDER_TYPE_SELL)
                .fulfilled(false)
                .build();

        log.info("addSellOrder : " + sellOrder);

        context.getLatestModel().setSellOrder(orderRepository.save(sellOrder));
    }

    public User findUserByUsername(String username) {
        String testUsername = String.format("%s%s", username, context.getSalt());
        log.info("findUserByUsername : " + testUsername);
        return userRepository.findByUsername(testUsername);
    }

    public Security findSecurityByName(String name) {
        String testName = String.format("%s%s", name, context.getSalt());
        log.info("findSecurityByName : " + testName);
        return securityRepository.findByName(testName);
    }

    public Trade findTradeByBuyOrder(Order buyOrder) {
        log.info("findTradeByBuyOrder : " + buyOrder);
        return tradeRepository.findByBuyOrder(buyOrder);
    }

    public void makeATrade(int price, int quantity) {
        List<Security> securities = securityRepository.findAll();

        for (Security sec : securities) {
            Optional<Order> foundBuyOrder = orderRepository.findAll().stream()
                    .filter(order -> !order.isFulfilled())
                    .filter(order -> Objects.equals(order.getSecurity().getName(), sec.getName()))
                    .filter(order -> order.getType().equals(ORDER_TYPE_BUY))
                    .min(Comparator.comparingDouble(Order::getPrice));

            Optional<Order> foundSellOrder = orderRepository.findAll().stream()
                    .filter(order -> !order.isFulfilled())
                    .filter(order -> Objects.equals(order.getSecurity().getName(), sec.getName()))
                    .filter(order -> order.getType().equals(ORDER_TYPE_SELL))
                    .min(Comparator.comparingDouble(Order::getPrice));

            if (foundBuyOrder.isPresent() && foundSellOrder.isPresent()) {
                Order sellOrder = foundSellOrder.get();
                Order buyOrder = foundBuyOrder.get();

                log.info("Matching orders found sellOrder:" + sellOrder);
                log.info("Matching orders found buyOrder:" + buyOrder);

                double tradePrice = sellOrder.getPrice();
                log.info("tradePrice:" + tradePrice);
                int tradeQuantity = 0;

                if (buyOrder.getPrice() >= sellOrder.getPrice()) {
                    int delta = sellOrder.getQuantity() - buyOrder.getQuantity();

                    if (delta > 0) {
                        buyOrder.setFulfilled(true);
                        sellOrder.setQuantity(delta);

                        tradeQuantity = buyOrder.getQuantity();
                        log.info("tradeQuantity:" + tradeQuantity);
                    }

                    if (delta < 0) {
                        sellOrder.setFulfilled(true);
                        buyOrder.setQuantity(buyOrder.getQuantity() - sellOrder.getQuantity());
                        tradeQuantity = sellOrder.getQuantity();
                        log.info("tradeQuantity:" + tradeQuantity);
                    }

                    if (delta == 0) {
                        sellOrder.setFulfilled(true);
                        buyOrder.setFulfilled(true);

                        tradeQuantity = sellOrder.getQuantity();
                        log.info("tradeQuantity:" + tradeQuantity);
                    }

                    Trade trade = new Trade();

                    trade.setQuantity(tradeQuantity);
                    trade.setPrice(tradePrice);
                    trade.setBuyOrder(buyOrder);
                    trade.setSellOrder(sellOrder);

                    orderRepository.save(sellOrder);
                    log.info("Save sellOrder: " + sellOrder);
                    orderRepository.save(buyOrder);
                    log.info("Save buyOrder: " + buyOrder);

                    Trade savedTrade = tradeRepository.save(trade);
                    context.getLatestModel().setTrade(savedTrade);
                    log.info("Save trade: " + savedTrade);

                    Trade actualTrade = tradeRepository.findBySellOrder(context.getLatestModel().getSellOrder());

                    assertThat(actualTrade.getPrice()).isEqualTo(price);
                    assertThat(actualTrade.getQuantity()).isEqualTo(quantity);
                } else {
                    log.info("no trade, price is not ok\n buy.price: " + buyOrder.getPrice() + "\n" +
                            "sell.price: " + sellOrder.getPrice());
                }
            } else {
                log.info("For Security: " + sec.getName() + " no Orders found\n" +
                        "foundBuyOrder : " + foundBuyOrder + "\n" +
                        "foundSellOrder : " + foundSellOrder);
            }
        }
    }
}
