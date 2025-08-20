package com.example.PfaBack.models;

import org.springframework.data.annotation.TypeAlias;

@TypeAlias("chicken")
public class Chicken extends Animal {
    public Chicken() {
        super();
        this.setSpecies("chicken");
    }
}
