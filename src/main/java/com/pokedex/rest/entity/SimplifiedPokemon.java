package com.pokedex.rest.entity;

import lombok.Data;

import java.util.List;

@Data
public class SimplifiedPokemon {
    private String name;
    private Integer weight;
    private List<String> types;
    private List<String> abilities;
    private String spriteUrl;

    public SimplifiedPokemon() {
    }

    public SimplifiedPokemon(String name, Integer weight, List<String> types, List<String> abilities, String spriteUrl) {
        this.name = name;
        this.weight = weight;
        this.types = types;
        this.abilities = abilities;
        this.spriteUrl = spriteUrl;
    }


}
