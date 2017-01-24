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
import sample.Control.Controller;

import java.util.*;

/**
 * Created by E6420 on 2017-01-08.
 */

public class PlayerTimeBox {

    ComboBox combobox = new ComboBox();

    Controller controller;
    LinkedHashMap timesMap = new LinkedHashMap<String, Integer>(); //keeps the keys in order they were inserted
    private int selectedSpeed = 1;

    public void init(Controller controller){
        this.controller = controller;
    }


    public void display(String title, String message){
        combobox.getItems().clear();
        timesMap.put("Rzeczywista", 1);
        timesMap.put("2 razy szybciej", 2);
        timesMap.put("5 razy szybciej", 5);
        timesMap.put("10 razy szybciej", 10);
        timesMap.put("Pokaż wszystko", 25);
        //timesMap.put("Pokaż wszystko", 1000);

        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);
        Label label = new Label();
        label.setText(message);

        Button yesButton = new Button("Wybierz");
        //Button noButton = new Button("Anuluj");



        combobox.setPromptText("Wybierz prędkość odtwarzania");
        combobox.getItems().addAll(timesMap.keySet());

        yesButton.setOnAction(event -> {
            System.out.println(combobox.getValue());
            selectedSpeed = (int)timesMap.get(combobox.getValue());
            window.close();
            //CommPort commPort = null;
        });

//        noButton.setOnAction(event -> {
//
//            window.close();
//        });

        VBox layout = new VBox(10);
        //layout.getChildren().addAll(label, combobox, yesButton, noButton);
        layout.getChildren().addAll(label, combobox, yesButton);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }

    public int getSelectedSpeed() {
        return selectedSpeed;
    }

    public void setSelectedSpeed(int selectedSpeed) {
        this.selectedSpeed = selectedSpeed;
    }
}

