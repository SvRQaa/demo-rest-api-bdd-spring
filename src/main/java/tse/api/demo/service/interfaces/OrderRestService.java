package tse.api.demo.service.interfaces;

import tse.api.demo.model.Order;
import tse.api.demo.model.Security;

public interface OrderRestService {
    Order createOrder(Order order);
    Order getOrderById(Long orderId);
    Order getOrderByType(String orderType);
    Order getOrderBySecurity(Security security);
}
