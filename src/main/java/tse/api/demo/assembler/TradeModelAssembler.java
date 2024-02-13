package tse.api.demo.assembler;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import tse.api.demo.controller.UserController;
import tse.api.demo.model.Trade;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TradeModelAssembler implements RepresentationModelAssembler<Trade, EntityModel<Trade>> {

    @Override
    public EntityModel<Trade> toModel(Trade trade) {

        return EntityModel.of(trade,
                linkTo(methodOn(UserController.class).one(trade.getId())).withSelfRel(),
                linkTo(methodOn(UserController.class).all()).withRel("trades"));
    }
}
