package com.pokedex.rest.service;

import com.pokedex.rest.entity.DetailedPokemonAbstraction;
import com.pokedex.rest.entity.PaginatedPokemonResponse;
import com.pokedex.rest.entity.Pokemon;
import com.pokedex.rest.entity.PokemonSpecies;
import com.pokedex.rest.entity.SimplifiedPokemon;
import com.pokedex.rest.entity.wrapper.AbilityWrapper;
import com.pokedex.rest.entity.wrapper.EvolutionWrapper;
import com.pokedex.rest.entity.wrapper.MoveWrapper;
import com.pokedex.rest.entity.wrapper.PokemonPaginatedListResponse;
import com.pokedex.rest.entity.wrapper.TypeWrapper;
import com.pokedex.rest.util.rest.InvokeRestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.pokedex.rest.constant.EndpointConstant.POKEMON_ENDPOINT;
import static com.pokedex.rest.constant.EndpointConstant.POKEMON_SPECIES_ENDPOINT;
import static com.pokedex.rest.util.validation.ValidationPredicate.validateNonEmptyString;

@Service
public class PokedexServiceImpl implements PokedexService {

    private static final HttpMethod GET_METHOD = HttpMethod.GET;
    private static final Logger LOG = LoggerFactory.getLogger(PokedexServiceImpl.class);
    public static final String ILLEGAL_NAME = "Argument name cant be null or empty string.";
    public static final String ERROR = "Error: ";

    @Value("${api.pokemon.url}")
    private String baseURL;

    private InvokeRestService invoke;

    public PokedexServiceImpl(InvokeRestService invoke) {
        this.invoke = invoke;
    }

    @Override
    @Cacheable("pokemonListPaginated")
    public PokemonPaginatedListResponse getPokemonListPaginated(Integer pageNumber) {
        PokemonPaginatedListResponse resp = new PokemonPaginatedListResponse();
        List<SimplifiedPokemon> pokemonList = new ArrayList<>();
        try {
            final String ENDPOINT = baseURL + POKEMON_ENDPOINT + "?limit=20&offset=" + getOffsetFromPageNumber(pageNumber);
            PaginatedPokemonResponse response = invoke.invokeRestService(ENDPOINT, GET_METHOD, null, PaginatedPokemonResponse.class);

            if (response != null) {
                pokemonList = response.getResults().stream()
                        .map(res -> getPokemonByName(res.getName()))
                        .filter(Objects::nonNull)
                        .map(this::getSimplifiedPokemon)
                        .collect(Collectors.toList());

                resp.setCount(response.getCount());
                resp.setPokemonList(pokemonList);
            } else {
                throw new IllegalArgumentException("The response from the API was invalid and the object is null.");
            }
        } catch (Exception e) {
            LOG.error(ERROR, e);
        }
        return resp;
    }

    @Override
    @Cacheable("getPokemonByName")
    public Pokemon getPokemonByName(String name) {
        Pokemon pokemon = null;
        try {
            if (validateNonEmptyString.test(name)) {
                String endpoint = baseURL + POKEMON_ENDPOINT + name;
                pokemon = invoke.invokeRestService(endpoint, GET_METHOD, null, Pokemon.class);
            } else {
                throw new IllegalArgumentException(ILLEGAL_NAME);
            }
        } catch (Exception e) {
            LOG.error(ERROR, e);
        }
        return pokemon;
    }

    @Override
    public DetailedPokemonAbstraction getDetailedPokemonByName(String name) {
        DetailedPokemonAbstraction abstraction;
        try {
            Pokemon pokemon = getPokemonByName(name);
            EvolutionWrapper wrapper = this.getPokemonEvolutionByName(name);

            abstraction = getDetailedPokemonAbstraction(pokemon, wrapper);

        } catch (Exception e) {
            LOG.error(ERROR, e);
            abstraction = null;
        }
        return abstraction;
    }


    @Override
    public EvolutionWrapper getPokemonEvolutionByName(String name) {
        EvolutionWrapper wrapper;
        if (name != null) {
            String evolutionUrl = getPokemonEvolutionUrl(name);
            wrapper = invoke.invokeRestService(evolutionUrl, GET_METHOD, null, EvolutionWrapper.class);

        } else {
            throw new IllegalArgumentException(ILLEGAL_NAME);
        }
        return wrapper;
    }

    @Override
    public String getPokemonEvolutionUrl(String name) {
        String endpoint = baseURL + POKEMON_SPECIES_ENDPOINT + name;
        PokemonSpecies pokemonSpecies = invoke.invokeRestService(endpoint, GET_METHOD, null, PokemonSpecies.class);
        return pokemonSpecies.getEvolutionUrl().getUrl();
    }

    @Override
    public SimplifiedPokemon getSimplifiedPokemon(Pokemon pokemon) {
        SimplifiedPokemon abstraction = new SimplifiedPokemon();
        abstraction.setName(pokemon.getName());
        abstraction.setWeight(pokemon.getWeight());
        abstraction.setSpriteUrl(pokemon.getSprites().getUrl());
        abstraction.setAbilities(getPokemonAbilities(pokemon.getAbilities()));
        abstraction.setTypes(getPokemonTypes(pokemon.getTypes()));
        return abstraction;
    }

    @Override
    public List<String> getPokemonAbilities(List<AbilityWrapper> abiliies) {
        return abiliies
                .stream()
                .map(abilityWrapper -> abilityWrapper.getAbility().getName())
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getPokemonTypes(List<TypeWrapper> types) {
        return types.stream()
                .map(typeWrapper -> typeWrapper.getType().getName())
                .collect(Collectors.toList());
    }

    @Override
    public DetailedPokemonAbstraction getDetailedPokemonAbstraction(Pokemon pokemon, EvolutionWrapper wrapper) {
        SimplifiedPokemon simplifiedPokemon = getSimplifiedPokemon(pokemon);
        return new DetailedPokemonAbstraction(simplifiedPokemon,
                getPokemonMoves(pokemon.getMoves()),
                wrapper);
    }

    @Override
    public List<String> getPokemonMoves(List<MoveWrapper> moves) {
        return moves.stream()
                .map(move -> move.getMove().getName())
                .collect(Collectors.toList());
    }

    @Override
    public Integer getOffsetFromPageNumber(Integer pageNumber) {
        return pageNumber < 0 ? 0 : 20 * --pageNumber;
    }
}
