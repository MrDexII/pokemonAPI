package com.andrzej.RESTfullPokemonAPI.utils;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PokemonUtils {
    /*
    attack|  defence->
          V
               normal|fire|water|electric|grass|ice|fighting|poison|ground|flying|psychic|bug|rock|ghost|dragon|dark|steel|fairy|
   0  normal  |   1  |  1 |  1  |    1   |  1  | 1 |    1   |   1  |   1  |   1  |   1   | 1 | 0.5|  0  |   1  |  1 | 0.5 |  1  |
   1  fire    |   1  | 0.5| 0.5 |    1   |  2  | 2 |    1   |   1  |   1  |   1  |   1   | 2 | 0.5|  1  |  0.5 |  1 |  2  |  1  |
   2  water   |   1  |  2 | 0.5 |    1   | 0.5 | 1 |    1   |   1  |   2  |   1  |   1   | 1 |  2 |  1  |  0.5 |  1 |  1  |  1  |
   3  electric|   1  |  1 |  2  |   0.5  | 0.5 | 1 |    1   |   1  |   0  |   2  |   1   | 1 |  1 |  1  |  0.5 |  1 |  1  |  1  |
   4  grass   |   1  | 0.5|  2  |    1   | 0.5 | 1 |    1   |  0.5 |   2  |  0.5 |   1   |0.5|  2 |  1  |  0.5 |  1 | 0.5 |  1  |
   5  ice     |   1  | 0.5| 0.5 |    1   |  2  |0.5|    1   |   1  |   2  |   2  |   1   | 1 |  1 |  1  |   2  |  1 | 0.5 |  1  |
   6  fighting|   2  |  1 |  1  |    1   |  1  | 2 |    1   |  0.5 |   1  |  0.5 |  0.5  |0.5|  2 |  0  |   1  |  2 |  2  | 0.5 |
   7  poison  |   1  |  1 |  1  |    1   |  2  | 1 |    1   |  0.5 |  0.5 |   1  |   1   | 1 | 0.5| 0.5 |   1  |  1 |  0  |  2  |
   8  ground  |   1  |  2 |  1  |    2   | 0.5 | 1 |    1   |   2  |   1  |   0  |   1   |0.5|  2 |  1  |   1  |  1 |  2  |  1  |
   9  flying  |   1  |  1 |  1  |   0.5  |  2  | 1 |    2   |   1  |   1  |   1  |   1   | 2 | 0.5|  1  |   1  |  1 | 0.5 |  1  |
   10 psychic |   1  |  1 |  1  |    1   |  1  | 1 |    2   |   2  |   1  |   1  |  0.5  | 1 |  1 |  1  |   1  |  0 | 0.5 |  1  |
   11 bug     |   1  | 0.5|  1  |    1   |  2  | 1 |   0.5  |  0.5 |   1  |  0.5 |   2   | 1 |  1 | 0.5 |   1  |  2 | 0.5 | 0.5 |
   12 rock    |   1  |  2 |  1  |    1   |  1  | 2 |   0.5  |   1  |  0.5 |   2  |   1   | 2 |  1 |  1  |   1  |  1 | 0.5 |  1  |
   13 ghost   |   0  |  1 |  1  |    1   |  1  | 1 |    1   |   1  |   1  |   1  |   2   | 1 |  1 |  2  |   1  | 0.5|  1  |  1  |
   14 dragon  |   1  |  1 |  1  |    1   |  1  | 1 |    1   |   1  |   1  |   1  |   1   | 1 |  1 |  1  |   2  |  1 | 0.5 |  0  |
   15 dark    |   1  |  1 |  1  |    1   |  1  | 1 |   0.5  |   1  |   1  |   1  |   2   | 1 |  1 |  2  |   1  | 0.5|  1  | 0.5 |
   16 steel   |   1  | 0.5| 0.5 |   0.5  |  1  | 2 |    1   |   1  |   1  |   1  |   1   | 1 |  2 |  1  |   1  |  1 | 0.5 |  2  |
   17 fairy   |   1  | 0.5|  1  |    1   |  1  | 1 |    2   |  0.5 |   1  |   1  |   1   | 1 |  1 |  1  |   2  |  2 | 0.5 |  1  |
    0 - no effect
    0.5 - not very effective
    1 - neutral
    2 - super effective
     */
    public static double[][] pokemonTypesTable = {
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0.5, 0, 1, 1, 0.5, 1},
            {1, 0.5, 0.5, 1, 2, 2, 1, 1, 1, 1, 1, 2, 0.5, 1, 0.5, 1, 2, 1},
            {1, 2, 0.5, 1, 0.5, 1, 1, 1, 2, 1, 1, 1, 2, 1, 0.5, 1, 1, 1},
            {1, 1, 2, 0.5, 0.5, 1, 1, 1, 0, 2, 1, 1, 1, 1, 0.5, 1, 1, 1},
            {1, 0.5, 0.5, 1, 2, 0.5, 1, 1, 2, 2, 1, 1, 1, 1, 2, 1, 0.5, 1},
            {2, 1, 1, 1, 1, 2, 1, 0.5, 1, 0.5, 0.5, 0.5, 2, 0, 1, 2, 2, 0.5},
            {1, 1, 1, 1, 2, 1, 1, 0.5, 0.5, 1, 1, 1, 0.5, 0.5, 1, 1, 0, 2},
            {1, 2, 1, 2, 0.5, 1, 1, 2, 1, 0, 1, 0.5, 2, 1, 1, 1, 2, 1},
            {1, 1, 1, 0.5, 2, 1, 2, 1, 1, 1, 1, 2, 0.5, 1, 1, 1, 0.5, 1},
            {1, 1, 1, 1, 1, 1, 2, 2, 1, 1, 0.5, 1, 1, 1, 1, 0, 0.5, 1},
            {1, 0.5, 1, 1, 2, 1, 0.5, 0.5, 1, 0.5, 2, 1, 1, 0.5, 1, 2, 0.5, 0.5},
            {1, 2, 1, 1, 1, 2, 0.5, 1, 0.5, 2, 1, 2, 1, 1, 1, 1, 0.5, 1},
            {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 2, 1, 0.5, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 0.5, 0},
            {1, 1, 1, 1, 1, 1, 0.5, 1, 1, 1, 2, 1, 1, 2, 1, 0.5, 1, 0.5},
            {1, 0.5, 0.5, 0.5, 1, 2, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 0.5, 2},
            {1, 0.5, 1, 1, 1, 1, 2, 0.5, 1, 1, 1, 1, 1, 1, 2, 2, 0.5, 1}
    };
    public static Map<String, Integer> pokemonTypesMap;

    static {
        pokemonTypesMap.put("normal",0);
        pokemonTypesMap.put("fire",1);
        pokemonTypesMap.put("water",2);
        pokemonTypesMap.put("electric",3);
        pokemonTypesMap.put("grass",4);
        pokemonTypesMap.put("ice",5);
        pokemonTypesMap.put("fighting",6);
        pokemonTypesMap.put("poison",7);
        pokemonTypesMap.put("ground",8);
        pokemonTypesMap.put("flying",9);
        pokemonTypesMap.put("psychic",10);
        pokemonTypesMap.put("bug",11);
        pokemonTypesMap.put("rock",12);
        pokemonTypesMap.put("ghost",13);
        pokemonTypesMap.put("dragon",14);
        pokemonTypesMap.put("dark",15);
        pokemonTypesMap.put("steel",16);
        pokemonTypesMap.put("fairy",17);
    }
}
