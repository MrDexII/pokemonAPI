package com.andrzej.RESTfullPokemonAPI.assembler;

import com.andrzej.RESTfullPokemonAPI.controller.PokemonController;
import com.andrzej.RESTfullPokemonAPI.model.Pokemon;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PokemonModelAssembler implements RepresentationModelAssembler<Pokemon, EntityModel<Pokemon>> {
    @Override
    public EntityModel<Pokemon> toModel(Pokemon pokemon) {
        return EntityModel.of(pokemon,
                linkTo(methodOn(PokemonController.class).getPokemonById(pokemon.get_id())).withSelfRel(),
                linkTo(methodOn(PokemonController.class).deletePokemon(pokemon.get_id())).withRel("delete"));
    }
}
