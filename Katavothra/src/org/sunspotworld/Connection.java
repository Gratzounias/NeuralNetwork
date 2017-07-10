/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sunspotworld;

import com.sun.spot.io.j2me.radiogram.RadiogramConnection;
import com.sun.spot.peripheral.NoRouteException;
import com.sun.spot.resources.Resources;
import com.sun.spot.resources.transducers.ITriColorLED;
import com.sun.spot.resources.transducers.ITriColorLEDArray;
import com.sun.spot.resources.transducers.LEDColor;
import java.io.IOException;
import java.util.Vector;
import javax.microedition.io.Connector;
import javax.microedition.io.Datagram;

/**
 *
 * @author admin
 */
public class Connection {
     private ITriColorLEDArray leds = (ITriColorLEDArray) Resources.lookup(ITriColorLEDArray.class); 
    private Vector vector1 = new Vector(), vector2 = new Vector(), vector3 = new Vector();
    private ITriColorLED led1 = leds.getLED(0);
    private ITriColorLED led2 = leds.getLED(2);
    
    public void Recieving(){
        RadiogramConnection conn = null;

        try {
            System.out.println("Initiate"); 
            conn = (RadiogramConnection) Connector.open("radiogram://:105");
            Datagram dg = conn.newDatagram(conn.getMaximumLength());
            Datagram dgreply = conn.newDatagram(conn.getMaximumLength());

            while (true) {
                try {
                    conn.receive(dg);
                    String result = dg.readUTF();
                    System.out.println("Katavothra received:  result = " + result); 
                    ShowResult(result);
                    
                } catch (NoRouteException e) {
                    System.out.println("No connection to " );
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        
        
        
    }
     public void ShowResult(String result){
                    if (result.equals("0")) {
                            leds.getLED(0).setOff();
                            leds.getLED(7).setColor(LEDColor.GREEN);
                            leds.getLED(7).setOn();
                    } else {
                            leds.getLED(7).setOff();
                            leds.getLED(0).setColor(LEDColor.RED);
                            leds.getLED(0).setOn();
                        
                    }
                        
   }
    
    
    
    
}
