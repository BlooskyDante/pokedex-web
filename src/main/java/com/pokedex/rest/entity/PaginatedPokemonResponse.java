package com.pokedex.rest.entity;

import com.pokedex.rest.entity.basic.Result;
import lombok.Data;

import java.util.List;

@Data
public class PaginatedPokemonResponse {
    private Integer count;
    private List<Result> results;
}
