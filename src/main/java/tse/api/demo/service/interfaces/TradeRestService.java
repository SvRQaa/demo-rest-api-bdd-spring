package tse.api.demo.service.interfaces;

import tse.api.demo.model.Order;
import tse.api.demo.model.Trade;

public interface TradeRestService {
    Trade createTrade(Trade trade);
    Trade getTradeById(Long tradeId);
    Trade getTradeByOrder(Order order);
}
