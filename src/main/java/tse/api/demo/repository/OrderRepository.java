package tse.api.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import tse.api.demo.model.Order;
import tse.api.demo.model.Security;

@Component
public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findOrderByType(String type);
    Order findOrderBySecurity(Security security);
}