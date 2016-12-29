package sample.Control;

import javafx.scene.chart.AreaChart;
import javafx.scene.control.CheckBox;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import sample.Chart.Chartek;
import sample.Main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Controller {

    public BorderPane borderPane = new BorderPane();
    public CheckBox checkobox2 = new CheckBox();
    public MenuItem oserwojPrzebiegItem = new MenuItem();
    public MenuItem nagrajPrzebiegItem = new MenuItem();
    public MenuItem odtworzPrzebiegItem = new MenuItem();
    public Chartek chartek = new Chartek();
    public AreaChart<Number, Number> areaChart = chartek.getAreaChart();
    public static boolean nagrajPrzebiegClicked = false;
    //public AreaChart areaChart2 = new AreaChart<Number, Number>(new NumberAxis(5, 10, 15), new NumberAxis(5, 10, 15));

    public void odtworzPrzebiegClicked(){

        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Pliki CSV (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(Main.stage);


        chartek.createChart(file);
        borderPane.setCenter(chartek.getAreaChart());
    }

    public void nagrajPrzebiegClicked(){
        try {
            String nazwaPliku = "test5.csv";
            Path sciezka = Paths.get(nazwaPliku);
            Files.write(sciezka, chartek.getOut());
            nagrajPrzebiegClicked = true;
        } catch (IOException ex) {
            System.out.println("Nie mogę zapisać pliku!");
        }
    }

    public void obserwojPrzebiegClicked(){
        chartek.createRealtimeChart();
        borderPane.setCenter(chartek.getAreaChart());
        Main.communicator.connect();
        if(Main.communicator.getConnected() && Main.communicator.initIOStream()) {
            Main.communicator.initListener();
        }
    }

    public void zapiszPrzebiegClicked(){
//        File file = chooser.getSelectedFile();
//        if (FilenameUtils.getExtension(file.getName()).equalsIgnoreCase("xml")) {
//            // filename is OK as-is
//        } else {
//            file = new File(file.toString() + ".xml");  // append .xml if "foo.jpg.xml" is OK
//            file = new File(file.getParentFile(), FilenameUtils.getBaseName(file.getName())+".xml"); // ALTERNATIVELY: remove the extension (if any) and replace it with ".xml"
//        }




        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Pliki CSV (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showSaveDialog(Main.stage);
        if(file != null){
            SaveFile(chartek.getOut(), file);
        }
    }

    private void SaveFile(ArrayList<String> list, File file){
        try {
            FileWriter fileWriter = null;

            fileWriter = new FileWriter(file);

            StringBuilder listString = new StringBuilder();

            for (String s : list)
                listString.append(s+"\n");
            fileWriter.write(listString.toString());

            fileWriter.close();
        } catch (IOException ex) {
            System.out.println(ex);
            //Logger.getLogger(JavaFX_Text.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
