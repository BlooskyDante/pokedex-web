package com.pokedex.rest.entity.basic;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Sprites {
    @JsonProperty("front_default")
    private String url;
}
