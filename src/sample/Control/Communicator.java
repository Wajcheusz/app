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
    public static String temporary = "";
    private CommPortIdentifier selectedPortIdentifier = null;
    private SerialPort serialPort = null;
    private InputStream input = null;
    private OutputStream output = null;
    private boolean bConnected = false;
//    static final int TIMEOUT = 2000;
//    static final int SPACE_ASCII = 32;
//    static final int DASH_ASCII = 45;
    static final int NEW_LINE_ASCII = 10;
//    public static String logText = "";
//    private static String selectedPort = null;
    public static CommPort commPort = null;
    Controller controller;

    public Communicator(CommPortIdentifier selectedPortIdentifier, Controller controller) {
        this.selectedPortIdentifier = selectedPortIdentifier;
        this.controller = controller;
    }

    public void connect() {
        try {
            commPort = selectedPortIdentifier.open("Stanowisko laboratoryjne", 1000);
            serialPort = (SerialPort)commPort;
            setConnected(true);
        } catch (PortInUseException var4) {
            controller.getLogger().clear();
            controller.getLogger().appendText("Wybrany port jest zajęty. (" + var4.toString() + ")");
//            logText = selectedPort + " jest zajęty. (" + var4.toString() + ")";
//            System.out.println(logText + "\n");
        } catch (Exception var5) {
            controller.getLogger().clear();
            controller.getLogger().appendText("Nie udało się otworzyć portu (" + var5.toString() + ")");
//            logText = "Nie udało się otworyzć portu " + selectedPort + "(" + var5.toString() + ")";
//            System.out.println(logText + "\n");
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
            controller.getLogger().clear();
            controller.getLogger().appendText("Nie udało się otworzyć (" + var3.toString() + ")");
//            logText = "I/O Streams failed to open. (" + var3.toString() + ")";
//            System.out.println(logText + "\n");
            return successful;
        }
    }

    public void initListener() {
        try {
            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);
        } catch (TooManyListenersException var2) {
            controller.getLogger().clear();
            controller.getLogger().appendText("Zbyt dużo listenerów. (" + var2.toString() + ")");
//            this.logText = "Too many listeners. (" + var2.toString() + ")";
//            System.out.println(this.logText + "\n");
        }
    }

    public final boolean getConnected() {
        return this.bConnected;
    }

    public void setConnected(boolean bConnected) {
        this.bConnected = bConnected;
    }

    String liczba = "";
    public void serialEvent(SerialPortEvent evt) {

        if (evt.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            try {
                byte singleData = (byte)input.read();

                if (singleData != NEW_LINE_ASCII) {
//                    logText = new String(new byte[] {singleData});
//                    liczba = liczba + logText;
                    liczba = liczba + new String(new byte[] {singleData});
                } else {
                    controller.getLogger().clear();
                    //controller.getLogger().appendText("Cliczba: " + liczba);
                    //System.out.print("Cliczba: " + liczba);
                    temporary = liczba;
                    //controller.getLogger().appendText("Cliczba: " + liczba);
                    //System.out.println("Ctemporary: " + temporary);
                    //System.out.print("\n");
                    liczba = "";
                }
            }
            catch (Exception e) {
                controller.getLogger().clear();
                controller.getLogger().appendText("Nie udało się odczytać danych. (" + e.toString() + ")");
            }
        }
    }
}

