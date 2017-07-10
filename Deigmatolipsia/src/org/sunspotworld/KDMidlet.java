/*
Georgios-Xristos Litsas
*/
package org.sunspotworld;

import com.sun.spot.resources.Resources;
import com.sun.spot.resources.transducers.LEDColor;
import com.sun.spot.resources.transducers.ITriColorLEDArray;
import com.sun.spot.resources.transducers.SwitchEvent;
import com.sun.spot.io.j2me.radiogram.*;
import com.sun.spot.peripheral.NoRouteException;

import java.io.*;
import java.util.Random;
import java.util.Vector;
import javax.microedition.io.*;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import org.sunspotworld.Transmit;

public class KDMidlet extends MIDlet {

    private ITriColorLEDArray leds = (ITriColorLEDArray) Resources.lookup(ITriColorLEDArray.class);
    private LEDColor[] colors = {LEDColor.RED, LEDColor.GREEN, LEDColor.BLUE};
    private RadiogramConnection tx = null;
    private GaussianGenerator randomGen = new GaussianGenerator();
    private NeuralTraining neural = null;
    int freq=2000;
    private Transmit tconnect=null;
    
    public KDMidlet() {
        neural = new NeuralTraining();
        tconnect= new Transmit();
    }
    
    
    

    protected void startApp() throws MIDletStateChangeException {
       
        String myaddress = System.getProperty("IEEE_ADDRESS");
        
        try {

            while (true) {
                Thread.sleep(freq);
                System.out.println(myaddress + "Started");
                double T = randomGen.generateGaussianValue(35, 10);
                double H = randomGen.generateGaussianValue(50, 35);
                int result = neural.compute(T, H);
               
                System.out.println("T = " + T   + " H = " + H + " R: " + result);

                if (result == 1) {
                    leds.getLED(0).setColor(LEDColor.RED);
                } else {
                    leds.getLED(0).setColor(LEDColor.GREEN);
                }

                leds.getLED(0).setOn();
                 tconnect.Connection(result);
                
            }
        } catch (Exception ex) {
            System.out.println(myaddress + "Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    protected void pauseApp() {
       
    }

    protected void destroyApp(boolean arg0) throws MIDletStateChangeException {
        
    }

    public void switchReleased(SwitchEvent evt) {
       
    }

    public void switchPressed(SwitchEvent evt) {
    }
}
