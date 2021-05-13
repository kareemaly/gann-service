package com.example.gannservice;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ActInputDto {
    public float[] inputs;

    public ActInputDto(@JsonProperty("inputs") float[] inputs) {
        this.inputs = inputs;
    }
}

