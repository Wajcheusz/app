package sample.Control;

import gnu.io.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

/**
 * Created by E6420 on 2016-11-19.
 */

public class Communicator implements SerialPortEventListener {
    public static String x = "30";
    //public static String temp = "30";
    public static String temporary = "40.2 25.5 27.5 28.5 21.5 29.5";
    private Enumeration ports = null;
    private HashMap portMap = new HashMap();
    private CommPortIdentifier selectedPortIdentifier = null;
    private SerialPort serialPort = null;
    private InputStream input = null;
    private OutputStream output = null;
    private boolean bConnected = false;
    //    static final int TIMEOUT = 2000;
//    static final int SPACE_ASCII = 32;
//    static final int DASH_ASCII = 45;
    static final int NEW_LINE_ASCII = 10;
    public static String logText = "";
    private static String selectedPort = null;

    public Communicator(CommPortIdentifier selectedPortIdentifier, String selectedPort) {
        this.selectedPortIdentifier = selectedPortIdentifier;
        this.selectedPort = selectedPort;
    }

    public void showPorts(){
        CommPortIdentifier serialPortId;
        Enumeration enumComm;

        enumComm = CommPortIdentifier.getPortIdentifiers();
        while (enumComm.hasMoreElements()) {
            serialPortId = (CommPortIdentifier) enumComm.nextElement();
            if (serialPortId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                System.out.println(serialPortId);
                System.out.println(serialPortId.getName());
            }
        }
    }

    public void connect() {
//        CommPortIdentifier serialPortId = null;
//        Enumeration enumComm;
//        List<CommPortIdentifier> x = new ArrayList<CommPortIdentifier>();
//
//
//        enumComm = CommPortIdentifier.getPortIdentifiers();
//        while (enumComm.hasMoreElements()) {
//            serialPortId = (CommPortIdentifier) enumComm.nextElement();
//            if (serialPortId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
//                x.add(serialPortId);
//                System.out.println(serialPortId.getName());
//            }
//        }
//
//        String selectedPort = "COM11";//(String)serialPortId.getName();//"COM11";//(String)this.window.cboxPorts.getSelectedItem();
//        selectedPortIdentifier = (CommPortIdentifier)portMap.get(selectedPort);
        CommPort commPort = null;

        try {
            commPort = selectedPortIdentifier.open("Stanowisko laboratoryjne", 1000);
//            commPort = x.get(4).open("Stanowisko laboratoryjne", 1000);
            serialPort = (SerialPort)commPort;
            setConnected(true);
            //this.logText = selectedPort + " opened successfully.";
            //System.out.println(this.logText);
        } catch (PortInUseException var4) {
            logText = selectedPort + " is in use. (" + var4.toString() + ")";
            System.out.println(logText + "\n");
        } catch (Exception var5) {
            logText = "Failed to open " + selectedPort + "(" + var5.toString() + ")";
            System.out.println(logText + "\n");
        }

    }

    public boolean initIOStream() {
        boolean successful = false;

        try {
            input = serialPort.getInputStream();
            output = serialPort.getOutputStream();
            successful = true;
            return successful;
        } catch (IOException var3) {
            logText = "I/O Streams failed to open. (" + var3.toString() + ")";
            System.out.println(logText + "\n");
            return successful;
        }
    }

    public void initListener() {
        try {
            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);
        } catch (TooManyListenersException var2) {
            this.logText = "Too many listeners. (" + var2.toString() + ")";
            System.out.println(this.logText + "\n");
        }

    }

    public final boolean getConnected() {
        return this.bConnected;
    }

    public void setConnected(boolean bConnected) {
        this.bConnected = bConnected;
    }

    String liczba = "";

    static StringBuffer z;
    public void serialEvent(SerialPortEvent evt) {

        if (evt.getEventType() == SerialPortEvent.DATA_AVAILABLE)
        {
            try
            {
                byte singleData = (byte)input.read();

                if (singleData != NEW_LINE_ASCII)
                {
                    logText = new String(new byte[] {singleData});
                    x=logText;
                    liczba = liczba + x;
                }
                else
                {
//                    z = new StringBuffer(liczba);
//                    z.substring(1,4);
//                    temporary = z.toString();
//                    System.out.print(temporary);
                    System.out.print("Cliczba: " + liczba);
                    temporary = liczba;
                    System.out.println("Ctemporary: " + temporary);
                    System.out.print("\n");
                    liczba = "";
                }
            }
            catch (Exception e)
            {
                logText = "Failed to read data. (" + e.toString() + ")";
            }
        }
    }
}

