package Tigerek;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Created by E6420 on 2016-11-30.
 */
public class Main extends Application {


    Communicator communicator;

//    public static void main(String[] args) {
//        launch(args);
//    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        init(primaryStage);
        primaryStage.show();
    }

    private void init(Stage primaryStage) {
        communicator = new Communicator();
        HBox topMenu = new HBox();
        Button button1 = new Button("File");
        Button button2 = new Button("Edit");
        Button button3 = new Button("View");
        button1.setOnAction(event ->{
            communicator.connect();
            if(communicator.getConnected() && communicator.initIOStream()) {
                communicator.initListener();
            }
        });
        topMenu.getChildren().addAll(button1, button2, button3);

        VBox leftMenu = new VBox();
        Button button4 = new Button("D");
        Button button5 = new Button("E");
        Button button6 = new Button("F");
        leftMenu.getChildren().addAll(button4, button5, button6);

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(topMenu);
        borderPane.setLeft(leftMenu);

        Chartek chartek = new Chartek();
        chartek.createChart();
        borderPane.setRight(chartek.sc);

        primaryStage.setScene(new Scene((borderPane)));
    }
}
