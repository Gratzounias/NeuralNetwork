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
import com.sun.spot.io.j2me.radiostream.RadiostreamConnection;
import com.sun.spot.peripheral.NoRouteException;
import com.sun.spot.resources.Resources;
import com.sun.spot.resources.transducers.ISwitch;
import com.sun.spot.resources.transducers.ISwitchListener;
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

/**
 * A simple MIDlet that uses the left switch (SW1) to set the color of the LEDs
 * and the right switch (SW2) to count in binary in the LEDs.
 */
public class Collect extends MIDlet implements ISwitchListener {

    private ITriColorLEDArray leds = (ITriColorLEDArray) Resources.lookup(ITriColorLEDArray.class);
    private ISwitch sw1 = (ISwitch) Resources.lookup(ISwitch.class, "SW1");
    private ISwitch sw2 = (ISwitch) Resources.lookup(ISwitch.class, "SW2");

    private volatile boolean cancelled = false;

    private CollectThread cthread = new CollectThread();
    private TransmitThread tthread = new TransmitThread();

    private boolean blink0 = false;

    private ITriColorLED led0 = leds.getLED(0);
    private ITriColorLED led7 = leds.getLED(7);

    private Vector v1 = new Vector();
    private Vector v2 = new Vector();
    private Vector v3 = new Vector();

    public void blink0() {
        blink0 = !blink0;
        if (blink0) {
            led0.setOff();
        } else {
            led0.setOn();
        }
    }

    class CollectThread extends Thread {

        public void run() {
            while (!cancelled) {
                try {
                    led0.setColor(LEDColor.BLUE);
                    led0.setOn();
                    System.out.println("Collector: Preparing radiogram connection ... ");
                    RadiogramConnection conn = (RadiogramConnection) Connector.open("radiogram://:100");

                    System.out.println("Collector: Radiogram connection prepared ... ");

                    Datagram dg = conn.newDatagram(conn.getMaximumLength());
                    Datagram dgreply = conn.newDatagram(conn.getMaximumLength());

                    try {
                        led0.setColor(LEDColor.GREEN);
//                        System.out.println("Collector: Waiting for data ... ");
                        conn.receive(dg);

                        blink0();

                        String question = dg.readUTF();
                        blink0();
//                        System.out.println("Collector: Client sent: " + question);

                        int pos = question.indexOf(',');
                        String address = question.substring(0, pos);
                        String value = question.substring(pos + 1);

//                        System.out.println("Collector:  address = " + address);
//                        System.out.println("Collector:  value   = " + value);

                        if (address.equalsIgnoreCase("0a00.020f.0000.1001")) {
                            v1.addElement(value);
                        } else if (address.equalsIgnoreCase("0a00.020f.0000.1002")) {
                            v2.addElement(value);
                        } else if (address.equalsIgnoreCase("0a00.020f.0000.1003")) { // 
                            v3.addElement(value);
                        } else {
                            System.out.println("++++++++++++++++ unknown IEEE address!! +++++++++++++++++");
                        }

//                        dgreply.reset(); // reset stream pointer                                                
//                        dgreply.setAddress(dg);
//                        blink0();
//                        dgreply.writeUTF("ok!");
//                        blink0();
//                        conn.send(dgreply);
//                        blink0();
                    } catch (NoRouteException e) {
                        System.out.println("No route to " + dgreply.getAddress());
                    } finally {
                        conn.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            Utils.sleep(1000);
        }
    }

    class TransmitThread extends Thread {

        public void run() {
            Utils.sleep(500);
            
            while (!cancelled) {
                Utils.sleep(2000);

                if (led7.isOn()) {
                    led7.setOff();
                } else {
                    led7.setColor(LEDColor.GREEN);
                    led7.setOn();
                }

                if (!v1.isEmpty() && !v2.isEmpty() && !v3.isEmpty()) {
                    String x1 = (String) v1.elementAt(0);
                    String x2 = (String) v2.elementAt(0);
                    String x3 = (String) v3.elementAt(0);

                    v1.removeElementAt(0);
                    v2.removeElementAt(0);
                    v3.removeElementAt(0);

                    int aces = 0;
                    if (x1.equals("1")) {
                        aces++;
                    }
                    if (x2.equals("1")) {
                        aces++;
                    }
                    if (x3.equals("1")) {
                        aces++;
                    }

                    String result = null;
                    if (aces >= 2) {
                        result = "1";
                    } else {
                        result = "0";
                    }

                    System.out.println("************************* Collector: transmitthread: " + x1 + x2 + x3 + " : " + result);

                    try {
                        RadiogramConnection conn = (RadiogramConnection) Connector.open("radiogram://0a00.020f.0000.1005:103");

                        System.out.println("Collector: connected ... ");

                        Datagram dg = conn.newDatagram(conn.getMaximumLength());

                        try {
                            System.out.println("Collector: sending result ... ");
                            dg.writeUTF(result);
                            conn.send(dg);
                        } catch (NoRouteException e) {
                            System.out.println("No route to 0a00.020f.0000.1005:103");
                        } finally {
                            conn.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    System.out.println("************************* missing data");
                }

            }
        }
    }

    protected void startApp() throws MIDletStateChangeException {

        System.out.println("Starting threads ... ");

        sw1.addISwitchListener(this);
        sw2.addISwitchListener(this);

        cthread.start();
        tthread.start();

        try {
            cthread.join();
            tthread.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void pauseApp() {
        // This will never be called by the Squawk VM
    }

    protected void destroyApp(boolean arg0) throws MIDletStateChangeException {
        // Only called if startApp throws any exception other than MIDletStateChangeException
    }

    public void switchReleased(SwitchEvent evt) {
        if (evt.getSwitch() == sw1) {
            if (cancelled) {
                cthread = new CollectThread();
                tthread = new TransmitThread();

                cthread.start();
                tthread.start();

                cancelled = false;
            }
        } else {
            cancelled = true;
        }
    }

    public void switchPressed(SwitchEvent evt) {
    }
}
