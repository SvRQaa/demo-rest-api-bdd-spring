package tse.api.demo.assembler;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import tse.api.demo.controller.UserController;
import tse.api.demo.model.Order;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class OrderModelAssembler implements RepresentationModelAssembler<Order, EntityModel<Order>> {

    @Override
    public EntityModel<Order> toModel(Order order) {

        return EntityModel.of(order,
                linkTo(methodOn(UserController.class).one(order.getId())).withSelfRel(),
                linkTo(methodOn(UserController.class).all()).withRel("orders"));
    }
}
