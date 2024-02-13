package tse.api.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Entity
@Data
@Builder
@AllArgsConstructor
public class Trade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "sell_order_id", unique = false, nullable = true)
    private Order sellOrder;

    @OneToOne
    @JoinColumn(name = "buy_order_id", unique = false, nullable = true)
    private Order buyOrder;

    private double price;
    private int quantity;

    public Trade() {}
}