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
        return EntityModel.of(pokemonType,
                linkTo(methodOn(PokemonTypeController.class).getPokemonTypeById(pokemonType.get_id())).withSelfRel(),
                linkTo(methodOn(PokemonTypeController.class).getAllPokemonTypes()).withRel("allPokemonTypes"),
                linkTo(methodOn(PokemonTypeController.class).deletePokemonType(pokemonType.get_id())).withRel("deletePokemonType")
        );
    }
}
