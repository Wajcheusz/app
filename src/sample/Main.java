package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.Control.Communicator;

public class Main extends Application {

    public static Stage stage;
    private static final int NUM_DATA_POINTS = 1000 ;

    @Override
    public void start(Stage primaryStage) throws Exception{
        stage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 1200, 840));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
