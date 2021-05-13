package com.example.gannservice;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FitnessInputDto {
    public float fitness;

    public FitnessInputDto(@JsonProperty("fitness") float fitness) {
        this.fitness = fitness;
    }
}

