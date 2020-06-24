package com.pokedex.rest.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pokedex.rest.entity.basic.UrlWrapper;
import lombok.Data;

@Data
public class PokemonSpecies {
    @JsonProperty("evolution_chain")
    private UrlWrapper evolutionUrl;
}
