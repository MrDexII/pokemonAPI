package com.andrzej.RESTfullPokemonAPI.elasticsearch.repository;

import com.andrzej.RESTfullPokemonAPI.model.Pokemon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface MyElasticsearchRepository extends ElasticsearchRepository<Pokemon, String> {
    Pokemon findByName(String name);

    @Query("""
            {
                "fuzzy": {
                    "name": {
                        "value": "?0",
                        "fuzziness": "AUTO",
                        "max_expansions": 50,
                        "prefix_length": 0,
                        "transpositions": true,
                        "rewrite": "constant_score"
                    }
                }
            }
            """
    )
    Page<Pokemon> findPokemonFuzzy(String name, Pageable pageable);

    @Query("""
            {
                "multi_match": {
                    "query": "?0",
                    "type": "bool_prefix",
                    "fields":[
                        "name",
                        "name._2gram",
                        "name._3gram"
                    ]
                }
            }
            """)
    Page<Pokemon> searchAsYouType(String name, Pageable pageable);
}
