/*
 * Copyright (c) 2010 Oracle.
 * Copyright (c) 2007 Sun Microsystems, Inc.
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS
 * BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.sunspotworld;

import com.sun.spot.io.j2me.radiogram.RadiogramConnection;
import com.sun.spot.peripheral.NoRouteException;
import com.sun.spot.resources.Resources;
import com.sun.spot.resources.transducers.ISwitch;
import com.sun.spot.resources.transducers.ISwitchListener;
import com.sun.spot.resources.transducers.ITriColorLED;
import com.sun.spot.resources.transducers.ITriColorLEDArray;
import com.sun.spot.resources.transducers.LEDColor;
import com.sun.spot.resources.transducers.SwitchEvent;
import com.sun.spot.util.*;
import java.io.IOException;
import javax.microedition.io.Connector;
import javax.microedition.io.Datagram;

import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

/**
 * A simple MIDlet that uses the left switch (SW1) to set the color of the LEDs
 * and the right switch (SW2) to count in binary in the LEDs.
 */
public class Sink extends MIDlet {

    private ITriColorLEDArray leds = (ITriColorLEDArray) Resources.lookup(ITriColorLEDArray.class);

    protected void startApp() throws MIDletStateChangeException {
        System.out.println("=========== Sink MIDlet ==========");

        RadiogramConnection conn = null;

        try {
            conn = (RadiogramConnection) Connector.open("radiogram://:103");

            System.out.println("Sink: Radiogram connection prepared ... ");

            Datagram dg = conn.newDatagram(conn.getMaximumLength());
            Datagram dgreply = conn.newDatagram(conn.getMaximumLength());

            while (true) {
                try {
//                    System.out.println("Sink: Waiting for data ... ");
                    conn.receive(dg);

                    String result = dg.readUTF();
//                    System.out.println("Sink: Client result: " + result);

                    System.out.println("************** Sink received:  result = " + result);

                    if (result.equals("1")) {
                        for (int i=0;i<=7;i++) {
                            leds.getLED(i).setColor(LEDColor.RED);
                            leds.getLED(i).setOn();
                        }
                    } else {
                        for (int i=0;i<=7;i++) {
                            leds.getLED(i).setColor(LEDColor.GREEN);
                            leds.getLED(i).setOn();
                        }
                    }
                    
                } catch (NoRouteException e) {
                    System.out.println("Sink No route to " + dgreply.getAddress());
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

    protected void pauseApp() {
        // This will never be called by the Squawk VM
    }

    protected void destroyApp(boolean arg0) throws MIDletStateChangeException {
        // Only called if startApp throws any exception other than MIDletStateChangeException
    }
}
