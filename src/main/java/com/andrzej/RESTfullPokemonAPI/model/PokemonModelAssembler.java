package com.andrzej.RESTfullPokemonAPI.model;

import com.andrzej.RESTfullPokemonAPI.controller.PokemonController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PokemonModelAssembler implements RepresentationModelAssembler<Pokemon, EntityModel<Pokemon>> {

    @Override
    public EntityModel<Pokemon> toModel(Pokemon pokemon) {
        return new EntityModel<>(pokemon,
                linkTo(methodOn(PokemonController.class).getPokemonById(pokemon.getId())).withSelfRel(),
                linkTo(methodOn(PokemonController.class).getAllPokemons()).withRel("allPokemons")
        );
    }
}
