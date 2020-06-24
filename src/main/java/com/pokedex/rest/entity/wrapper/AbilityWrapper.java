package com.pokedex.rest.entity.wrapper;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pokedex.rest.entity.basic.Result;
import lombok.Data;

@Data
public class AbilityWrapper {
    @JsonProperty("is_hidden")
    private boolean isHidden;
    private Integer slot;
    private Result ability;
}
