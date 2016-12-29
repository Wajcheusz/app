package sample.Control;

import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.CheckBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleGroup;
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
        skalowanie();
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
//    @FXML private CheckBox connectPoints = new CheckBox();
    @FXML private RadioMenuItem pointsChart = new RadioMenuItem();
    @FXML private RadioMenuItem lineChart = new RadioMenuItem();
    @FXML private RadioMenuItem pointsAndLineChart = new RadioMenuItem();

    @FXML private ToggleGroup skalowanie = new ToggleGroup();
    @FXML private RadioMenuItem skalowanieAutomatyczne = new RadioMenuItem();
    @FXML private RadioMenuItem skalowanie30Sekund = new RadioMenuItem();
    @FXML private RadioMenuItem skalowanie1Minuta = new RadioMenuItem();
    @FXML private RadioMenuItem skalowanie2Minuty = new RadioMenuItem();
    @FXML private RadioMenuItem skalowanie5Minut = new RadioMenuItem();
    @FXML private RadioMenuItem skalowanie10Minut = new RadioMenuItem();
    @FXML private RadioMenuItem skalowanie20Minut = new RadioMenuItem();
    @FXML private RadioMenuItem skalowanie40Minut = new RadioMenuItem();
    @FXML private RadioMenuItem skalowanie60Minut = new RadioMenuItem();

    @FXML private MenuItem oserwojPrzebiegItem = new MenuItem();
    @FXML private MenuItem nagrajPrzebiegItem = new MenuItem();
    @FXML private MenuItem odtworzPrzebiegItem = new MenuItem();
    private Chartek chartek = new Chartek();
    @FXML private XYChart<Number, Number> XYChart = chartek.getXYChart();
    public static boolean nagrajPrzebiegClicked = false;
    //public AreaChart areaChart2 = new AreaChart<Number, Number>(new NumberAxis(5, 10, 15), new NumberAxis(5, 10, 15));

    @FXML private void skalowanie(){
        skalowanieAutomatyczne.setUserData(0);
        skalowanie30Sekund.setUserData(30);
        skalowanie1Minuta.setUserData(60);
        skalowanie2Minuty.setUserData(120);
        skalowanie5Minut.setUserData(300);
        skalowanie10Minut.setUserData(600);
        skalowanie20Minut.setUserData(1200);
        skalowanie40Minut.setUserData(2400);
        skalowanie60Minut.setUserData(3600);
    }

    @FXML private void checkboxSelected(){
        System.out.println("KLIKNIETO");
        chartek.getDataQ().clear();
    }

    @FXML private void checkbox2Selected(){
        chartek.getDataQ2().clear();
    }

    @FXML private void checkbox3Selected(){
        chartek.getDataQ3().clear();
    }

    @FXML private void checkbox4Selected(){
        chartek.getDataQ4().clear();
    }

    @FXML private void checkbox5Selected(){
        chartek.getDataQ5().clear();
    }

    @FXML private void checkbox6Selected(){
        chartek.getDataQ6().clear();
    }

    @FXML private void odtworzPrzebiegClicked(){

        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Pliki CSV (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(Main.stage);

        chartek.Start();
        chartek.createChart(file);
        borderPane.setCenter(chartek.getXYChart());
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
        chartek.Start();
        chartek.createRealtimeChart();
        borderPane.setCenter(chartek.getXYChart());
        Main.communicator.connect();
        if(Main.communicator.getConnected() && Main.communicator.initIOStream()) {
            Main.communicator.initListener();
        }
    }

    @FXML private void stopClicked(){
        chartek.stop();
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

    public Boolean getPointsSelection(){
        return pointsChart.isSelected();
    }

    public Boolean getLineSelection(){
        return lineChart.isSelected();
    }

    public Boolean getointsAndLineSelection(){
        return pointsAndLineChart.isSelected();
    }

    public ToggleGroup getSkalowanie() {
        return skalowanie;
    }

    //
//    public RadioMenuItem getSkalowanie30Sekund() {
//        return skalowanie30Sekund;
//    }
//
//    public RadioMenuItem getSkalowanie1Minuta() {
//        return skalowanie1Minuta;
//    }
//
//    public RadioMenuItem getSkalowanie2Minuty() {
//        return skalowanie2Minuty;
//    }
//
//    public RadioMenuItem getSkalowanie5Minut() {
//        return skalowanie5Minut;
//    }
//
//    public RadioMenuItem getSkalowanie10Minut() {
//        return skalowanie10Minut;
//    }
//
//    public RadioMenuItem getSkalowanie20Minut() {
//        return skalowanie20Minut;
//    }
//
//    public RadioMenuItem getSkalowanie40Minut() {
//        return skalowanie40Minut;
//    }
//
//    public RadioMenuItem getSkalowanie60Minut() {
//        return skalowanie60Minut;
//    }
//
//    public RadioMenuItem getSkalowanieAutomatyczne() {
//        skalowanie.
//        return skalowanieAutomatyczne;
//    }
}
