package com.example.gannservice;

import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GannController {

    private List<Creature> creatures;
    private int noOfCreatures = 30;

    @Autowired
    private RouletteWheelSelection selectionAlgorithm;

    @PostMapping("/first-population")
    public PopulationDto firstPopulation() {
        creatures = Stream.generate(Creature::new).limit(noOfCreatures).collect(Collectors.toList());

        return toPopulationDto(creatures);
    }

    @PostMapping("/next-population")
    public PopulationDto nextPopulation() {
        List<Creature> deadCreatures = new ArrayList<>(creatures);
        creatures.clear();

        for (int i = 0; i < deadCreatures.size()/2; i ++) {
            List<Creature> parents = selectionAlgorithm.select(deadCreatures, true, 2, new Random());

            Creature[] children = parents.get(0).crossover(parents.get(1));

            children[0].mutate();
            children[1].mutate();

            creatures.addAll(Arrays.asList(children));
        }

        return toPopulationDto(creatures);
    }

    @PostMapping("/act/{id}")
    public ActOutputDto act(@PathVariable(value = "id") String id, @RequestBody ActInputDto inputDto) {
        Creature creature = getById(id);
        float[] outputs = creature.act(inputDto.inputs);
        return new ActOutputDto(outputs);
    }

    @PostMapping("/update-fitness/{id}")
    public void act(@PathVariable(value = "id") String id, @RequestBody FitnessInputDto inputDto) {
        Creature creature = getById(id);
        creature.setFitness(inputDto.fitness);
    }

    private Creature getById(String id) {
        return creatures.stream().filter(c -> c.getId().equals(id)).findAny().orElseThrow();
    }

    private PopulationDto toPopulationDto(List<Creature> creatures) {
        return new PopulationDto(
            creatures.stream().map(c -> new CreatureDto(c.getId())).collect(Collectors.toList())
        );
    }
}
