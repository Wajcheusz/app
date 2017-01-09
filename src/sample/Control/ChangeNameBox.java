package sample.Control;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.LinkedHashMap;

/**
 * Created by E6420 on 2017-01-09.
 */
public class ChangeNameBox {


    Controller controller;
    private String changedName;


    public void init(Controller controller){
        this.controller = controller;
    }


    public void display(String title, String message){
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);
        Label label = new Label();
        label.setText(message);

        Button yesButton = new Button("Zmień nazwę");
        Button noButton = new Button("Anuluj");
        TextField textField = new TextField();
        textField.setText(changedName);

        yesButton.setOnAction(event -> {
            changedName = textField.getText();
            window.close();
            //CommPort commPort = null;
        });

        noButton.setOnAction(event -> {

            window.close();
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, textField, yesButton, noButton);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }

    public String getChangedName() {
        return changedName;
    }

    public void setChangedName(String changedName) {
        this.changedName = changedName;
    }
}
