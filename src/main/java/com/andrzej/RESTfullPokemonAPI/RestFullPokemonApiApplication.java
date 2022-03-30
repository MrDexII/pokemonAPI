package com.andrzej.RESTfullPokemonAPI;

import com.andrzej.RESTfullPokemonAPI.repositorie.PokemonRepository;
import com.andrzej.RESTfullPokemonAPI.repositorie.PokemonTypeRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories(basePackageClasses = {PokemonRepository.class, PokemonTypeRepository.class})
@SpringBootApplication
public class RestFullPokemonApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestFullPokemonApiApplication.class, args);
    }
}
