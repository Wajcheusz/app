package sample.Control;

import javafx.fxml.FXML;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import sample.Chart.Chartek;
import sample.Main;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Controller {

    @FXML
    public void initialize() {
        System.out.println("Application started");
        skalowanie();
        nagrajButton.setDisable(true);
        zapiszButton.setDisable(true);
        resetButton.setDisable(true);
        disconnectButton.setDisable(true);
        obserwujButton.setDisable(true);
//        checkbox.setText(chartek.getSeries().getName());
//        checkbox2.setText(chartek.getSeries2().getName());
//        checkbox3.setText(chartek.getSeries3().getName());
//        checkbox4.setText(chartek.getSeries4().getName());
//        checkbox5.setText(chartek.getSeries5().getName());
//        checkbox6.setText(chartek.getSeries6().getName());
//        checkboxRightClicked();
    }

    @FXML
    private BorderPane borderPane = new BorderPane();
    @FXML
    private CheckBox checkbox = new CheckBox();
    @FXML
    private CheckBox checkbox2 = new CheckBox();
    @FXML
    private CheckBox checkbox3 = new CheckBox();
    @FXML
    private CheckBox checkbox4 = new CheckBox();
    @FXML
    private CheckBox checkbox5 = new CheckBox();
    @FXML
    private CheckBox checkbox6 = new CheckBox();
    @FXML
    private RadioMenuItem pointsChart = new RadioMenuItem();
    @FXML
    private RadioMenuItem lineChart = new RadioMenuItem();
    @FXML
    private RadioMenuItem pointsAndLineChart = new RadioMenuItem();

    @FXML
    private ToggleGroup skalowanie = new ToggleGroup();

    @FXML
    private Menu opcjeMenu = new Menu();
    @FXML
    private Menu skalowanieMenu = new Menu();

    @FXML
    private RadioMenuItem skalowanieAutomatyczne = new RadioMenuItem();
    @FXML
    private RadioMenuItem skalowanie30Sekund = new RadioMenuItem();
    @FXML
    private RadioMenuItem skalowanie1Minuta = new RadioMenuItem();
    @FXML
    private RadioMenuItem skalowanie2Minuty = new RadioMenuItem();
    @FXML
    private RadioMenuItem skalowanie5Minut = new RadioMenuItem();
    @FXML
    private RadioMenuItem skalowanie10Minut = new RadioMenuItem();
    @FXML
    private RadioMenuItem skalowanie20Minut = new RadioMenuItem();
    @FXML
    private RadioMenuItem skalowanie40Minut = new RadioMenuItem();
    @FXML
    private RadioMenuItem skalowanie60Minut = new RadioMenuItem();
    @FXML
    private Button zoom = new Button();
    @FXML
    private Button zoomOut = new Button();
    @FXML
    private Button left = new Button();
    @FXML
    private Button right = new Button();
    @FXML
    private Button resetZoomButton = new Button();

    @FXML
    private Button obserwujButton = new Button();
    @FXML
    private Button odtworzButton = new Button();
    @FXML
    private Button nagrajButton = new Button();
    @FXML
    private Button zapiszButton = new Button();
    @FXML
    private Button resetButton = new Button();
    @FXML
    private Button connectButton = new Button();
    @FXML
    private Button disconnectButton = new Button();


    @FXML
    private MenuItem oserwojPrzebiegItem = new MenuItem();
    @FXML
    private MenuItem nagrajPrzebiegItem = new MenuItem();
    @FXML
    private MenuItem odtworzPrzebiegItem = new MenuItem();
    private Chartek chartek = new Chartek();
    //private Chartek chartek = null;
    private ConnectBox connectBox = new ConnectBox();
    private PlayerTimeBox playerTimeBox = new PlayerTimeBox();
    //@FXML private XYChart<Number, Number> XYChart = chartek.getXYChart();
    public static boolean nagrajPrzebiegClicked = false;
    ChangeNameBox changeNameBox = new ChangeNameBox();

    @FXML
    private void connectButtonClicked() {
        connectBox.init(this);
        ConnectBox.display("Wybór portu COM", "Wybierz port COM z którym chcesz się połączyć");
    }

    @FXML
    private void disconnectButtonClicked() {
        connectButton.setDisable(false);
        disconnectButton.setDisable(true);
        obserwujButton.setDisable(true);
        nagrajButton.setDisable(true);
        odtworzButton.setDisable(false);
        resetButton.setDisable(true);
        chartek.stop();
        Communicator.commPort.close();
    }

    @FXML
    private void resetZoomButtonClicked() {
        chartek.clearKolejka();
    }


    @FXML
    private void leftButtonClicked() {
        chartek.getKolejka().add(3);

    }

    @FXML
    private void rightButtonClicked() {
        chartek.getKolejka().add(4);
    }

    @FXML
    private void zoomClicked() {
        chartek.getKolejka().add(2);
    }

    @FXML
    private void zoomOutClicked() {
        chartek.getKolejka().add(1);
    }


    @FXML
    private void skalowanie() {
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

//    private void checkboxRightClicked(){
//        checkbox.setOnAction(event -> {
//            if (event.getSource() == MouseButton.SECONDARY){
//                System.out.println("prawy");
//            } else if (event.getSource() == MouseButton.PRIMARY){
//                System.out.println("lewy");
//            }
//        });
//    }

    @FXML
    private void checkboxSelected(MouseEvent event) {
//        System.out.println("KLIKNIETO");
//        System.out.println(event.getButton().toString());
        if (event.getButton() == MouseButton.PRIMARY) {
            System.out.println("lewy");
            chartek.getDataQ().clear();
        } else if (event.getButton() == MouseButton.SECONDARY) {
            System.out.println("prawy");
            changeNameBox.setChangedName(checkbox.getText());
            changeNameBox.display("Zmiana nazwy", "Wpisz nową nazwę");
            checkbox.setText(changeNameBox.getChangedName());
            chartek.getSeries().setName(changeNameBox.getChangedName());
        }
    }

    @FXML
    private void checkbox2Selected(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            chartek.getDataQ2().clear();
        } else if (event.getButton() == MouseButton.SECONDARY) {
            changeNameBox.setChangedName(checkbox2.getText());
            changeNameBox.display("Zmiana nazwy", "Wpisz nową nazwę");
            checkbox2.setText(changeNameBox.getChangedName());
            chartek.getSeries2().setName(changeNameBox.getChangedName());
        }
    }

    @FXML
    private void checkbox3Selected(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            chartek.getDataQ3().clear();
        } else if (event.getButton() == MouseButton.SECONDARY) {
            changeNameBox.setChangedName(checkbox3.getText());
            changeNameBox.display("Zmiana nazwy", "Wpisz nową nazwę");
            checkbox3.setText(changeNameBox.getChangedName());
            chartek.getSeries3().setName(changeNameBox.getChangedName());
        }
    }

    @FXML
    private void checkbox4Selected(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            chartek.getDataQ4().clear();
        } else if (event.getButton() == MouseButton.SECONDARY) {
            changeNameBox.setChangedName(checkbox4.getText());
            changeNameBox.display("Zmiana nazwy", "Wpisz nową nazwę");
            checkbox4.setText(changeNameBox.getChangedName());
            chartek.getSeries4().setName(changeNameBox.getChangedName());
        }
    }

    @FXML
    private void checkbox5Selected(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            chartek.getDataQ5().clear();
        } else if (event.getButton() == MouseButton.SECONDARY) {
            changeNameBox.setChangedName(checkbox5.getText());
            changeNameBox.display("Zmiana nazwy", "Wpisz nową nazwę");
            checkbox5.setText(changeNameBox.getChangedName());
            chartek.getSeries5().setName(changeNameBox.getChangedName());
        }
    }

    @FXML
    private void checkbox6Selected(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            chartek.getDataQ6().clear();
        } else if (event.getButton() == MouseButton.SECONDARY) {
            changeNameBox.setChangedName(checkbox6.getText());
            changeNameBox.display("Zmiana nazwy", "Wpisz nową nazwę");
            checkbox6.setText(changeNameBox.getChangedName());
            chartek.getSeries6().setName(changeNameBox.getChangedName());
        }
    }

    @FXML
    private void odtworzPrzebiegClicked() {
        playerTimeBox.init(this);
        playerTimeBox.display("Wybór prędkości odtwarzania", "Wybierz prędkość z jaką chcesz odtworzyć wykres");


        //chartek = new Chartek();
        chartek.init(this);

        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Pliki CSV (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(Main.stage);
        if (file != null) {
            chartek.start();
            chartek.createChart(file, playerTimeBox.getSelectedSpeed());
            borderPane.setCenter(chartek.getXYChart());
            nagrajButton.setDisable(true);
            resetButton.setDisable(false);
            obserwujButton.setDisable(true);
            odtworzButton.setDisable(true);
        }//todo przy anuluj bedzie problem
    }

    @FXML
    private void nagrajPrzebiegClicked() {
        try {
            String nazwaPliku = "test5.csv";
            Path sciezka = Paths.get(nazwaPliku);
            Files.write(sciezka, chartek.getOut());
            nagrajPrzebiegClicked = true;
        } catch (IOException ex) {
            System.out.println("Nie mogę zapisać pliku!");
        }
        zapiszButton.setDisable(false);
        nagrajButton.setDisable(true);
    }

    @FXML
    private void obserwujPrzebiegClicked() {
        //chartek = new Chartek();
        chartek.init(this);
        chartek.start();
        chartek.createRealtimeChart();
        borderPane.setCenter(chartek.getXYChart());
        nagrajButton.setDisable(false);
        resetButton.setDisable(false);
        odtworzButton.setDisable(true);
        obserwujButton.setDisable(true);
    }

    @FXML
    private void resetClicked() {
        chartek.stop();
        nagrajButton.setDisable(true);
        obserwujButton.setDisable(false);
        odtworzButton.setDisable(false);
        resetButton.setDisable(true);
        zapiszButton.setDisable(true);
    }

    @FXML
    private void zapiszPrzebiegClicked() {

        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Pliki CSV (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showSaveDialog(Main.stage);
        if (file != null) {
            SaveFile(chartek.getOut(), file);
        }
    }

    private void SaveFile(ArrayList<String> list, File file) {
        try {
            FileWriter fileWriter = null;

            fileWriter = new FileWriter(file);

            StringBuilder listString = new StringBuilder();

            for (String s : list)
                listString.append(s + "\n");
            fileWriter.write(listString.toString());

            fileWriter.close();
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    public void setZoom(Button zoom) {
        this.zoom = zoom;
    }

    public Button getZoomOut() {
        return zoomOut;
    }

    public void setZoomOut(Button zoomOut) {
        this.zoomOut = zoomOut;
    }

    public Button getLeft() {
        return left;
    }

    public void setLeft(Button left) {
        this.left = left;
    }

    public Button getRight() {
        return right;
    }

    public void setRight(Button right) {
        this.right = right;
    }

    public Menu getOpcjeMenu() {
        return opcjeMenu;
    }

    public void setOpcjeMenu(Menu opcjeMenu) {
        this.opcjeMenu = opcjeMenu;
    }

    public Menu getSkalowanieMenu() {
        return skalowanieMenu;
    }

    public void setSkalowanieMenu(Menu skalowanieMenu) {
        this.skalowanieMenu = skalowanieMenu;
    }

    public Boolean getCheckboxSelection() {
        return checkbox.isSelected();
    }

    public Boolean getCheckbox2Selection() {
        return checkbox2.isSelected();
    }

    public Boolean getCheckbox3Selection() {
        return checkbox3.isSelected();
    }

    public Boolean getCheckbox4Selection() {
        return checkbox4.isSelected();
    }

    public Boolean getCheckbox5Selection() {
        return checkbox5.isSelected();
    }

    public Boolean getCheckbox6Selection() {
        return checkbox6.isSelected();
    }

    public Boolean getPointsSelection() {
        return pointsChart.isSelected();
    }

    public Boolean getLineSelection() {
        return lineChart.isSelected();
    }

    public Boolean getointsAndLineSelection() {
        return pointsAndLineChart.isSelected();
    }

    public ToggleGroup getSkalowanie() {
        return skalowanie;
    }

    public Button getZoom() {
        return zoom;
    }

    public void setZoomSelected(Button zoom) {
        this.zoom = zoom;
    }

    public Button getConnectButton() {
        return connectButton;
    }

    public Button getDisconnectButton() {
        return disconnectButton;
    }

    public Button getObserwujButton() {
        return obserwujButton;
    }

    public Button getOdtworzButton() {
        return odtworzButton;
    }

    public Button getResetButton() {
        return resetButton;
    }

    public CheckBox getCheckbox() {
        return checkbox;
    }

    public void setCheckbox(CheckBox checkbox) {
        this.checkbox = checkbox;
    }

    public CheckBox getCheckbox2() {
        return checkbox2;
    }

    public void setCheckbox2(CheckBox checkbox2) {
        this.checkbox2 = checkbox2;
    }

    public CheckBox getCheckbox3() {
        return checkbox3;
    }

    public void setCheckbox3(CheckBox checkbox3) {
        this.checkbox3 = checkbox3;
    }

    public CheckBox getCheckbox4() {
        return checkbox4;
    }

    public void setCheckbox4(CheckBox checkbox4) {
        this.checkbox4 = checkbox4;
    }

    public CheckBox getCheckbox5() {
        return checkbox5;
    }

    public void setCheckbox5(CheckBox checkbox5) {
        this.checkbox5 = checkbox5;
    }

    public CheckBox getCheckbox6() {
        return checkbox6;
    }

    public void setCheckbox6(CheckBox checkbox6) {
        this.checkbox6 = checkbox6;
    }
}
