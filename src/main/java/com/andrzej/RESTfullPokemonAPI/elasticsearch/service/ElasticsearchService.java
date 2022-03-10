package com.andrzej.RESTfullPokemonAPI.elasticsearch.service;

import com.andrzej.RESTfullPokemonAPI.assembler.PokemonModelAssembler;
import com.andrzej.RESTfullPokemonAPI.elasticsearch.repository.MyElasticsearchRepository;
import com.andrzej.RESTfullPokemonAPI.model.Pokemon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ElasticsearchService {
    private final MyElasticsearchRepository myElasticsearchRepository;
    private final PagedResourcesAssembler<Pokemon> pagedResourcesAssembler;
    private final PokemonModelAssembler pokemonModelAssembler;

    public ElasticsearchService(MyElasticsearchRepository myElasticsearchRepository,
                                PagedResourcesAssembler<Pokemon> pagedResourcesAssembler,
                                PokemonModelAssembler pokemonModelAssembler) {
        this.myElasticsearchRepository = myElasticsearchRepository;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.pokemonModelAssembler = pokemonModelAssembler;
    }

    public ResponseEntity<PagedModel<EntityModel<Pokemon>>> searchAsYouType(String pokemonName, Pageable pageable) {
        Page<Pokemon> pokemons = myElasticsearchRepository.searchAsYouType(pokemonName, pageable);
        PagedModel<EntityModel<Pokemon>> entityModels = pagedResourcesAssembler.toModel(pokemons, pokemonModelAssembler);
        return ResponseEntity.ok(entityModels);
    }
}
