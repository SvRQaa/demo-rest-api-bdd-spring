package tse.api.demo.assembler;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import tse.api.demo.controller.SecurityController;
import tse.api.demo.model.Security;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class SecurityModelAssembler implements RepresentationModelAssembler<Security, EntityModel<Security>> {

    @Override
    public EntityModel<Security> toModel(Security security) {

        return EntityModel.of(security,
                linkTo(methodOn(SecurityController.class).one(security.getId())).withSelfRel(),
                linkTo(methodOn(SecurityController.class).all()).withRel("securities"));
    }
}
