package tse.api.demo.helpers;

import org.springframework.stereotype.Component;
import tse.api.demo.model.Order;
import tse.api.demo.model.Security;
import tse.api.demo.model.User;

import java.util.List;

import static tse.api.demo.utils.constants.ExCon.ORDER_TYPE_BUY;
import static tse.api.demo.utils.constants.ExCon.ORDER_TYPE_SELL;

@Component
public class OrderHelper {

    public OrderHelper() {}

    public Order formOrder(User user, Security sec, int buyPrice, int buyQuantity, String orderType) {

        List<String> orderTypes = List.of(ORDER_TYPE_BUY, ORDER_TYPE_SELL);

        if (!orderTypes.contains(orderType)) {
            throw new RuntimeException(orderType + " is unknown Order Type.\n Known types are: " + orderTypes);
        }

        return Order.builder()
                .security(sec)
                .user(user)
                .price(buyPrice)
                .quantity(buyQuantity)
                .type(orderType)
                .fulfilled(false)
                .build();
    }
}
