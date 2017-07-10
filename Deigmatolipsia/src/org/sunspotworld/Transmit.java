/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sunspotworld;

import com.sun.spot.io.j2me.radiogram.RadiogramConnection;
import com.sun.spot.peripheral.NoRouteException;
import javax.microedition.io.Connector;
import javax.microedition.io.Datagram;

/**
 *
 * @author admin
 */
public class Transmit {
    
    public void Connection(int result){
        String myaddress = System.getProperty("IEEE_ADDRESS");
        String server = "broadcast";
        
        
        try{
            RadiogramConnection conn = (RadiogramConnection) Connector.open("radiogram://" + server + ":100");
                Datagram dg = conn.newDatagram(conn.getMaximumLength());

                try {                    
                    System.out.println("Generator " + myaddress + ": sending result ... " + result);
                    String x = String.valueOf(result);     
                    
                    String msg = myaddress + "+" + x;
                    
                    dg.writeUTF(msg);
                    conn.send(dg);
 
                } catch (NoRouteException e) {
                    System.out.println(myaddress + ": No route to " + server);
                } finally {
                    conn.close();
                }
        }catch(Exception e0){
            e0.printStackTrace();
        }
        
    }
    
    
    
}
