package com.pokedex.rest.entity;

import com.pokedex.rest.entity.basic.Sprites;
import com.pokedex.rest.entity.wrapper.AbilityWrapper;
import com.pokedex.rest.entity.wrapper.MoveWrapper;
import com.pokedex.rest.entity.wrapper.TypeWrapper;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class Pokemon {
    private String name;
    private Integer weight;
    private List<TypeWrapper> types;
    private List<AbilityWrapper> abilities;
    private List<MoveWrapper> moves;
    private Sprites sprites;

    public Pokemon(String name, Integer weight, List<TypeWrapper> types, List<AbilityWrapper> abilities, List<MoveWrapper> moves, Sprites sprites) {
        this.name = name;
        this.weight = weight;
        this.types = types;
        this.abilities = abilities;
        this.moves = moves;
        this.sprites = sprites;
    }
}
