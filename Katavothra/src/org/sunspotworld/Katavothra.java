/*
Georgios-Xristos Litsas
*/
package org.sunspotworld;

import com.sun.spot.io.j2me.radiogram.RadiogramConnection;
import com.sun.spot.peripheral.NoRouteException;
import com.sun.spot.resources.Resources;
import com.sun.spot.resources.transducers.ITriColorLEDArray;
import com.sun.spot.resources.transducers.LEDColor;
import java.io.IOException;
import javax.microedition.io.Connector;
import javax.microedition.io.Datagram;

import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import org.sunspotworld.Connection; 


public class Katavothra extends MIDlet {
    private Connection Connect = null;
    
    public Katavothra(){
            Connect = new Connection();
    }
       public String result;
    private ITriColorLEDArray leds = (ITriColorLEDArray) Resources.lookup(ITriColorLEDArray.class);
    
    

    protected void startApp() throws MIDletStateChangeException {
        System.out.println("KatavothraMidlet Starting...");
        leds.getLED(7).setColor(LEDColor.ORANGE);
        leds.getLED(7).setOn();
                                                  
        Connect.Recieving();
        leds.getLED(7).setOff();
        
        
    }

    protected void pauseApp() {
        
    }

    protected void destroyApp(boolean arg0) throws MIDletStateChangeException {
        
    }

    
}
