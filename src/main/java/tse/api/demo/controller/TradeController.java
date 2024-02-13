package tse.api.demo.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tse.api.demo.assembler.TradeModelAssembler;
import tse.api.demo.exception.TradeNotFoundException;
import tse.api.demo.model.Trade;
import tse.api.demo.repository.TradeRepository;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class TradeController {

    private final TradeRepository repository;
    private final TradeModelAssembler assembler;

    TradeController(TradeRepository repository, TradeModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @GetMapping("/trades/{id}")
    public EntityModel<Trade> one(@PathVariable Long id) {

        Trade trade = repository.findById(id)
                .orElseThrow(() -> new TradeNotFoundException(id));

        return assembler.toModel(trade);
    }

    @GetMapping("/trades")
    public CollectionModel<EntityModel<Trade>> all() {

        List<EntityModel<Trade>> Trade = repository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(Trade, linkTo(methodOn(TradeController.class).all()).withSelfRel());
    }

    @PostMapping("/trades")
    ResponseEntity<?> newTrade(@RequestBody Trade newTrade) {

        EntityModel<Trade> entityModel = assembler.toModel(repository.save(newTrade));

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @PutMapping("/trades/{id}")
    ResponseEntity<?> replaceTrade(@RequestBody Trade newTrade, @PathVariable Long id) {

        Trade updatedTrade = repository.findById(id)
                .map(trade -> {
                    trade.setBuyOrder(newTrade.getBuyOrder());
                    trade.setSellOrder(newTrade.getSellOrder());
                    trade.setPrice(newTrade.getPrice());
                    trade.setQuantity(newTrade.getQuantity());
                    return repository.save(trade);
                })
                .orElseGet(() -> {
                    newTrade.setId(id);
                    return repository.save(newTrade);
                });

        EntityModel<Trade> entityModel = assembler.toModel(updatedTrade);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @DeleteMapping("/trades/{id}")
    ResponseEntity<?> deleteTrade(@PathVariable Long id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}