package com.andrzej.RESTfullPokemonAPI.websocket.utils;

import com.andrzej.RESTfullPokemonAPI.model.Pokemon;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PokemonUtils {
    /*
    attack|  defence->
          V       0     1    2       3      4    5      6       7      8      9      10    11  12    13    14    15    16    17
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
            {1, 0.5, 2, 1, 0.5, 1, 1, 0.5, 2, 0.5, 1, 0.5, 2, 1, 0.5, 1, 0.5, 1},
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
    public static Map<String, Integer> pokemonTypesMap = new HashMap<>();

    static {
        pokemonTypesMap.put("normal", 0);
        pokemonTypesMap.put("fire", 1);
        pokemonTypesMap.put("water", 2);
        pokemonTypesMap.put("electric", 3);
        pokemonTypesMap.put("grass", 4);
        pokemonTypesMap.put("ice", 5);
        pokemonTypesMap.put("fighting", 6);
        pokemonTypesMap.put("poison", 7);
        pokemonTypesMap.put("ground", 8);
        pokemonTypesMap.put("flying", 9);
        pokemonTypesMap.put("psychic", 10);
        pokemonTypesMap.put("bug", 11);
        pokemonTypesMap.put("rock", 12);
        pokemonTypesMap.put("ghost", 13);
        pokemonTypesMap.put("dragon", 14);
        pokemonTypesMap.put("dark", 15);
        pokemonTypesMap.put("steel", 16);
        pokemonTypesMap.put("fairy", 17);
    }

    //rock paper scissors implementation
    public static int returnWinningPokemonIndex(Pokemon pokemon1, Pokemon pokemon2) {
        Object obj = new Object();

        synchronized (obj) {
            int pokemon1TypesNumber1 = -1;
            int pokemon1TypesNumber2 = -1;
            int pokemon2TypesNumber1 = -1;
            int pokemon2TypesNumber2 = -1;
            boolean isPokemon1Contains2Types, isPokemon2Contains2Types;
            double pokemon1Points = 0;
            double pokemon2Points = 0;

            List<String> pokemon1Types = pokemon1.getTypes();
            List<String> pokemon2Types = pokemon2.getTypes();

            pokemon1TypesNumber1 = pokemonTypesMap.get(pokemon1Types.get(0).toLowerCase());
            pokemon2TypesNumber1 = pokemonTypesMap.get(pokemon2Types.get(0).toLowerCase());

            pokemon1Points += pokemonTypesTable[pokemon1TypesNumber1][pokemon2TypesNumber1];
            pokemon2Points += pokemonTypesTable[pokemon2TypesNumber1][pokemon1TypesNumber1];

            isPokemon1Contains2Types = (pokemon1Types.size() == 2);
            isPokemon2Contains2Types = (pokemon2Types.size() == 2);

            if (isPokemon1Contains2Types) {
                pokemon1TypesNumber2 = pokemonTypesMap.get(pokemon1Types.get(1).toLowerCase());
            }
            if (isPokemon2Contains2Types) {
                pokemon2TypesNumber2 = pokemonTypesMap.get(pokemon2Types.get(1).toLowerCase());
            }

            if (isPokemon1Contains2Types && isPokemon2Contains2Types) {
                pokemon1Points += pokemonTypesTable[pokemon1TypesNumber1][pokemon2TypesNumber2];
                pokemon2Points += pokemonTypesTable[pokemon2TypesNumber2][pokemon1TypesNumber1];

                pokemon1Points += pokemonTypesTable[pokemon1TypesNumber2][pokemon2TypesNumber1];
                pokemon2Points += pokemonTypesTable[pokemon2TypesNumber1][pokemon1TypesNumber2];

                pokemon1Points += pokemonTypesTable[pokemon1TypesNumber2][pokemon2TypesNumber2];
                pokemon2Points += pokemonTypesTable[pokemon2TypesNumber2][pokemon1TypesNumber2];
            } else if (isPokemon1Contains2Types) {
                pokemon1Points += pokemonTypesTable[pokemon1TypesNumber2][pokemon2TypesNumber1];
                pokemon2Points += pokemonTypesTable[pokemon2TypesNumber1][pokemon1TypesNumber2];
            } else if (isPokemon2Contains2Types) {
                pokemon1Points += pokemonTypesTable[pokemon1TypesNumber1][pokemon2TypesNumber2];
                pokemon2Points += pokemonTypesTable[pokemon2TypesNumber2][pokemon1TypesNumber1];
            }

            if (pokemon1Points == pokemon2Points) {
                return -1;
            } else if (pokemon1Points > pokemon2Points) {
                return 0;
            } else {
                return 1;
            }
        }
    }
}
