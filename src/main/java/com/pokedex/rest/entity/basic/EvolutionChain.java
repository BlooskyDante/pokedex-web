package com.pokedex.rest.entity.basic;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class EvolutionChain {
    @JsonProperty("evolves_to")
    private List<EvolutionChain> evolvesTo;
    private Result species;
}
