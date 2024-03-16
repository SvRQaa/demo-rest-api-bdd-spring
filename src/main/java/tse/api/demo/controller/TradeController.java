package tse.api.demo.controller;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tse.api.demo.assembler.TradeModelAssembler;
import tse.api.demo.exception.TradeNotFoundException;
import tse.api.demo.model.Order;
import tse.api.demo.model.Security;
import tse.api.demo.model.Trade;
import tse.api.demo.repository.OrderRepository;
import tse.api.demo.repository.SecurityRepository;
import tse.api.demo.repository.TradeRepository;
import tse.api.demo.repository.UserRepository;
import tse.api.demo.utils.TestContext;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static tse.api.demo.utils.constants.ExCon.ORDER_TYPE_BUY;
import static tse.api.demo.utils.constants.ExCon.ORDER_TYPE_SELL;

@RestController
public class TradeController {

    private final TradeRepository repository;
    private final TradeModelAssembler assembler;
    private final UserRepository userRepository;
    private final SecurityRepository securityRepository;
    private final OrderRepository orderRepository;
    @Getter
    @Setter
    private TestContext context;

    private static final Logger log = LoggerFactory.getLogger(TradeController.class);

    @Autowired
    TradeController(TradeRepository repository, TradeModelAssembler assembler, UserRepository userRepository, SecurityRepository securityRepository, OrderRepository orderRepository, TestContext context) {
        this.repository = repository;
        this.assembler = assembler;
        this.userRepository = userRepository;
        this.securityRepository = securityRepository;
        this.orderRepository = orderRepository;
        this.context = context;
    }

    @GetMapping("/trades/{id}")
    public EntityModel<Trade> one(@PathVariable Long id) {

        Trade trade = repository.findById(id)
                .orElseThrow(() -> new TradeNotFoundException(id));

        return assembler.toModel(trade);
    }

    @GetMapping("/trades")
    public CollectionModel<EntityModel<Trade>> all() {

        List<EntityModel<Trade>> Trade = repository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(Trade, linkTo(methodOn(TradeController.class).all()).withSelfRel());
    }

    @PostMapping("/trades")
    ResponseEntity<?> newTrade(@RequestBody Trade newTrade) {

        EntityModel<Trade> entityModel = assembler.toModel(repository.saveAndFlush(newTrade));

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @PutMapping("/trades/{id}")
    ResponseEntity<?> replaceTrade(@RequestBody Trade newTrade, @PathVariable Long id) {

        Trade updatedTrade = repository.findById(id)
                .map(trade -> {
                    trade.setBuyOrder(newTrade.getBuyOrder());
                    trade.setSellOrder(newTrade.getSellOrder());
                    trade.setPrice(newTrade.getPrice());
                    trade.setQuantity(newTrade.getQuantity());
                    return repository.saveAndFlush(trade);
                })
                .orElseGet(() -> {
                    newTrade.setId(id);
                    return repository.saveAndFlush(newTrade);
                });

        EntityModel<Trade> entityModel = assembler.toModel(updatedTrade);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @DeleteMapping("/trades/{id}")
    ResponseEntity<?> deleteTrade(@PathVariable Long id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/trades/job")
    public ResponseEntity<?> makeATrade() {
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

                    orderRepository.saveAndFlush(sellOrder);
                    log.info("Save sellOrder: " + sellOrder);
                    orderRepository.saveAndFlush(buyOrder);
                    log.info("Save buyOrder: " + buyOrder);

                    EntityModel<Trade> entityModel = assembler.toModel(repository.saveAndFlush(trade));
                    Trade createdTrade = entityModel.getContent();
                    context.getLatestModel().setTrade(createdTrade);

                    return ResponseEntity
                            .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                            .body(entityModel);
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
        return ResponseEntity.notFound().build();
    }
}