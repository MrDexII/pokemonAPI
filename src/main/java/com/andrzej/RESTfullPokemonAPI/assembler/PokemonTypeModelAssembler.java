package com.andrzej.RESTfullPokemonAPI.assembler;

import com.andrzej.RESTfullPokemonAPI.controller.PokemonTypeController;
import com.andrzej.RESTfullPokemonAPI.model.PokemonType;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PokemonTypeModelAssembler implements RepresentationModelAssembler<PokemonType, EntityModel<PokemonType>> {
    @Override
    public EntityModel<PokemonType> toModel(PokemonType pokemonType) {
        return new EntityModel<>(pokemonType,
                linkTo(methodOn(PokemonTypeController.class).getPokemonTypeById(pokemonType.getId())).withSelfRel(),
                linkTo(methodOn(PokemonTypeController.class).getAllPokemonTypes()).withRel("allPokemonTypes"),
                linkTo(methodOn(PokemonTypeController.class).deletePokemonType(pokemonType.getId())).withRel("deletePokemonType")
        );
    }
}
