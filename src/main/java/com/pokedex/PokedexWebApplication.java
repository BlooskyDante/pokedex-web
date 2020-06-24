package com.pokedex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class PokedexWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(PokedexWebApplication.class, args);
    }

}
