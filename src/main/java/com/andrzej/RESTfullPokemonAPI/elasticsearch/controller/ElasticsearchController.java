package com.andrzej.RESTfullPokemonAPI.elasticsearch.controller;

import com.andrzej.RESTfullPokemonAPI.elasticsearch.service.ElasticsearchService;
import com.andrzej.RESTfullPokemonAPI.model.Pokemon;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/elastic")
public class ElasticsearchController {
    private final ElasticsearchService elasticsearchService;

    public ElasticsearchController(ElasticsearchService elasticsearchService) {
        this.elasticsearchService = elasticsearchService;
    }

    @GetMapping("/{pokemonName}")
    public ResponseEntity<PagedModel<EntityModel<Pokemon>>> getPokemonByNameSearchAsYouType(@PathVariable String pokemonName,
                                                                                            Pageable pageable) {
        return elasticsearchService.searchAsYouType(pokemonName, pageable);
    }

}
