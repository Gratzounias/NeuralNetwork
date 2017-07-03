/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sunspotworld;

//import org.encog.util.db.javaMETestsServer.TestResult;
import com.sun.spot.resources.transducers.LEDColor;
import java.util.Random;
import java.util.Vector;
import javax.microedition.midlet.MIDletStateChangeException;
import org.encog.neural.data.NeuralData;
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

    final int MAX_EPOCHS = 100; // 500
    final double MIN_ERROR = 0.001;
    final double learningRate = 0.7;  //Variable to hold the learning rate
    final double momentum = 0.9;  //Variable to hold the momentum

    final BasicNetwork network = new BasicNetwork();

    public static double TEMP_HUMIDITY_INPUT[][] = {{0.0, 40.0}, {10.0, 50.0}, {20.0, 60.0}, {30.0, 70.0}, {50.0, 20.0}, {35.0, 20.0}, {40.0, 15.0}, {50.0, 10.0}, {35, 0}, {35,35}, {100,35}, {100,0} }; // {t,h}
    public static double TEMP_HUMIDITY_IDEAL_OUTPUT[][] = {{0.0}, {0.0}, {0.0}, {0.0}, {1.0}, {1.0}, {1.0}, {1.0}, {1.0}, {1.0}, {1.0}, {1.0}};

    public NeuralNetworkManager() {
        System.out.println("NeuralNetworkManager initializing ... layers") ;
        
        network.addLayer(new FeedforwardLayer(2));
        network.addLayer(new FeedforwardLayer(4));
        network.addLayer(new FeedforwardLayer(1));
        network.reset();

        System.out.println("NeuralNetworkManager initializing ... training set") ;
       
        simple_train();
//        advanced_train();
    }

    private void simple_train() {
         NeuralDataSet trainingSet = new BasicNeuralDataSet(TEMP_HUMIDITY_INPUT, TEMP_HUMIDITY_IDEAL_OUTPUT);

        // train the neural network
        Train train = new Backpropagation(network, trainingSet, learningRate, momentum);

        int epoch = 1;

        System.out.println("NeuralNetworkManager training ...") ;
        long startTime = System.currentTimeMillis();

        do {
            train.iteration();
            System.out.println("Epoch #" + epoch + "   Error:" + train.getError());
            epoch++;
        } while ((epoch < MAX_EPOCHS) && (train.getError() > MIN_ERROR));

        long endTime = System.currentTimeMillis();
        long trainingTime = endTime - startTime;

        System.out.println("NeuralNetworkManager train complete") ;
        System.out.println("Training time:" + trainingTime + "  Epochs:" + epoch);
    }
    
    private void advanced_train() {
        NumberGenerator randomGen = new NumberGenerator();

        int LENGTH = 100;
        
        double[][] input = new double[LENGTH][2];
        double[][] output = new double[LENGTH][1];
        
        for (int i = 0; i < LENGTH; i++) {
            double t = randomGen.generateUniformValue();
            double h = randomGen.generateUniformValue();            
            double result = (t >= 35 && h <= 35) ? 1000 : 0;

//            System.out.println("training sample: " + t + "\t" + h + "\t" + result);
            input[i][0] = (int) t;
            input[i][1] = (int) h;
            output[i][0] = result;
        }

        network.addLayer(new FeedforwardLayer(2));
        network.addLayer(new FeedforwardLayer(4));
        network.addLayer(new FeedforwardLayer(1));
        network.reset();

        NeuralDataSet trainingSet = new BasicNeuralDataSet(input, output);

        // train the neural network
        Train train = new Backpropagation(network, trainingSet, learningRate, momentum);

        int epoch = 1;

        long startTime = System.currentTimeMillis();

        do {
            train.iteration();
            System.out.println("Epoch #" + epoch + "   Error:" + train.getError());
            epoch++;
        } while ((epoch < MAX_EPOCHS) && (train.getError() > MIN_ERROR));

        long endTime = System.currentTimeMillis();
        long trainingTime = endTime - startTime;

        System.out.println("Training time:" + trainingTime + "  Epochs:" + epoch);
    }

    public int compute(double t, double h) {
        double input[][] = {{(int)t, (int)h}};
        double output[][] = {{0}};

        NeuralDataSet inputset = new BasicNeuralDataSet(input, output);

        BasicNeuralDataSet.BasicNeuralIterator iterator = ((BasicNeuralDataSet) inputset).iterator();  //sjb - Removed FOR loop format with new iteration format
        NeuralDataPair pair = iterator.next();

        final NeuralData neural_output = network.compute(pair.getInput());

        System.out.println("Neural Network Results:" + pair.getInput().getData(0) + "," + pair.getInput().getData(1) + ", predicted=" + neural_output.getData(0));

        double predicted_value = neural_output.getData(0);

        if (Math.abs(predicted_value) < 0.2) {
            return 0;
        } else {
            return 1;
        }
    }
}
