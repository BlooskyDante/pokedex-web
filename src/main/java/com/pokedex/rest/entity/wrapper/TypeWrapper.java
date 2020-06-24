package com.pokedex.rest.entity.wrapper;

import com.pokedex.rest.entity.basic.Result;
import lombok.Data;

@Data
public class TypeWrapper {
    private Integer slot;
    private Result type;
}
