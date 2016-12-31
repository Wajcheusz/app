package sample.Control;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sample.Chart.Chartek;
import sample.Main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Controller {

    @FXML public void initialize() {
        //opcjeMenu.setDisable(true);
        System.out.println("Application started");
        chartek.init(this);
        //zoom.setDisable(true);
        //start1();
        skalowanie();
        nagrajButton.setDisable(true);
        zapiszButton.setDisable(true);
        resetButton.setDisable(true);
//        opcjeMenu.setDisable(false);
//        opcjeMenu.disableProperty();
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

    @FXML private Menu opcjeMenu = new Menu();
    @FXML private Menu skalowanieMenu = new Menu();

    @FXML private RadioMenuItem skalowanieAutomatyczne = new RadioMenuItem();
    @FXML private RadioMenuItem skalowanie30Sekund = new RadioMenuItem();
    @FXML private RadioMenuItem skalowanie1Minuta = new RadioMenuItem();
    @FXML private RadioMenuItem skalowanie2Minuty = new RadioMenuItem();
    @FXML private RadioMenuItem skalowanie5Minut = new RadioMenuItem();
    @FXML private RadioMenuItem skalowanie10Minut = new RadioMenuItem();
    @FXML private RadioMenuItem skalowanie20Minut = new RadioMenuItem();
    @FXML private RadioMenuItem skalowanie40Minut = new RadioMenuItem();
    @FXML private RadioMenuItem skalowanie60Minut = new RadioMenuItem();
    @FXML private Button zoom = new Button();
    @FXML private Button zoomOut = new Button();
    @FXML private Button left = new Button();
    @FXML private Button right = new Button();
    @FXML private Button resetZoomButton = new Button();

    @FXML private Button obserwujButton = new Button();
    @FXML private Button odtworzButton = new Button();
    @FXML private Button nagrajButton = new Button();
    @FXML private Button zapiszButton = new Button();
    @FXML private Button resetButton = new Button();


    @FXML private MenuItem oserwojPrzebiegItem = new MenuItem();
    @FXML private MenuItem nagrajPrzebiegItem = new MenuItem();
    @FXML private MenuItem odtworzPrzebiegItem = new MenuItem();
    private Chartek chartek = new Chartek();
    @FXML private XYChart<Number, Number> XYChart = chartek.getXYChart();
    public static boolean nagrajPrzebiegClicked = false;
    //public AreaChart areaChart2 = new AreaChart<Number, Number>(new NumberAxis(5, 10, 15), new NumberAxis(5, 10, 15));

//    @FXML private void zoomClicked(){
//        final Rectangle zoomRect = new Rectangle();
//        zoomRect.setManaged(false);
//        zoomRect.setFill(Color.LIGHTSEAGREEN.deriveColor(0, 1, 1, 0.5));
//        borderPane.getChildren().add(zoomRect);
//        //chartContainer.getChildren().add(zoomRect);
//
//        chartek.setUpZooming(zoomRect, chartek.getXYChart());
//        chartek.doZoom(zoomRect, chartek.getXYChart());
//    }


    @FXML private void resetZoomButtonClicked(){
        chartek.clearKolejka();
    }

    @FXML private void leftButtonClicked(){
        //chartek.setLeftClicked(true);
        //chartek.setMove(chartek.getMove()+1);
        chartek.getKolejka().add(3);

    }

    @FXML private void rightButtonClicked(){
        chartek.getKolejka().add(4);
        //chartek.setRightClicked(true);
        //chartek.setMove(chartek.getMove()-1);
    }

    @FXML private void zoomClicked(){
        chartek.getKolejka().add(2);
        //chartek.setZoom(chartek.getZoom()+1);
        //chartek.getKolejka().add(false);

        //chartek.setZoomClicked(true);
        //chartek.prepareTimeline2(2);
        //chartek.powieksz();
    }

    @FXML private void zoomOutClicked(){
        chartek.getKolejka().add(1);
        //chartek.setZoomOutClicked(true);
        //chartek.pomniejsz();
        //chartek.setZoom(chartek.getZoom()-1);
    }



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
        if(file != null){
            chartek.Start();
            chartek.createChart(file);
            borderPane.setCenter(chartek.getXYChart());
            nagrajButton.setDisable(true);
            resetButton.setDisable(false);
            obserwujButton.setDisable(true);
            odtworzButton.setDisable(true);
        }//todo przy anuluj bedzie problem
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
        zapiszButton.setDisable(false);
        nagrajButton.setDisable(true);
    }

    @FXML private void obserwujPrzebiegClicked(){
        chartek.Start();
        chartek.createRealtimeChart();
        borderPane.setCenter(chartek.getXYChart());
        Main.communicator.connect();
        if(Main.communicator.getConnected() && Main.communicator.initIOStream()) {
            Main.communicator.initListener();
        }
        nagrajButton.setDisable(false);
        resetButton.setDisable(false);
        odtworzButton.setDisable(true);
        obserwujButton.setDisable(true);
        //start1();
    }

    @FXML private void resetClicked(){
        chartek.stop();
        nagrajButton.setDisable(true);
        obserwujButton.setDisable(false);
        odtworzButton.setDisable(false);
        resetButton.setDisable(true);
        zapiszButton.setDisable(true);
    }

    @FXML private void startClicked(){
        //chartek.start();
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

    public Button getZoom() {
        return zoom;
    }

    public void setZoomSelected(Button zoom) {
        this.zoom = zoom;
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
//
//    private static final int NUM_DATA_POINTS = 1000 ;
//    public void start1() {
//        //final LineChart<Number, Number> chart = createChart();
//        XYChart<Number, Number> chart = chartek.getXYChart();
//
//        StackPane chartContainer = new StackPane();
//        chartContainer.getChildren().add(chart);
//
//        Rectangle zoomRect = new Rectangle();
//        zoomRect.setManaged(false);
//        zoomRect.setFill(Color.LIGHTSEAGREEN.deriveColor(0, 1, 1, 0.5));
//        chartContainer.getChildren().add(zoomRect);
//        //borderPane.getChildren().add(zoomRect);
//       // borderPane.getChildren().add(zoomRect);
//
//        setUpZooming(zoomRect, chart);
//
//        HBox controls = new HBox(10);
//        controls.setPadding(new Insets(10));
//        controls.setAlignment(Pos.CENTER);
//
//        Button zoomButton = new Button("Zoom");
//        Button resetButton = new Button("Reset");
//        zoomButton.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                doZoom(zoomRect, chart);
//            }
//        });
//        resetButton.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                final NumberAxis xAxis = (NumberAxis)chart.getXAxis();
//                xAxis.setLowerBound(0);
//                xAxis.setUpperBound(1000);
//                final NumberAxis yAxis = (NumberAxis)chart.getYAxis();
//                yAxis.setLowerBound(0);
//                yAxis.setUpperBound(1000);
//
//                zoomRect.setWidth(0);
//                zoomRect.setHeight(0);
//            }
//        });
//        final BooleanBinding disableControls =
//                zoomRect.widthProperty().lessThan(5)
//                        .or(zoomRect.heightProperty().lessThan(5));
//        zoomButton.disableProperty().bind(disableControls);
//        controls.getChildren().addAll(zoomButton, resetButton);
//
//        //final BorderPane root = new BorderPane();
//        borderPane.setRight(chartContainer);
//        borderPane.setBottom(controls);
////        root.setCenter(chartContainer);
////        root.setBottom(controls);
//
////        final Scene scene = new Scene(root, 600, 400);
////        primaryStage.setScene(scene);
////        primaryStage.show();
//    }
//
//    private LineChart<Number, Number> createChart() {
//        final NumberAxis xAxis = createAxis();
//        final NumberAxis yAxis = createAxis();
//        final LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
//        chart.setAnimated(false);
//        chart.setCreateSymbols(false);
//        chart.setData(generateChartData());
//        return chart ;
//    }
//
//    private NumberAxis createAxis() {
//        final NumberAxis xAxis = new NumberAxis();
//        xAxis.setAutoRanging(false);
//        xAxis.setLowerBound(0);
//        xAxis.setUpperBound(1000);
//        return xAxis;
//    }
//
//    private ObservableList<XYChart.Series<Number, Number>> generateChartData() {
//        final XYChart.Series<Number, Number> series = new XYChart.Series<>();
//        series.setName("Data");
//        final Random rng = new Random();
//        for (int i=0; i<NUM_DATA_POINTS; i++) {
//            XYChart.Data<Number, Number> dataPoint = new XYChart.Data<Number, Number>(i, rng.nextInt(1000));
//            series.getData().add(dataPoint);
//        }
//        return FXCollections.observableArrayList(Collections.singleton(series));
//    }
//
//    private void setUpZooming(final Rectangle rect, final Node zoomingNode) {
//        final ObjectProperty<Point2D> mouseAnchor = new SimpleObjectProperty<>();
//        zoomingNode.setOnMousePressed(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                mouseAnchor.set(new Point2D(event.getX(), event.getY()));
//                rect.setWidth(0);
//                rect.setHeight(0);
//            }
//        });
//        zoomingNode.setOnMouseDragged(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                double x = event.getX();
//                double y = event.getY();
//                rect.setX(Math.min(x, mouseAnchor.get().getX()));
//                rect.setY(Math.min(y, mouseAnchor.get().getY()));
//                rect.setWidth(Math.abs(x - mouseAnchor.get().getX()));
//                rect.setHeight(Math.abs(y - mouseAnchor.get().getY()));
//            }
//        });
//    }
//
//    private void doZoom(Rectangle zoomRect, XYChart<Number, Number> chart) {
//        Point2D zoomTopLeft = new Point2D(zoomRect.getX(), zoomRect.getY());
//        Point2D zoomBottomRight = new Point2D(zoomRect.getX() + zoomRect.getWidth(), zoomRect.getY() + zoomRect.getHeight());
//        final NumberAxis yAxis = (NumberAxis) chart.getYAxis();
//        Point2D yAxisInScene = yAxis.localToScene(0, 0);
//        final NumberAxis xAxis = (NumberAxis) chart.getXAxis();
//        Point2D xAxisInScene = xAxis.localToScene(0, 0);
//        double xOffset = zoomTopLeft.getX() - yAxisInScene.getX() ;
//        double yOffset = zoomBottomRight.getY() - xAxisInScene.getY();
//        double xAxisScale = xAxis.getScale();
//        double yAxisScale = yAxis.getScale();
//        chartek.setAxises(xAxis.getLowerBound() + xOffset / xAxisScale, xAxis.getLowerBound() + zoomRect.getWidth() / xAxisScale, yAxis.getLowerBound() + yOffset / yAxisScale, yAxis.getLowerBound() - zoomRect.getHeight() / yAxisScale);
////        chartek.setxAxis(chartek.getxAxis().setLowerBound(xAxis.getLowerBound() + xOffset / xAxisScale));
////        chartek.getxAxis().setUpperBound(xAxis.getLowerBound() + zoomRect.getWidth() / xAxisScale);
////        chartek.getyAxis().setLowerBound(yAxis.getLowerBound() + yOffset / yAxisScale);
////        chartek.getyAxis().setUpperBound(yAxis.getLowerBound() - zoomRect.getHeight() / yAxisScale);
//        xAxis.setLowerBound(xAxis.getLowerBound() + xOffset / xAxisScale);
//        xAxis.setUpperBound(xAxis.getLowerBound() + zoomRect.getWidth() / xAxisScale);
//        yAxis.setLowerBound(yAxis.getLowerBound() + yOffset / yAxisScale);
//        yAxis.setUpperBound(yAxis.getLowerBound() - zoomRect.getHeight() / yAxisScale);
//        chartek.setXYChart(chart);
//        System.out.println(yAxis.getLowerBound() + " " + yAxis.getUpperBound());
//        zoomRect.setWidth(0);
//        zoomRect.setHeight(0);
//    }
}
