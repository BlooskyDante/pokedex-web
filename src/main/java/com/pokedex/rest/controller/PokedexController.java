package com.pokedex.rest.controller;

import com.pokedex.rest.entity.SimplifiedPokemon;
import com.pokedex.rest.entity.DetailedPokemonAbstraction;
import com.pokedex.rest.entity.wrapper.PokemonPaginatedListResponse;
import com.pokedex.rest.service.PokedexService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pokemon")
public class PokedexController {

    private PokedexService service;

    public PokedexController(PokedexService service) {
        this.service = service;
    }

    @GetMapping("/page/{pageNumber}")
    public PokemonPaginatedListResponse getPokemonListPaginated(@PathVariable Integer pageNumber) {
        return service.getPokemonListPaginated(pageNumber);
    }


    @GetMapping("/name/{name}")
    public DetailedPokemonAbstraction getPokemonDetailsByName(@PathVariable String name) {
        return service.getDetailedPokemonByName(name);
    }

}
