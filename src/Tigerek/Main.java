package Tigerek;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

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
        Button button1 = new Button("Start");
        button1.getStyleClass().add("my_customLabel");
        Button button2 = new Button("Nagraj przebieg");
        Button button3 = new Button("View");

        topMenu.getChildren().addAll(button1, button2, button3);

        VBox leftMenu = new VBox();
        Button button4 = new Button("D");
        Button button5 = new Button("E");
        Button button6 = new Button("F");

        leftMenu.getChildren().addAll(button4, button5, button6);

        //BorderPane borderPane = new BorderPane();

        GridPane gridPane = new GridPane();
        //gridPane.add(topMenu, 0, 0);
        //gridPane.add(leftMenu, 0, 1);
        //gridPane.autosize();
        //gridPane.setPrefSize(1200, 800);
        gridPane.setGridLinesVisible(true);
        //gridPane.getColumnConstraints().add(new ColumnConstraints(200)); // column 0 is 100 wide
        //gridPane.getColumnConstraints().add(new ColumnConstraints(200)); // column 1 is 200 wide


        BorderPane borderPane = new BorderPane();
        borderPane.setLayoutX(10);
        borderPane.setLayoutY(20);
        borderPane.setPrefSize(800, 600);
        borderPane.setTop(topMenu);
        borderPane.setLeft(leftMenu);


        Chartek chartek = new Chartek("first_chart");

        button1.setOnAction(event ->{

            chartek.createRealtimeChart();

            gridPane.add(chartek.areaChart, 2, 2);
            //borderPane.setRight(chartek.areaChart);
            communicator.connect();
            if(communicator.getConnected() && communicator.initIOStream()) {
                communicator.initListener();
            }
        });
        button2.setOnAction(event ->{
            try {
                String nazwaPliku = "test.csv";
                Path sciezka = Paths.get(nazwaPliku);
                Files.write(sciezka, chartek.getOut());
            } catch (IOException ex) {
                System.out.println("Nie mogę zapisać pliku!");
            }
        });
        button3.setOnAction(event ->{
            Chartek chartek1 = new Chartek("first_chart");
            Chartek chartek2 = new Chartek("first_chart");
            Chartek chartek3 = new Chartek("first_chart");
            Chartek chartek4 = new Chartek("first_chart");
            Chartek chartek5 = new Chartek("first_chart");
            Chartek chartek6 = new Chartek("first_chart");
            chartek1.createChart();
            chartek2.createChart();
            chartek3.createChart();
            chartek4.createChart();
            chartek5.createChart();
            chartek6.createChart();
            chartek.createChart();
            gridPane.add(chartek1.areaChart, 0, 0);
            gridPane.add(chartek2.areaChart, 0, 1);
            gridPane.add(chartek3.areaChart, 0, 2);
            gridPane.add(chartek4.areaChart, 1, 0);
            gridPane.add(chartek5.areaChart, 1, 1);
            gridPane.add(chartek6.areaChart, 1, 2);
//            communicator.connect();
//            if(communicator.getConnected() && communicator.initIOStream()) {
//                communicator.initListener();
//            }
        });
        borderPane.setCenter(gridPane);
        Scene scene = new Scene(borderPane);
        scene.getStylesheets().add(Main.class.getResource("myStyle.css").toExternalForm());
        //primaryStage.setScene(new Scene((borderPane)));
        primaryStage.setScene(scene);
    }
}
