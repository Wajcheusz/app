package sample.Boxes;

import gnu.io.CommPortIdentifier;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.Control.*;

import java.util.Enumeration;
import java.util.HashMap;

/**
 * Created by Mateusz.Blaszczak on 2017-01-07.
 */
public class ConnectBox {
    static boolean connected;
    static ComboBox combobox = new ComboBox();
    private static HashMap portMap = new HashMap();
    private static CommPortIdentifier selectedPortIdentifier = null;
    private static String selectedPort = null;
    static sample.Control.Communicator communicator = null;
    static Controller controller;

    public static CommPortIdentifier getSelectedPortIdentifier() {
        return selectedPortIdentifier;
    }

    public static void setSelectedPortIdentifier(CommPortIdentifier selectedPortIdentifier) {
        ConnectBox.selectedPortIdentifier = selectedPortIdentifier;
    }

    public void init(Controller controller){
        this.controller = controller;
    }

    private static void showPorts(){
        combobox.getItems().clear();
        CommPortIdentifier serialPortId;
        Enumeration enumComm;

        enumComm = CommPortIdentifier.getPortIdentifiers();
        while (enumComm.hasMoreElements()) {
            serialPortId = (CommPortIdentifier) enumComm.nextElement();
            if (serialPortId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                portMap.put(serialPortId.getName(), serialPortId);
                combobox.getItems().add(serialPortId.getName());
            }
        }
    }

    public static boolean display(String title, String message){
        portMap.clear();
        showPorts();
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);
        Label label = new Label();
        label.setText(message);

        Button yesButton = new Button("Połącz");
        Button noButton = new Button("Anuluj");

        combobox.setPromptText("Wybierz port");


        yesButton.setOnAction(event -> {
            System.out.println(combobox.getValue());
            connected = true;
            selectedPort = combobox.getValue().toString();
            selectedPortIdentifier = (CommPortIdentifier)portMap.get(selectedPort);

            //todo wydziel do innej funkcji
            communicator = new sample.Control.Communicator(selectedPortIdentifier, controller);
            communicator.connect();
            if(communicator.getConnected() && communicator.initIOStream()) {
                communicator.initListener();
                controller.getConnectButton().setDisable(true);
                controller.getDisconnectButton().setDisable(false);
                controller.getObserwujButton().setDisable(false);
            }
            window.close();
        });

        noButton.setOnAction(event -> {
            connected = false;
            controller.getDisconnectButton().setDisable(true);
            controller.getConnectButton().setDisable(false);
            controller.getObserwujButton().setDisable(true);
            window.close();
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, combobox, yesButton, noButton);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();

        return connected;
    }
}
