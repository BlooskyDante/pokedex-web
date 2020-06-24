package com.pokedex.rest.entity.wrapper;

import com.pokedex.rest.entity.basic.EvolutionChain;
import lombok.Data;

@Data
public class EvolutionWrapper {
    private Integer id;
    private EvolutionChain chain;
}
