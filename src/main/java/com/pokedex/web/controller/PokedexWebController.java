package com.pokedex.web.controller;

import com.pokedex.rest.entity.DetailedPokemonAbstraction;
import com.pokedex.rest.entity.wrapper.PokemonPaginatedListResponse;
import com.pokedex.rest.service.PokedexService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Slf4j
@Controller
public class PokedexWebController {

    private PokedexService service;

    public PokedexWebController(PokedexService service) {
        this.service = service;
    }

    @GetMapping(value = "/", produces = "text/html;charset=UTF-8")
    public String index(Model model, @RequestParam(name = "page") Optional<Integer> pageNum) {
        Integer page = pageNum.orElse(1);

        PokemonPaginatedListResponse response = service.getPokemonListPaginated(page);
        model.addAttribute("page", page);
        model.addAttribute("pokemonList", response.getPokemonList());

        return "index";
    }

    @GetMapping(value = "/details", produces = "text/html;charset=UTF-8")
    public String details(Model model, @RequestParam(name = "name") String name) {
        DetailedPokemonAbstraction response = service.getDetailedPokemonByName(name);
        model.addAttribute("name", response.getName());
        model.addAttribute("weight", response.getWeight());
        model.addAttribute("types", response.getTypes());
        model.addAttribute("abilities", response.getAbilities());
        model.addAttribute("spriteUrl", response.getSpriteUrl());
        model.addAttribute("evolutionWrapper", response.getEvolutionWrapper());
        model.addAttribute("moves", response.getMoves());
        return "details";
    }
}
