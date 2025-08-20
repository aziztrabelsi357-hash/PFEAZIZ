package com.example.PfaBack.models;

import org.springframework.data.annotation.TypeAlias;

@TypeAlias("sheep")
public class Sheep extends Animal {
    public Sheep() {
        super();
        this.setSpecies("sheep");
    }
}
