/*
Georgios-Xristos Litsas
*/
package org.sunspotworld;

import com.sun.spot.io.j2me.radiogram.RadiogramConnection;

import com.sun.spot.peripheral.NoRouteException;
import com.sun.spot.resources.Resources;
import com.sun.spot.resources.transducers.ITriColorLED;
import com.sun.spot.resources.transducers.ITriColorLEDArray;
import com.sun.spot.resources.transducers.LEDColor;
import com.sun.spot.resources.transducers.SwitchEvent;
import com.sun.spot.util.*;
import java.util.Vector;
import javax.microedition.io.Connector;
import javax.microedition.io.Datagram;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import org.sunspotworld.Connections;


public class Sygkentrwtis extends MIDlet {

    private ITriColorLEDArray leds = (ITriColorLEDArray) Resources.lookup(ITriColorLEDArray.class);  
    private ITriColorLED led1 = leds.getLED(0);
    private ITriColorLED led2 = leds.getLED(2);
    private volatile boolean working = false;
    private CollectionThread thread1 = new CollectionThread();
    private TransmitingThread thread2 = new TransmitingThread();
    private Vector vector1 = new Vector(), vector2 = new Vector(), vector3 = new Vector();
    int sleep =200 ,freq1=1000, freq2=2000;
    private Connections connect = null;
    
    public Sygkentrwtis (){
            connect = new Connections();
    }

     class CollectionThread extends Thread {

        public void run() {
            while (!working) {
                led1.setColor(LEDColor.BLUE);
                    led1.setOn();
                    connect.RecievConnection();
            }

            Utils.sleep(freq1);
        }
    }

    
    
    class TransmitingThread extends Thread {

        public void run() {
            Utils.sleep(sleep);
            
            while (!working) {
                Utils.sleep(freq2);

                if (led2.isOn()) {
                    led2.setOff();
                } else {
                    led2.setColor(LEDColor.GREEN);
                    led2.setOn();
                }

                Algorithm();
            }
        }
    }
    
    public void Algorithm(){
        String result;
    if (!vector1.isEmpty() && !vector2.isEmpty() && !vector3.isEmpty()) {
                    String vec1 = (String) vector1.elementAt(0);
                    String vec2 = (String) vector2.elementAt(0);
                    String vec3 = (String) vector3.elementAt(0);

                    vector1.removeElementAt(0);
                    vector2.removeElementAt(0);
                    vector3.removeElementAt(0);

                    result= connect.VectorStoring(vec1, vec2, vec3);
                    System.out.println("Transmiting: " + vec1 + vec2 + vec3 + " : " + result);

                   connect.TransmitConnection(result);

                } else {
                    System.out.println("no data");
                }

    }

    protected void startApp() throws MIDletStateChangeException {

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (Exception e) {
            e.printStackTrace();
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
