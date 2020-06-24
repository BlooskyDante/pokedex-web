package com.pokedex.rest.service;

import com.pokedex.rest.entity.DetailedPokemonAbstraction;
import com.pokedex.rest.entity.Pokemon;
import com.pokedex.rest.entity.SimplifiedPokemon;
import com.pokedex.rest.entity.wrapper.AbilityWrapper;
import com.pokedex.rest.entity.wrapper.EvolutionWrapper;
import com.pokedex.rest.entity.wrapper.MoveWrapper;
import com.pokedex.rest.entity.wrapper.TypeWrapper;

import java.util.List;

public interface PokedexService {

    List<SimplifiedPokemon> getPokemonListPaginated(Integer pageNumber);

    Pokemon getPokemonByName(String name);

    DetailedPokemonAbstraction getDetailedPokemonByName(String name);

    String getPokemonEvolutionUrl(String name);

    EvolutionWrapper getPokemonEvolutionByName(String name);

    SimplifiedPokemon getSimplifiedPokemon(Pokemon pokemon);

    List<String> getPokemonAbilities(List<AbilityWrapper> abiliies);

    List<String> getPokemonTypes(List<TypeWrapper> types);

    DetailedPokemonAbstraction getDetailedPokemonAbstraction(Pokemon pokemon, EvolutionWrapper wrapper);

    List<String> getPokemonMoves(List<MoveWrapper> moves);

    Integer getOffsetFromPageNumber(Integer pageNumber);
}
