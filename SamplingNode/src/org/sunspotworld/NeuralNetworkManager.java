/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sunspotworld;

//import org.encog.util.db.javaMETestsServer.TestResult;
import org.encog.neural.data.NeuralDataPair;
import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.data.basic.BasicNeuralDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.Train;
import org.encog.neural.networks.layers.FeedforwardLayer;
import org.encog.neural.networks.training.backpropagation.Backpropagation;

/**
 *
 * @author admin
 */
public class NeuralNetworkManager {

    final int MAX_EPOCHS = 500;
    final double MIN_ERROR = 0.001;
    final int numTests = 1;  //Varible to hold the number of tests to run
    final double learningRate = 0.7;  //Variable to hold the learning rate
    final double momentum = 0.9;  //Variable to hold the momentum

    final BasicNetwork network = new BasicNetwork();

    public static double TEMP_HUMIDITY_INPUT[][] = {{0.0, 0.0}, {1.0, 0.0}, {0.0, 1.0}, {1.0, 1.0}};
    public static double TEMP_HUMIDITY_IDEAL_OUTPUT[][] = {{0.0}, {1.0}, {1.0}, {0.0}};

    public NeuralNetworkManager() {
        network.addLayer(new FeedforwardLayer(2));
        network.addLayer(new FeedforwardLayer(3));
        network.addLayer(new FeedforwardLayer(1));
        network.reset();

        NeuralDataSet trainingSet = new BasicNeuralDataSet(TEMP_HUMIDITY_INPUT, TEMP_HUMIDITY_IDEAL_OUTPUT);

        // train the neural network
        Train train = new Backpropagation(network, trainingSet, learningRate, momentum);

        int epoch = 1;

        long startTime = System.currentTimeMillis();

        do {
            train.iteration();
            System.out.println("Epoch #" + epoch + " Error:" + train.getError());
            epoch++;
        } while ((epoch < MAX_EPOCHS) && (train.getError() > MIN_ERROR));

        long endTime = System.currentTimeMillis();
        long trainingTime = endTime - startTime;

        System.out.println("Training time:" + trainingTime);
    }

    public boolean compute(double t, double h) {
        // ###
        final BasiNeuralData output = network.compute(pair.getInput());

        System.out.println("Neural Network Results:");
        System.out.println(pair.getInput().getData(0) + "," + pair.getInput().getData(1) + ", actual=" + output.getData(0) + ",ideal=" + pair.getIdeal().getData(0));

        double predicted_value = 1; // output.getData(0);
        if (predicted_value != 0.0) {
            return true;
        } else {
            return false;
        }
    }
}
