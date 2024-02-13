package tse.api.demo.model;

import lombok.Data;

@Data
public class LastSavedModel {
    User user;
    Security security;
    Order sellOrder;
    Order buyOrder;
    Trade trade;
}
