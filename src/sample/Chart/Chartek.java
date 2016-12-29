package sample.Chart;

import javafx.animation.AnimationTimer;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import sample.Control.Communicator;
import sample.Control.Controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by E6420 on 2016-11-30.
 */
public class Chartek {
    public void init(Controller controller){
        this.controller = controller;
    }
    Controller controller;
    final int LICZBA_CZUJNIKOW = 6;
    private ArrayList<String> out = new ArrayList<>();
    //    private List<ConcurrentLinkedQueue<Number>> data = new  ArrayList<ConcurrentLinkedQueue<Number>>();
//    private void createData(int countOfCharts){
//        for (int i = 0; i<countOfCharts; i++){
//            data.add(i, new ConcurrentLinkedQueue<Number>());
//        }
//    }
    private ConcurrentLinkedQueue<Number> dataQ = new ConcurrentLinkedQueue<Number>();
    private ConcurrentLinkedQueue<Number> dataQ2 = new ConcurrentLinkedQueue<Number>();
    private ConcurrentLinkedQueue<Number> dataQ3 = new ConcurrentLinkedQueue<Number>();
    private ConcurrentLinkedQueue<Number> dataQ4 = new ConcurrentLinkedQueue<Number>();
    private ConcurrentLinkedQueue<Number> dataQ5 = new ConcurrentLinkedQueue<Number>();
    private ConcurrentLinkedQueue<Number> dataQ6 = new ConcurrentLinkedQueue<Number>();
    private ExecutorService executor;
    //    private AddToQueue addToQueue;
    private AddToQueueFromText addToQueueFromText;
    private AddToQueueRealTime addToQueueRealTime;
    private int xSeriesData = 0;
    private static final int MAX_DATA_POINTS = 130;
    private NumberAxis xAxis;
    private NumberAxis yAxis;
    private XYChart.Series series;
    private XYChart.Series series2;
    private XYChart.Series series3;
    private XYChart.Series series4;
    private XYChart.Series series5;
    private XYChart.Series series6;
    private Series ser;
    private XYChart<Number, Number> XYChart;
    private volatile boolean running = true;

    public void stop(){
        System.out.println("Force closing");
        executor.shutdownNow();
    }

    public void Start() {
    //public Chartek() {
        xSeriesData = 0;
        ser = new Series(LICZBA_CZUJNIKOW);
        xAxis = new NumberAxis(0, MAX_DATA_POINTS, MAX_DATA_POINTS / 10);
        xAxis.setForceZeroInRange(false);
        xAxis.setAutoRanging(false);

        yAxis = new NumberAxis();
        yAxis.setAutoRanging(true);
        if (controller.getLineSelection()){
        XYChart = new LineChart<Number, Number>(xAxis, yAxis) {
                    //Wywalenie Kropek, można dodać
                    // Override to remove symbols on each data point
                    @Override
                    protected void dataItemAdded(Series<Number, Number> series, int itemIndex, Data<Number, Number> item) {
                    }
                };
        } else if (controller.getointsAndLineSelection()){
            XYChart = new LineChart<Number, Number>(xAxis, yAxis);
        } else {
            XYChart = new ScatterChart<Number, Number>(xAxis, yAxis);
        }
        XYChart.setAnimated(false);
        XYChart.setId("liveXYChart");
        XYChart.setTitle("Animated Area Chart");
        //XYChart.getStyleClass().add(styleClass);

        //-- Chart Series
        series = new XYChart.Series<Number, Number>();
        series.setName("Area Chart Series");
        series2 = new XYChart.Series<Number, Number>();
        series2.setName("Druga seria");
        series3 = new XYChart.Series<Number, Number>();
        series3.setName("Trzecia seria");
        series4 = new XYChart.Series<Number, Number>();
        series4.setName("Czwarta seria");
        series5 = new XYChart.Series<Number, Number>();
        series5.setName("Piąta seria");
        series6 = new XYChart.Series<Number, Number>();
        series6.setName("Szusta seria");
        //XYChart.setVisible(false);
        XYChart.getData().addAll(series, series2, series3, series4, series5, series6);
        //XYChart.setVisible(false);



        //XYChart.getStyleClass().add(styleClass);
    }

    public void createChart(File file) {
        //-- Prepare Executor Services
        executor = Executors.newCachedThreadPool();
        addToQueueFromText = new AddToQueueFromText(file);
        executor.execute(addToQueueFromText);
        //-- Prepare Timeline
        prepareTimeline(1);
    }

    public void createRealtimeChart() {
        //-- Prepare Executor Services
        executor = Executors.newCachedThreadPool();
        addToQueueRealTime = new AddToQueueRealTime();
        executor.execute(addToQueueRealTime);
        //-- Prepare Timeline
        prepareTimeline(1);
    }

    private class AddToQueueRealTime implements Runnable {
        int i = 0;

        public void run() {
            //while (running){
                try {
                    //while (running){
                    Thread.sleep(1000);
                    try {
                        ser.generateSeries(Communicator.temporary);
                        if (Controller.nagrajPrzebiegClicked) {
                            out.add(Communicator.temporary);
                        }
                        dataQ.add(ser.getCharts().get(0).get(i));
                        dataQ2.add(ser.getCharts().get(1).get(i));
                        dataQ3.add(ser.getCharts().get(2).get(i));
                        dataQ4.add(ser.getCharts().get(3).get(i));
                        dataQ5.add(ser.getCharts().get(4).get(i));
                        dataQ6.add(ser.getCharts().get(5).get(i));
                        System.out.println("Po przetworzeniu: " + ser.getCharts().get(0).get(i));
                        System.out.println("Po przetworzeniu2: " + ser.getCharts().get(1).get(i));
                        i++;
                        xSeriesData++;
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    } catch (java.lang.Exception ex){ //todo sprawdz
                        ex.printStackTrace();
                    }
                    executor.execute(this);

                } catch (InterruptedException ex) {
                    //todo dokoncz
                }
            //}
        }
    }

    private class AddToQueueFromText implements Runnable {
        public AddToQueueFromText(File csvFile) {
            this.csvFile = csvFile;
        }
        int i = 0;
        private File csvFile;

        BufferedReader br = null;
        String line = "";

        public void run() {
            //while (running){
                try {
                    //Thread.sleep(1000);
                    try {
                        try {
                            br = new BufferedReader(new FileReader(csvFile));
                            while ((line = br.readLine().trim()) != null) {
                                ser.generateSeries(line);
                                dataQ.add(ser.getCharts().get(0).get(i));
                                dataQ2.add(ser.getCharts().get(1).get(i));
                                dataQ3.add(ser.getCharts().get(2).get(i));
                                dataQ4.add(ser.getCharts().get(3).get(i));
                                dataQ5.add(ser.getCharts().get(4).get(i));
                                dataQ6.add(ser.getCharts().get(5).get(i));
                                Thread.sleep(1000);
                                i++;
                                xSeriesData++;
                            }
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        } catch (NullPointerException e) {
                            System.out.println("koniec pliku???");
                            e.printStackTrace();
                        }
                    } catch (IOException ex) {
                        System.out.println("Nie moge odczytac pliku!");
                    }

                    executor.execute(this);
                } catch (InterruptedException exe) {
                    exe.printStackTrace();
                }
            //}
        }
    }

    //-- Timeline gets called in the JavaFX Main thread
    //0 zmniejsz 2 powieksz
    public void prepareTimeline(int scale) {
        // Every frame to take any data from queue and add to chart
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (scale == 1) {
                addDataToSeries();
                } else if (scale == 0) {
                    pomniejsz();
                } else if(scale == 2) {
                    powieksz();
                }
            }
        }.start();
    }
    //static int countOfSinglePoints;
    private void addDataToSeries() {
        //W ORYGINALE:
//        for (int i = 0; i < 20; i++) { //-- add 20 numbers to the plot+
//            if (dataQ.isEmpty()) break;
//            series.getData().add(new XYChart.Data(xSeriesData++, dataQ.remove()));
//        }

        if (!dataQ.isEmpty() && controller.getCheckboxSelection()) {
            series.getData().add(new XYChart.Data(xSeriesData, dataQ.remove()));
        }
        if (!dataQ2.isEmpty() && controller.getCheckbox2Selection()) {
            series2.getData().add(new XYChart.Data(xSeriesData, dataQ2.remove()));
        }
        if (!dataQ3.isEmpty() && controller.getCheckbox3Selection()) {
            series3.getData().add(new XYChart.Data(xSeriesData, dataQ3.remove()));
        }
        if (!dataQ4.isEmpty() && controller.getCheckbox4Selection()) {
            series4.getData().add(new XYChart.Data(xSeriesData, dataQ4.remove()));
        }
        if (!dataQ5.isEmpty() && controller.getCheckbox5Selection()) {
            series5.getData().add(new XYChart.Data(xSeriesData, dataQ5.remove()));
        }
        if (!dataQ6.isEmpty() && controller.getCheckbox6Selection()) {
            series6.getData().add(new XYChart.Data(xSeriesData, dataQ6.remove()));
        }
//        if (countOfSinglePoints == LICZBA_CZUJNIKOW){
//            xSeriesData++;
//            countOfSinglePoints = 0;
//        }
//        if(!dataQ.isEmpty() || !dataQ2.isEmpty() || !dataQ3.isEmpty() || !dataQ4.isEmpty() || !dataQ5.isEmpty() || !dataQ6.isEmpty()){
//            xSeriesData++;
//        }
        //xSeriesData++;

        //SKALOWANIE
//        // remove points to keep us at no more than MAX_DATA_POINTS
//        if (series.getData().size() > MAX_DATA_POINTS) {
//            series.getData().remove(0, series.getData().size() - MAX_DATA_POINTS);
//        }
//        // update

//        System.out.println(controller.getSkalowanie().getSelectedToggle().getProperties());
        //System.out.println(controller.getSkalowanie().getSelectedToggle().getUserData());
//        System.out.println(controller.getSkalowanie().getSelectedToggle());
        //controller.getSkalowanie().getSelectedToggle().getProperties().
        if (controller.getSkalowanie().getSelectedToggle().getUserData().equals(0)){
            xAxis.setLowerBound(0);
            xAxis.setUpperBound(xSeriesData - 1);
        } else {
            xAxis.setLowerBound(xSeriesData - (int)controller.getSkalowanie().getSelectedToggle().getUserData());
            xAxis.setUpperBound(xSeriesData - 1);
        }
        //xAxis.setLowerBound(0);
        //xAxis.setLowerBound(xSeriesData - MAX_DATA_POINTS);
        //xAxis.setUpperBound(xSeriesData - 1);
        //xAxis.setUpperBound(5);
    }


//    public void prepareTimeline2() {
//        // Every frame to take any data from queue and add to chart
//        new AnimationTimer() {
//            @Override
//            public void handle(long now) {
//                powieksz();
//            }
//        }.start();
//    }
//
//    public void prepareTimeline3() {
//        // Every frame to take any data from queue and add to chart
//        new AnimationTimer() {
//            @Override
//            public void handle(long now) {
//                pomniejsz();
//            }
//        }.start();
//    }

    public void powieksz(){
        if (xSeriesData-xAxis.getLowerBound()>8) {
        xAxis.setLowerBound(xAxis.getLowerBound()+Math.ceil((xAxis.getUpperBound()-xAxis.getLowerBound())/2));
        } else {controller.getZoom().setDisable(true);}
    }

    public void pomniejsz(){
        //if((xAxis.getUpperBound()-xAxis.getLowerBound())/2>xAxis.getLowerBound()){
        controller.getZoom().setDisable(false);
//        if((Math.abs(xAxis.getLowerBound()-xAxis.getUpperBound()))/2<xAxis.getLowerBound()){
//            xAxis.setLowerBound(Math.floor(xAxis.getLowerBound()*3/2)-Math.floor(xAxis.getUpperBound()/2));
//        } else {xAxis.setLowerBound(0);}
        xAxis.setLowerBound(0);
        //xAxis.setLowerBound(Math.ceil(xAxis.getLowerBound()*/2)-Math.ceil(xAxis.getUpperBound()/2));
    }

//    public void clearData(int numberOfSensor){
//
//    }

    public ArrayList<String> getOut() {
        return out;
    }

    public XYChart<Number, Number> getXYChart() {
        return XYChart;
    }

    public ConcurrentLinkedQueue<Number> getDataQ() {
        return dataQ;
    }

    public ConcurrentLinkedQueue<Number> getDataQ2() {
        return dataQ2;
    }

    public ConcurrentLinkedQueue<Number> getDataQ3() {
        return dataQ3;
    }

    public ConcurrentLinkedQueue<Number> getDataQ4() {
        return dataQ4;
    }

    public ConcurrentLinkedQueue<Number> getDataQ5() {
        return dataQ5;
    }

    public ConcurrentLinkedQueue<Number> getDataQ6() {
        return dataQ6;
    }

    public NumberAxis getxAxis() {
        return xAxis;
    }

    public void setAxises(double xAxisLower, double xAxisUpper, double yAxisLower, double yAxisUpper) {
        this.xAxis.setLowerBound(xAxisLower);// = xAxis;
        this.xAxis.setUpperBound(xAxisUpper);
        this.yAxis.setLowerBound(yAxisLower);
        this.yAxis.setUpperBound(yAxisUpper);
    }

    public NumberAxis getyAxis() {
        return yAxis;
    }

    public void setyAxis(NumberAxis yAxis) {
        this.yAxis = yAxis;
    }

    //
//    public void doZoom(Rectangle zoomRect, XYChart<Number, Number> chart) {
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
//        xAxis.setLowerBound(xAxis.getLowerBound() + xOffset / xAxisScale);
//        xAxis.setUpperBound(xAxis.getLowerBound() + zoomRect.getWidth() / xAxisScale);
//        yAxis.setLowerBound(yAxis.getLowerBound() + yOffset / yAxisScale);
//        yAxis.setUpperBound(yAxis.getLowerBound() - zoomRect.getHeight() / yAxisScale);
//        System.out.println(yAxis.getLowerBound() + " " + yAxis.getUpperBound());
//        zoomRect.setWidth(0);
//        zoomRect.setHeight(0);
//    }
//
//    public void setUpZooming(final Rectangle rect, final Node zoomingNode) {
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


    public void setXYChart(javafx.scene.chart.XYChart<Number, Number> XYChart) {
        this.XYChart = XYChart;
    }
}

