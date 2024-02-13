package tse.api.demo.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tse.api.demo.assembler.SecurityModelAssembler;
import tse.api.demo.exception.SecurityNotFoundException;
import tse.api.demo.model.Security;
import tse.api.demo.repository.SecurityRepository;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class SecurityController {

    private final SecurityRepository repository;
    private final SecurityModelAssembler assembler;

    SecurityController(SecurityRepository repository, SecurityModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @GetMapping("/securities/{id}")
    public EntityModel<Security> one(@PathVariable Long id) {

        Security security = repository.findById(id)
                .orElseThrow(() -> new SecurityNotFoundException(id));

        return assembler.toModel(security);
    }

    @GetMapping("/securities")
    public CollectionModel<EntityModel<Security>> all() {

        List<EntityModel<Security>> security = repository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(security, linkTo(methodOn(SecurityController.class).all()).withSelfRel());
    }

    @PostMapping("/securities")
    ResponseEntity<?> newSecurity(@RequestBody Security newSecurity) {

        EntityModel<Security> entityModel = assembler.toModel(repository.save(newSecurity));

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @PutMapping("/securities/{id}")
    ResponseEntity<?> replaceSecurity(@RequestBody Security newSecurity, @PathVariable Long id) {

        Security updatedSecurity = repository.findById(id)
                .map(security -> {
                    security.setName(newSecurity.getName());
                    return repository.save(security);
                })
                .orElseGet(() -> {
                    newSecurity.setId(id);
                    return repository.save(newSecurity);
                });

        EntityModel<Security> entityModel = assembler.toModel(updatedSecurity);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @DeleteMapping("/securities/{id}")
    ResponseEntity<?> deleteSecurity(@PathVariable Long id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}