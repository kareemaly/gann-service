package com.example.gannservice;

import java.util.List;

public class PopulationDto {
    public List<CreatureDto> creatures;

    public PopulationDto(List<CreatureDto> creatures) {
        this.creatures = creatures;
    }
}

