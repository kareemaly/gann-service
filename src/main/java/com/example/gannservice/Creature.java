package com.example.gannservice;

import java.util.UUID;

import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.lossfunctions.LossFunctions;

public class Creature {
    private MultiLayerNetwork brain;

    private int noOfInputs = 1;
    private int noOfOutputs = 1;

    private String id;
    private float fitness;

    public Creature() {
        this.createBrain();
        // random weights
        this.brain.init();
        this.id = UUID.randomUUID().toString();
    }

    // 2d array (1 row and n columns)
    public Creature(INDArray weights) {
        this.createBrain();
        // random weights
        this.brain.init(weights, true);
        this.id = UUID.randomUUID().toString();
    }

    private void createBrain() {
        MultiLayerConfiguration config = new NeuralNetConfiguration.Builder()
            .list()
            .layer(0, new DenseLayer.Builder().nIn(noOfInputs).nOut(30).activation(Activation.RELU).build())
            .layer(1, new OutputLayer.Builder().lossFunction(LossFunctions.LossFunction.MSE).nIn(30).nOut(noOfOutputs).activation(Activation.SIGMOID).build())
            .build();

        this.brain = new MultiLayerNetwork(config);
    }

    public float[] act(float[] inputs) {
        // 2d array (1 row and inputs.length columns)
        return this.brain.output(Nd4j.create(new float[][] { inputs })).toFloatVector();
    }

    public Creature[] crossover(Creature parent2) {
        Creature parent1 = this;

        long numOfWeights = parent1.brain.numParams();

        INDArray weights1 = Nd4j.create(1, numOfWeights);
        INDArray weights2 = Nd4j.create(1, numOfWeights);

        for (int i = 0; i < numOfWeights; i ++) {
            if (i < Math.floor(numOfWeights/2)) {
                // First half of weights
                weights1.putScalar(0, i, parent1.brain.params().getFloat(0, i));
                weights2.putScalar(0, i, parent2.brain.params().getFloat(0, i));
            } else {
                // Second half of weights
                weights1.putScalar(0, i, parent2.brain.params().getFloat(0, i));
                weights2.putScalar(0, i, parent1.brain.params().getFloat(0, i));
            }
        }

        return new Creature[] {
            new Creature(weights1),
            new Creature(weights2)
        };
    }

    public void mutate() {
        for (int i = 0; i < brain.numParams(); i ++) {
            if (Math.random() < 0.05) {
                brain.params().putScalar(0, i, Math.random()*2 - 1);
            }
        }
    }

    public String getId() {
        return this.id;
    }


    public float getFitness() {
        return this.fitness;
    }

    public void setFitness(float fitness) {
        this.fitness = fitness;
    }
}


