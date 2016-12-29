package sample.Control;

import javafx.fxml.FXML;
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

    @FXML public void initialize() {
        System.out.println("Application started");
        chartek.init(this);
//        tab1Controller.init(this);
//        tab2Controller.init(this);
    }

    @FXML private BorderPane borderPane = new BorderPane();
    @FXML private CheckBox checkbox = new CheckBox();
    @FXML private CheckBox checkbox2 = new CheckBox();
    @FXML private CheckBox checkbox3 = new CheckBox();
    @FXML private CheckBox checkbox4 = new CheckBox();
    @FXML private CheckBox checkbox5 = new CheckBox();
    @FXML private CheckBox checkbox6 = new CheckBox();
    @FXML private MenuItem oserwojPrzebiegItem = new MenuItem();
    @FXML private MenuItem nagrajPrzebiegItem = new MenuItem();
    @FXML private MenuItem odtworzPrzebiegItem = new MenuItem();
    private Chartek chartek = new Chartek();
    @FXML private AreaChart<Number, Number> areaChart = chartek.getAreaChart();
    public static boolean nagrajPrzebiegClicked = false;
    //public AreaChart areaChart2 = new AreaChart<Number, Number>(new NumberAxis(5, 10, 15), new NumberAxis(5, 10, 15));


//    @FXML private void checkboxSelected(){
//        //checkbox.setSelected(!checkbox.isSelected());
//        //System.out.println("1 selected" + checkbox.isSelected());
//    }
//
//    @FXML private void checkbox2Selected(){
//        //checkbox2.setSelected(!checkbox2.isSelected());
//        //System.out.println("2 selected" + checkbox2.isSelected());
//    }
//
//    @FXML private void checkbox3Selected(){
//        //checkbox3.setSelected(!checkbox3.isSelected());
//    }
//
//    @FXML private void checkbox4Selected(){
//        //checkbox4.setSelected(!checkbox4.isSelected());
//    }
//
//    @FXML private void checkbox5Selected(){
//        //checkbox5.setSelected(!checkbox5.isSelected());
//    }
//
//    @FXML private void checkbox6Selected(){
//        //checkbox6.setSelected(!checkbox6.isSelected());
//    }

    @FXML private void odtworzPrzebiegClicked(){

        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Pliki CSV (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(Main.stage);


        chartek.createChart(file);
        borderPane.setCenter(chartek.getAreaChart());
    }

    @FXML private void nagrajPrzebiegClicked(){
        try {
            String nazwaPliku = "test5.csv";
            Path sciezka = Paths.get(nazwaPliku);
            Files.write(sciezka, chartek.getOut());
            nagrajPrzebiegClicked = true;
        } catch (IOException ex) {
            System.out.println("Nie mogę zapisać pliku!");
        }
    }

    @FXML private void obserwojPrzebiegClicked(){
        chartek.createRealtimeChart();
        borderPane.setCenter(chartek.getAreaChart());
        Main.communicator.connect();
        if(Main.communicator.getConnected() && Main.communicator.initIOStream()) {
            Main.communicator.initListener();
        }
    }

    @FXML private void zapiszPrzebiegClicked(){
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

    public Boolean getCheckboxSelection() {
        return checkbox.isSelected();
    }

    public Boolean getCheckbox2Selection(){
        return checkbox2.isSelected();
    }

    public Boolean getCheckbox3Selection(){
        return checkbox3.isSelected();
    }

    public Boolean getCheckbox4Selection(){
        return checkbox4.isSelected();
    }

    public Boolean getCheckbox5Selection(){
        return checkbox5.isSelected();
    }

    public Boolean getCheckbox6Selection(){
        return checkbox6.isSelected();
    }
}
