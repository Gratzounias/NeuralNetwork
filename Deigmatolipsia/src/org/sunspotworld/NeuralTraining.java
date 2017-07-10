/*
Georgios-Xristos Litsas
*/
package org.sunspotworld;


import org.encog.matrix.Matrix;
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
public class NeuralTraining {

    final int MAX_EPOCHS = 100; // 500
    final double MIN_ERROR = 0.001;
    final double learningRate = 0.7;  //Variable to hold the learning rate
    final double momentum = 0.9;  //Variable to hold the momentum

    final BasicNetwork network = new BasicNetwork();

    public static double TempHumInput[][] = {{0.0, 40.0}, {35.0, 60.0},{10.0, 40.0}, {10.0, 50.0}, {20.0, 60.0}, {30.0, 70.0},{30.0, 40.0}, {40.0, 60.0},  {40.0, 15.0}, {50.0, 10.0}, {35, 0}, {35,35}, {60,35}, {70,0} }; // {t,h}
    public static double Temp_Hum_Ideal_Output[][] = {{0.0}, {0.0},{0.0}, {0.0}, {0.0}, {0.0},{0.0},{0.0}, {1.0}, {1.0}, {1.0}, {1.0}, {1.0}, {1.0}, {1.0}, {1.0}};

    public NeuralTraining() {
       
       FeedforwardLayer l1 = new FeedforwardLayer(2);
       FeedforwardLayer l2 = new FeedforwardLayer(4);
       FeedforwardLayer l3 = new FeedforwardLayer(1);
        
        network.addLayer(l1);
        network.addLayer(l2);
        network.addLayer(l3);
        network.reset();
       
        training();
        
    }

    private void training() {// train the neural network
            int epoch = 1;
            NeuralDataSet trainingSet = new BasicNeuralDataSet(TempHumInput, Temp_Hum_Ideal_Output);
            Train train = new Backpropagation(network, trainingSet, learningRate, momentum);

        do {
            train.iteration();
            System.out.println("Epoch #" + epoch + "   Error:" + train.getError());
            epoch++;
        } while ((epoch < MAX_EPOCHS) && (train.getError() > MIN_ERROR));

        System.out.println("Train complete") ;
    }
    
  

    public int compute(double temp, double hum) {
        double input[][] = {{(int)temp, (int)hum}};
        double output[][] = {{0}};

        NeuralDataSet inputset = new BasicNeuralDataSet(input, output);
        BasicNeuralDataSet.BasicNeuralIterator iterator = ((BasicNeuralDataSet) inputset).iterator();  
        NeuralDataPair pair = iterator.next();
        final NeuralData neural_output = network.compute(pair.getInput());
        System.out.println("Results:" + pair.getInput().getData(0) + "," + pair.getInput().getData(1) + ", predicted=" + neural_output.getData(0));

        double predicted_value = neural_output.getData(0);
        if (Math.abs(predicted_value) < 0.3) {
            return 0;
        } else {
            return 1;
        }
    }
}
