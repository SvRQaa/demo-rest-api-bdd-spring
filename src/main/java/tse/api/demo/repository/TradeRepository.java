package tse.api.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import tse.api.demo.model.Order;
import tse.api.demo.model.Trade;

@Component
public interface TradeRepository extends JpaRepository<Trade, Long> {
    Trade findBySellOrder(Order sellOrder);
    Trade findByBuyOrder(Order buyOrder);

}
