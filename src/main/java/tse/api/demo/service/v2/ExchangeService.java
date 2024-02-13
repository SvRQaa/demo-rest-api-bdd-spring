package tse.api.demo.service.v2;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.assertj.core.api.SoftAssertions;
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

@Service
@ComponentScan(basePackages = {"tse.api.demo"})
public class ExchangeService {
    private static final Logger log = LoggerFactory.getLogger(ExchangeService.class);
    private final UserRepository userRepository;
    private final SecurityRepository securityRepository;
    private final TradeRepository tradeRepository;
    private final OrderRepository orderRepository;
    private ObjectMapper mapper = new ObjectMapper();
    @Getter
    private SoftAssertions softAssertions = new SoftAssertions();
    @Getter
    @Setter
    private TestContext context;

    @Autowired
    public ExchangeService(UserRepository userRepository, SecurityRepository securityRepository,
                           TradeRepository tradeRepository, OrderRepository orderRepository,
                           TestContext context) {
        this.userRepository = userRepository;
        this.securityRepository = securityRepository;
        this.tradeRepository = tradeRepository;
        this.orderRepository = orderRepository;
        this.context = context;
    }

    public User createUser(User user) {
        User savedUser = userRepository.save(user);
        return savedUser;
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Security createSecurity(Security security) {
        return securityRepository.save(security);
    }

    public Security findSecurityById(Long id) {
        return securityRepository.findById(id).orElse(null);
    }

    public Security findSecurityByName(String name) {
        return securityRepository.findByName(name);
    }

    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    public Trade createTrade(Trade trade) {
        return tradeRepository.save(trade);
    }


}
