package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.Control.Communicator;

public class Main extends Application {

    public static Stage stage;

    public static Communicator communicator;
    @Override
    public void start(Stage primaryStage) throws Exception{
        stage = primaryStage;
        communicator = new Communicator();
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
