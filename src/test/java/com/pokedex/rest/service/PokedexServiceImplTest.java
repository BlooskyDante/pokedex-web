package com.pokedex.rest.service;

import com.pokedex.rest.entity.DetailedPokemonAbstraction;
import com.pokedex.rest.entity.PaginatedPokemonResponse;
import com.pokedex.rest.entity.Pokemon;
import com.pokedex.rest.entity.PokemonSpecies;
import com.pokedex.rest.entity.SimplifiedPokemon;
import com.pokedex.rest.entity.basic.EvolutionChain;
import com.pokedex.rest.entity.basic.Result;
import com.pokedex.rest.entity.basic.Sprites;
import com.pokedex.rest.entity.basic.UrlWrapper;
import com.pokedex.rest.entity.wrapper.AbilityWrapper;
import com.pokedex.rest.entity.wrapper.EvolutionWrapper;
import com.pokedex.rest.entity.wrapper.MoveWrapper;
import com.pokedex.rest.entity.wrapper.PokemonPaginatedListResponse;
import com.pokedex.rest.entity.wrapper.TypeWrapper;
import com.pokedex.rest.util.rest.InvokeRestService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.pokedex.rest.constant.EndpointConstant.POKEMON_SPECIES_ENDPOINT;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class PokedexServiceImplTest {
    @Mock
    private InvokeRestService invoke;

    @Spy
    @InjectMocks
    private PokedexService service = new PokedexServiceImpl();


    private static final Integer PAGE_NUMBER = 1;
    private static final String BASE_URL = "https://pokeapi.co/api/v2/";
    private static final Integer POKEMON_COUNT = 1;
    private static final String SPECIES_NAME = "SPECIES NAME";
    private static final String SPRITE_URL = "SPRITE URL";
    private static final String TYPE_NAME = "TYPE NAME";
    private static final String ABILITY_NAME = "ABILITY NAME";
    private static final String MOVE_NAME = "MOVE NAME";
    private static final String NAME = "POKEMON NAME";
    private static final Integer WEIGHT = 10;


    private List<TypeWrapper> tWrapperList;
    private List<MoveWrapper> mWrapperList;
    private List<AbilityWrapper> aWrapperList;
    private List<SimplifiedPokemon> simplifiedPokemonList;
    private List<String> moveList;
    private List<String> abilityList;
    private List<String> typeList;
    private List<Result> resultList;
    private MoveWrapper mWrapper;
    private AbilityWrapper aWrapper;
    private TypeWrapper tWrapper;
    private EvolutionWrapper eWrapper;
    private EvolutionChain eChain;
    private Result species;
    private Result ability;
    private Result move;
    private Result type;
    private Result result;
    private Sprites sprites;
    private UrlWrapper urlWrapper;

    private Pokemon pokemon;
    private DetailedPokemonAbstraction detailedPokemonAbstraction;
    private SimplifiedPokemon simplifiedPokemon;
    private PokemonSpecies pokemonSpecies;

    private PokemonPaginatedListResponse response;

    private PaginatedPokemonResponse paginatedResponse;

    @Before
    public void setUp() {

        ReflectionTestUtils.setField(service, "baseURL", BASE_URL);

        tWrapperList = new ArrayList<>();
        mWrapperList = new ArrayList<>();
        aWrapperList = new ArrayList<>();
        moveList = new ArrayList<>();
        eWrapper = new EvolutionWrapper();
        abilityList = new ArrayList<>();
        typeList = new ArrayList<>();
        pokemonSpecies = new PokemonSpecies();
        simplifiedPokemonList = new ArrayList<>();
        resultList = new ArrayList<>();

        mWrapper = new MoveWrapper();
        aWrapper = new AbilityWrapper();
        tWrapper = new TypeWrapper();
        eChain = new EvolutionChain();
        urlWrapper = new UrlWrapper();

        move = new Result();
        ability = new Result();
        type = new Result();
        sprites = new Sprites();
        species = new Result();
        result = new Result();

        move.setName(MOVE_NAME);
        mWrapper.setMove(move);
        mWrapperList.add(mWrapper);

        ability.setName(ABILITY_NAME);
        aWrapper.setAbility(ability);
        aWrapperList.add(aWrapper);

        type.setName(TYPE_NAME);
        tWrapper.setType(type);
        tWrapperList.add(tWrapper);

        species.setName(SPECIES_NAME);
        eChain.setSpecies(species);
        eWrapper.setChain(eChain);

        sprites.setUrl(SPRITE_URL);

        moveList.add(MOVE_NAME);
        abilityList.add(ABILITY_NAME);
        typeList.add(TYPE_NAME);

        pokemon = new Pokemon(NAME, WEIGHT, tWrapperList, aWrapperList, mWrapperList, sprites);

        simplifiedPokemon = new SimplifiedPokemon();
        simplifiedPokemon.setName(NAME);
        simplifiedPokemon.setWeight(WEIGHT);
        simplifiedPokemon.setSpriteUrl(SPRITE_URL);
        simplifiedPokemon.setAbilities(abilityList);
        simplifiedPokemon.setTypes(typeList);

        detailedPokemonAbstraction = new DetailedPokemonAbstraction(simplifiedPokemon, moveList, eWrapper);

        urlWrapper.setUrl(BASE_URL + POKEMON_SPECIES_ENDPOINT + NAME);
        pokemonSpecies.setEvolutionUrl(urlWrapper);

        simplifiedPokemonList.add(simplifiedPokemon);
        response = new PokemonPaginatedListResponse();

        result.setName(NAME);
        resultList.add(result);

        paginatedResponse = new PaginatedPokemonResponse();
        paginatedResponse.setResults(resultList);
        paginatedResponse.setCount(POKEMON_COUNT);

        response.setPokemonList(simplifiedPokemonList);
        response.setCount(POKEMON_COUNT);
        response.setPokemonList(simplifiedPokemonList);

    }

    @Test
    public void given_nullArgument_when_getOffsetFromPageNumber_then_returnZero() {
        final Integer EXPECTED = 0;
        Integer result = service.getOffsetFromPageNumber(null);

        assertEquals(EXPECTED, result);
    }

    @Test
    public void given_pageNumberTwo_when_getOffsetFromPageNumber_then_returnOffset20() {
        final Integer EXPECTED = 20;
        Integer result = service.getOffsetFromPageNumber(2);
        assertEquals(EXPECTED, result);
    }

    @Test
    public void given_nullMoves_when_getPokemonMoves_then_returnEmptyList() {
        List<String> result = service.getPokemonMoves(null);

        assertEquals(Collections.EMPTY_LIST, result);
    }

    @Test
    public void given_validMoves_when_getPokemonMoves_then_returnList() {
        List<String> result = service.getPokemonMoves(mWrapperList);

        assertEquals(1, result.size());
        assertEquals(MOVE_NAME, result.get(0));
    }

    @Test
    public void given_validInput_when_getDetailedPokemonAbstraction_then_returnDetailedPokemonAbstraction() {
        doReturn(simplifiedPokemon).when(service).getSimplifiedPokemon(any());
        doReturn(moveList).when(service).getPokemonMoves(any());

        DetailedPokemonAbstraction result = service.getDetailedPokemonAbstraction(pokemon, eWrapper);

        assertEquals(NAME, result.getName());
        assertEquals(WEIGHT, result.getWeight());
        assertEquals(1, result.getAbilities().size());
        assertEquals(ABILITY_NAME, result.getAbilities().get(0));
    }

    @Test
    public void given_validInput_when_getPokemonTypes_then_returnTypeList() {
        List<String> result = service.getPokemonTypes(tWrapperList);

        assertEquals(1, result.size());
        assertEquals(TYPE_NAME, result.get(0));
    }

    @Test
    public void given_validInput_when_getPokemonAbilities_then_returnAbilityList() {
        List<String> result = service.getPokemonAbilities(aWrapperList);
        assertEquals(1, result.size());
        assertEquals(ABILITY_NAME, result.get(0));
    }

    @Test
    public void given_pokemon_when_getSimplifiedPokemon_then_returnSimpPokemonInstance() {
        doReturn(abilityList).when(service).getPokemonAbilities(any());
        doReturn(typeList).when(service).getPokemonTypes(anyList());

        SimplifiedPokemon result = service.getSimplifiedPokemon(pokemon);

        assertEquals(NAME, result.getName());
        assertEquals(WEIGHT, result.getWeight());
        assertEquals(SPRITE_URL, result.getSpriteUrl());
    }

    @Test
    public void given_pokemonName_when_getPokemonEvolutionURL_then_returnEndpoint() {
        doReturn(pokemonSpecies).when(invoke).invokeRestService(any(), any(), any(), any());
        final String EXPECTED = BASE_URL + POKEMON_SPECIES_ENDPOINT + NAME;
        String result = service.getPokemonEvolutionUrl(NAME);

        assertEquals(EXPECTED, result);
    }

    @Test
    public void given_pokemonName_when_getPokemonEvolutionByName_then_returnEvolutionWrapper() {
        final String EVOLUTION_URL = BASE_URL + POKEMON_SPECIES_ENDPOINT + NAME;
        doReturn(EVOLUTION_URL).when(service).getPokemonEvolutionUrl(any());
        doReturn(eWrapper).when(invoke).invokeRestService(any(), any(), any(), any());

        EvolutionWrapper result = service.getPokemonEvolutionByName(NAME);

        verify(service).getPokemonEvolutionUrl(any());
        verify(invoke).invokeRestService(any(), any(), any(), any());
        assertEquals(eWrapper, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void given_invalidName_when_getPokemonEvolutionByName_then_throwIllegalArgumentException() {
        service.getPokemonEvolutionByName(null);
    }

    @Test
    public void given_validInput_when_getDetailedPokemonByName_then_returnDetailedPokeAbstraction() {
        doReturn(pokemon).when(service).getPokemonByName(any());
        doReturn(eWrapper).when(service).getPokemonEvolutionByName(any());
        doReturn(detailedPokemonAbstraction).when(service).getDetailedPokemonAbstraction(any(), any());

        DetailedPokemonAbstraction res = service.getDetailedPokemonByName(NAME);

        assertEquals(detailedPokemonAbstraction, res);
    }

    @Test
    public void given_exceptionThrown_when_getDetailedPokemonByName_then_returnNull() {
        doThrow(NullPointerException.class).when(service).getPokemonByName(any());

        DetailedPokemonAbstraction res = service.getDetailedPokemonByName(null);

        assertNull(res);
    }

    @Test
    public void given_invalidName_when_getPokemonByName_then_returnNull() {
        Pokemon res = service.getPokemonByName(null);

        assertNull(res);
    }

    @Test
    public void given_validInput_when_getPokemonByName_then_returnPokemon() {
        doReturn(pokemon).when(invoke).invokeRestService(any(), any(), any(), any());

        Pokemon res = service.getPokemonByName(NAME);

        assertEquals(pokemon, res);
    }

    @Test
    public void given_validInput_when_getPokemonListPaginated_returnResponse() {
        doReturn(paginatedResponse).when(invoke).invokeRestService(any(), any(), any(), any());
        doReturn(pokemon).when(service).getPokemonByName(any());
        doReturn(simplifiedPokemon).when(service).getSimplifiedPokemon(any());

        PokemonPaginatedListResponse res = service.getPokemonListPaginated(PAGE_NUMBER);

        assertEquals(response.getCount(), res.getCount());
        assertEquals(response.getPokemonList().size(), res.getPokemonList().size());
    }

    @Test
    public void given_invalidPaginatedPokemonResponse_when_getPokemonListPaginated_then_returnEmptyResponse() {
        doReturn(null).when(invoke).invokeRestService(any(), any(), any(), any());

        PokemonPaginatedListResponse res = service.getPokemonListPaginated(PAGE_NUMBER);
        
        assertEquals(0, res.getPokemonList().size());
    }
}