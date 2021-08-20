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

//    private Pageable pageable;

    @Override
    public EntityModel<Pokemon> toModel(Pokemon pokemon) {
//        return pageable != null ? new EntityModel<>(pokemon,
//                linkTo(methodOn(PokemonController.class).getPokemonById(pokemon.getId())).withSelfRel(),
//                linkTo(methodOn(PokemonController.class).getAllPokemons(pageable))
//                        .slash(String.format("?page=%d&size=%d", 0, pageable.getPageSize()))
//                        .withRel("allPokemons"),
//                linkTo(methodOn(PokemonController.class).deletePokemon(pokemon.getId())).withRel("delete"))
//                : new EntityModel<>(pokemon,
//                linkTo(methodOn(PokemonController.class).getPokemonById(pokemon.getId())).withSelfRel(),
//                linkTo(methodOn(PokemonController.class).deletePokemon(pokemon.getId())).withRel("delete")
//        );
        return new EntityModel<>(pokemon,
                linkTo(methodOn(PokemonController.class).getPokemonById(pokemon.get_id())).withSelfRel(),
                linkTo(methodOn(PokemonController.class).deletePokemon(pokemon.get_id())).withRel("delete"));
    }

//    public void setPageable(Pageable pageable) {
//        this.pageable = pageable;
//    }
}
