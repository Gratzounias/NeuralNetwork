
package org.sunspotworld;

import com.sun.spot.io.j2me.radiogram.RadiogramConnection;
import com.sun.spot.peripheral.NoRouteException;
import com.sun.spot.resources.Resources;
import com.sun.spot.resources.transducers.ITriColorLED;
import com.sun.spot.resources.transducers.ITriColorLEDArray;
import com.sun.spot.resources.transducers.LEDColor;
import java.util.Vector;
import javax.microedition.io.Connector;
import javax.microedition.io.Datagram;

/**
 *
 * @author Georgios-Xristos
 * 
 */
public class Connections {

    
    private ITriColorLEDArray leds = (ITriColorLEDArray) Resources.lookup(ITriColorLEDArray.class); 
    private Vector vector1 = new Vector(), vector2 = new Vector(), vector3 = new Vector();
    private ITriColorLED led1 = leds.getLED(0);
    private ITriColorLED led2 = leds.getLED(2);
    private Address addr= null;
    
    public Connections(){
            addr= new Address();
    }
    void RecievConnection(){
                try {
                    
                    RadiogramConnection conn = (RadiogramConnection) Connector.open("radiogram://:100");
                    Datagram dg = conn.newDatagram(conn.getMaximumLength());
                    Datagram dgreply = conn.newDatagram(conn.getMaximumLength());
                        
                    try {
   
                        conn.receive(dg);  
                        led1.setColor(LEDColor.BLUE);
                        String question = dg.readUTF();
                        int p = question.indexOf('+');
                        String address = question.substring(0, p);
                        String value = question.substring(p + 1);
                        Storing(address,value);
                        

                    } catch (NoRouteException e) {
                        System.out.println("No connection to ");
                    } finally {
                        conn.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
    }
    
    void TransmitConnection(String result){
    try {
                        RadiogramConnection conn = (RadiogramConnection) Connector.open("radiogram://0a00.020f.0000.1005:105");
                        
                        Datagram dg = conn.newDatagram(conn.getMaximumLength());
                        try {
                            System.out.println("Sygkentrwtis sending ");
                            dg.writeUTF(result);
                            conn.send(dg);
                        } catch (NoRouteException e) {
                            System.out.println("No route to katavothra");
                        } finally {
                            conn.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
    }

    public void Storing(String address, String value){
                if (address.equalsIgnoreCase(addr.ADDR1)) {
                            vector1.addElement(value);
                        } else if (address.equalsIgnoreCase(addr.ADDR2)) {
                            vector2.addElement(value);
                        } else if (address.equalsIgnoreCase(addr.ADDR3)) { // 
                            vector3.addElement(value);
                        } else {
                            System.out.println("unknown connection");
                        }
    }
    
    public String VectorStoring(String vec1, String vec2, String vec3){
       int tmp;
        tmp = 0;
                    if (vec1.equals("1")) {
                        tmp++;
                    }
                    if (vec2.equals("1")) {
                        tmp++;
                    }
                    if (vec3.equals("1")) {
                        tmp++;
                    }
                    
                    String result = null;
                    if (tmp < 2) {
                        result = "0";
                    } else {
                        result = "1";
                    }

        
        return result;
    
    }
    
}
