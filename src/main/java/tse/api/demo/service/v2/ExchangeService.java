package tse.api.demo.service.v2;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
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
import tse.api.demo.service.interfaces.OrderRestService;
import tse.api.demo.service.interfaces.SecurityRestService;
import tse.api.demo.service.interfaces.TradeRestService;
import tse.api.demo.service.interfaces.UserRestService;

import static tse.api.demo.utils.constants.ExCon.ORDER_TYPE_BUY;

@Service
@ComponentScan(basePackages = {"tse.api.demo"})
@Getter
public class ExchangeService implements SecurityRestService, UserRestService, OrderRestService, TradeRestService {
    private static final Logger log = LoggerFactory.getLogger(ExchangeService.class);
    private final UserRepository userRepository;
    private final SecurityRepository securityRepository;
    private final TradeRepository tradeRepository;
    private final OrderRepository orderRepository;
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    public ExchangeService(UserRepository userRepository, SecurityRepository securityRepository,
                           TradeRepository tradeRepository, OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.securityRepository = securityRepository;
        this.tradeRepository = tradeRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public User createUser(User user) {
        return userRepository.saveAndFlush(user);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Security createSecurity(Security security) {
        return securityRepository.saveAndFlush(security);
    }

    @Override
    public Security findSecurityById(Long id) {
        return securityRepository.findById(id).orElse(null);
    }

    @Override
    public Security findSecurityByName(String name) {
        return securityRepository.findByName(name);
    }

    @Override
    public Order createOrder(Order order) {
        return orderRepository.saveAndFlush(order);
    }

    @Override
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

    @Override
    public Order getOrderByType(String orderType) {
        return orderRepository.findOrderByType(orderType);
    }

    @Override
    public Order getOrderBySecurity(Security security) {
        return orderRepository.findOrderBySecurity(security);
    }

    @Override
    public Trade createTrade(Trade trade) {
        return tradeRepository.saveAndFlush(trade);
    }

    @Override
    public Trade getTradeById(Long tradeId) {
        return tradeRepository.findById(tradeId).orElse(null);
    }

    @Override
    public Trade getTradeByOrder(Order order) {
        return order.getType().equals(ORDER_TYPE_BUY) ?
                tradeRepository.findByBuyOrder(order) :
                tradeRepository.findBySellOrder(order);
    }
}
