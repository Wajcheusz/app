package sample.Chart;

import javafx.animation.AnimationTimer;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import sample.Control.Communicator;
import sample.Control.Controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
    private ConcurrentLinkedQueue<Number> dataQ = new ConcurrentLinkedQueue<Number>();
    private ConcurrentLinkedQueue<Number> dataQ2 = new ConcurrentLinkedQueue<Number>();
    private ConcurrentLinkedQueue<Number> dataQ3 = new ConcurrentLinkedQueue<Number>();
    private ConcurrentLinkedQueue<Number> dataQ4 = new ConcurrentLinkedQueue<Number>();
    private ConcurrentLinkedQueue<Number> dataQ5 = new ConcurrentLinkedQueue<Number>();
    private ConcurrentLinkedQueue<Number> dataQ6 = new ConcurrentLinkedQueue<Number>();
    private ExecutorService executor;
    private AddToQueueFromText addToQueueFromText;
    private AddToQueueRealTime addToQueueRealTime;
    private int xSeriesData = 0;
    private static final int MAX_DATA_POINTS = 130;
    private NumberAxis xAxis;
    private NumberAxis yAxis;
    private XYChart.Series series, series2, series3, series4, series5, series6;
    private Series ser;
    private XYChart<Number, Number> XYChart;
    private volatile boolean running = true;
    private boolean zoomClicked, zoomOutClicked, leftClicked, rightClicked;
    private int zoom;
    private int move;
    private List<Integer> kolejka= new ArrayList<>();

    public void stop(){
        System.out.println("Force closing");
        XYChart.setVisible(false);
        executor.shutdownNow();
        controller.getOpcjeMenu().setDisable(false);
        controller.getSkalowanieMenu().setDisable(false);
    }

    public void start() {
        controller.getOpcjeMenu().setDisable(true);
        controller.getSkalowanieMenu().setDisable(true);
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
        XYChart.setId("Wykres ID");
        XYChart.setTitle("Wykres Tytuł");
        //XYChart.getStyleClass().add(styleClass);
        //-- Chart Series
        series = new XYChart.Series<Number, Number>();
        series.setName("Czujnik1");
        series2 = new XYChart.Series<Number, Number>();
        series2.setName("Druga seria");
        series3 = new XYChart.Series<Number, Number>();
        series3.setName("Trzecia seria");
        series4 = new XYChart.Series<Number, Number>();
        series4.setName("Czwarta seria");
        series5 = new XYChart.Series<Number, Number>();
        series5.setName("Piąta seria");
        series6 = new XYChart.Series<Number, Number>();
        series6.setName("Szósta seria");
        //XYChart.setVisible(false);

//        Set<Node> items = XYChart.lookupAll("Label.chart-legend-item");
//        int i = 0;
//        // these colors came from caspian.css .default-color0..4.chart-pie
//        Color[] colors = { Color.web("#f9d900"), Color.web("#a9e200"), Color.web("#22bad9"), Color.web("#0181e2"), Color.web("#2f357f") };
//        for (Node item : items) {
//            Label label = (Label) item;
//            final Rectangle rectangle = new Rectangle(10, 10, colors[i]);
//            final Glow niceEffect = new Glow();
//            niceEffect.setInput(new Reflection());
//            rectangle.setEffect(niceEffect);
//            label.setGraphic(rectangle);
//            i++;
//        }

        XYChart.getData().addAll(series, series2, series3, series4, series5, series6);



    }

    public void createChart(File file) {
        kolejka.clear();
        //-- Prepare Executor Services
        executor = Executors.newCachedThreadPool();
        addToQueueFromText = new AddToQueueFromText(file);
        executor.execute(addToQueueFromText);
        //-- Prepare Timeline
        prepareTimeline(1);
    }

    public void createRealtimeChart() {
        kolejka.clear();
        //-- Prepare Executor Services
        executor = Executors.newCachedThreadPool();
        addToQueueRealTime = new AddToQueueRealTime();
        executor.execute(addToQueueRealTime);
        //-- Prepare Timeline
        prepareTimeline(1);
    }

    public void clearKolejka(){
        kolejka.clear();
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

    public boolean stop = false;
    private class AddToQueueFromText implements Runnable {
        public AddToQueueFromText(File csvFile) {
            this.csvFile = csvFile;
        }
        int i = 0;
        private File csvFile;

        BufferedReader br = null;
        String line = "";


        public void run() {
            while (running){
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
                            running = false;
                            e.printStackTrace();
                        }
                    } catch (IOException ex) {
                        System.out.println("Nie moge odczytac pliku!");
                    }
                    executor.execute(this);
                    //Thread.sleep(1000);
                } catch (InterruptedException exe) {
                    exe.printStackTrace();
                }
            }
        }
    }

    public AnimationTimer at;
    public AnimationTimer at2;
    //-- Timeline gets called in the JavaFX Main thread
    public void prepareTimeline(int scale) {
        // Every frame to take any data from queue and add to chart
        at = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (scale == 1) {
                addDataToSeries();
                }
            }
        };
        at.start();
    }

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
        if (controller.getSkalowanie().getSelectedToggle().getUserData().equals(0)){
            xAxis.setLowerBound(0);
            xAxis.setUpperBound(xSeriesData - 1);
        } else {
            xAxis.setLowerBound(xSeriesData - (int)controller.getSkalowanie().getSelectedToggle().getUserData());
            xAxis.setUpperBound(xSeriesData - 1);
        }
            for(int x : kolejka){
                switch(x){
                    case 1: pomniejsz();
                        break;
                    case 2: powieksz();
                        break;
                    case 3: lewo();
                        break;
                    case 4: prawo();
                }
            }


        double down=xAxis.getLowerBound();
        double up = xAxis.getUpperBound();
        double dif = (up-down)/2;
        if (down<=0){
            controller.getLeft().setDisable(true);
        } else {controller.getLeft().setDisable(false);}
        if (up>=xSeriesData-1){
            controller.getRight().setDisable(true);
        } else {controller.getRight().setDisable(false);}

        if (down<=0 && up>=xSeriesData){
            controller.getZoomOut().setDisable(true);
        } else {controller.getZoomOut().setDisable(false);}

        if (up-down<6){
            controller.getZoom().setDisable(true);
        } else {controller.getZoom().setDisable(false);}
    }


    public void lewo(){
        double down=xAxis.getLowerBound();
        double up = xAxis.getUpperBound();
        double dif = up-down;
        xAxis.setUpperBound(down);
        xAxis.setLowerBound(Math.max(0, down-dif));
    }

    public void prawo(){
        double down=xAxis.getLowerBound();
        double up = xAxis.getUpperBound();
        double dif = up-down;
        xAxis.setUpperBound(Math.min(xSeriesData, up+dif));
        xAxis.setLowerBound(up);
    }

    public void powieksz(){
        double down=xAxis.getLowerBound();
        double up = xAxis.getUpperBound();
        double dif = (up-down)/2;
        if (xSeriesData-xAxis.getLowerBound()>8) {
            xAxis.setLowerBound(down+dif/2);
            xAxis.setUpperBound(up-dif/2);
        } else {controller.getZoom().setDisable(true);}
    }

    public void pomniejsz(){
        double down=xAxis.getLowerBound();
        double up = xAxis.getUpperBound();
        double dif = (up-down)/2;

        xAxis.setLowerBound(Math.max(0,down-dif));
        xAxis.setUpperBound(Math.min(xSeriesData,up+dif));
    }


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

    public boolean isZoomClicked() {
        return zoomClicked;
    }

    public void setZoomClicked(boolean zoomClicked) {
        this.zoomClicked = zoomClicked;
    }

    public boolean isZoomOutClicked() {
        return zoomOutClicked;
    }

    public void setZoomOutClicked(boolean zoomOutClicked) {
        this.zoomOutClicked = zoomOutClicked;
    }

    public boolean isLeftClicked() {
        return leftClicked;
    }

    public void setLeftClicked(boolean leftClicked) {
        this.leftClicked = leftClicked;
    }

    public boolean isRightClicked() {
        return rightClicked;
    }

    public void setRightClicked(boolean rightClicked) {
        this.rightClicked = rightClicked;
    }

    public int getZoom() {
        return zoom;
    }

    public void setZoom(int zoom) {
        this.zoom = zoom;
    }

    public int getMove() {
        return move;
    }

    public void setMove(int move) {
        this.move = move;
    }

    public List<Integer> getKolejka() {
        return kolejka;
    }

    public void setKolejka(List<Integer> kolejka) {
        this.kolejka = kolejka;
    }

    public void setXYChart(javafx.scene.chart.XYChart<Number, Number> XYChart) {
        this.XYChart = XYChart;
    }
}

