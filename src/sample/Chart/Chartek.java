package sample.Chart;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import sample.Control.Communicator;
import sample.Control.Controller;
import sample.Control.Dopasuj;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by E6420 on 2016-11-30.
 */
public class Chartek {
    public void init(Controller controller) {
        this.controller = controller;
    }

    Controller controller;
    final int LICZBA_CZUJNIKOW = 6;
    private int playerTime = 1000;
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
    private List<Integer> kolejka = new ArrayList<>();
    private double a, b, c, d, e, f;

    public void stop() {
        System.out.println("Force closing");
        XYChart.setVisible(false);
        try {
            executor.shutdownNow();
        } catch (Exception e) {
            System.out.println("costam");
        }

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
        series = new XYChart.Series<Number, Number>();
        series.setName(controller.getCheckbox().getText());
        series2 = new XYChart.Series<Number, Number>();
        series2.setName(controller.getCheckbox2().getText());
        series3 = new XYChart.Series<Number, Number>();
        series3.setName(controller.getCheckbox3().getText());
        series4 = new XYChart.Series<Number, Number>();
        series4.setName(controller.getCheckbox4().getText());
        series5 = new XYChart.Series<Number, Number>();
        series5.setName(controller.getCheckbox5().getText());
        series6 = new XYChart.Series<Number, Number>();
        series6.setName(controller.getCheckbox6().getText());
        if (controller.getLineSelection()) {
            XYChart = new LineChart<Number, Number>(xAxis, yAxis) {
                //Wywalenie Kropek, można dodać
                // Override to remove symbols on each data point
                @Override
                protected void dataItemAdded(Series<Number, Number> series, int itemIndex, Data<Number, Number> item) {
                }
            };
        } else if (controller.getPointsAndLineSelection()) {
            XYChart = new LineChart<Number, Number>(xAxis, yAxis);
        } else {
            XYChart = new ScatterChart<Number, Number>(xAxis, yAxis);
        }
        XYChart.setAnimated(false);
        XYChart.setId("Wykres ID");
        XYChart.setTitle("Wykres temperaturowy");
        XYChart.getData().addAll(series, series2, series3, series4, series5, series6);
    }

    public void createChart(File file, int speed) {
        kolejka.clear();
        //-- Prepare Executor Services
        if (speed != 25) {
            executor = Executors.newCachedThreadPool();
            addToQueueFromText = new AddToQueueFromText(file, speed);
            executor.execute(addToQueueFromText);
            prepareTimeline();
        } else {
            addToQueueFromTextAll(file);
            zoom();
            prepareTimeline();
        }

    }

    public void createRealtimeChart() {
        kolejka.clear();
        //-- Prepare Executor Services
        executor = Executors.newCachedThreadPool();
        addToQueueRealTime = new AddToQueueRealTime();
        executor.execute(addToQueueRealTime);
        prepareTimeline();
    }

    public void clearKolejka() {
        kolejka.clear();
    }

    private void clearTextFields() {
        controller.getTxt1().clear();
        controller.getTxt2().clear();
        controller.getTxt3().clear();
        controller.getTxt4().clear();
        controller.getTxt5().clear();
    }

    private class AddToQueueRealTime implements Runnable {
        int i = 0;

        public void run() {
            //while (running){
            try {
                //while (running){
                Thread.sleep(500);
                try {
                    if (!Communicator.temporary.equals("")) ;
                    {
                        ser.generateSeries(Communicator.temporary);
                        if (Controller.nagrajPrzebiegClicked) {
                            out.add(Communicator.temporary);
                        }

                        a = ser.getCharts().get(0).get(i);
                        b = ser.getCharts().get(1).get(i);
                        c = ser.getCharts().get(2).get(i);
                        d = ser.getCharts().get(3).get(i);
                        e = ser.getCharts().get(4).get(i);
                        f = ser.getCharts().get(5).get(i);

                        dataQ.add(a);
                        dataQ2.add(b);
                        dataQ3.add(c);
                        dataQ4.add(d);
                        dataQ5.add(e);
                        dataQ6.add(f);

                        Platform.runLater(() -> {
                            clearTextFields();
                            controller.getTxt1().appendText(String.valueOf(a));
                            controller.getTxt2().appendText(String.valueOf(b));
                            controller.getTxt3().appendText(String.valueOf(c));
                            controller.getTxt4().appendText(String.valueOf(d));
                            controller.getTxt5().appendText(String.valueOf(e));
                        });

                        System.out.println("Po przetworzeniu: " + ser.getCharts().get(0).get(i));
                        System.out.println("Po przetworzeniu2: " + ser.getCharts().get(1).get(i));
                        i++;
                        xSeriesData++;
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                } catch (java.lang.Exception ex) { //todo sprawdz
                    ex.printStackTrace();
                }
                executor.execute(this);

            } catch (InterruptedException ex) {
                //todo dokoncz
            }
        }
    }


    private void clearSeries() {
        series.getData().clear();
        series2.getData().clear();
        series3.getData().clear();
        series4.getData().clear();
        series5.getData().clear();
        series6.getData().clear();
    }

    private void addToQueueFromTextAll(File csvFile) {
        clearSeries();
        int i2 = 0;
        xSeriesData = 0;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(csvFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Dopasuj x = new Dopasuj();

        String line = "";
        try {
            while ((line = br.readLine().trim()) != null) {
                ser.generateSeries(line);
                x.oblicz(ser);
                if (controller.getCheckboxSelection() == true) {
                    dataQ.add(ser.getCharts().get(0).get(i2));
                    series.getData().add(new XYChart.Data(i2, dataQ.remove()));
                }
                if (controller.getCheckbox2Selection() == true) {
                    dataQ2.add(ser.getCharts().get(1).get(i2));
                    series2.getData().add(new XYChart.Data(i2, dataQ2.remove()));
                }
                if (controller.getCheckbox3Selection() == true) {
                    dataQ3.add(ser.getCharts().get(2).get(i2));
                    series3.getData().add(new XYChart.Data(i2, dataQ3.remove()));
                }
                if (controller.getCheckbox4Selection() == true) {
                    dataQ4.add(ser.getCharts().get(3).get(i2));
                    series4.getData().add(new XYChart.Data(xSeriesData, dataQ4.remove()));
                }
                if (controller.getCheckbox5Selection() == true) {
                    dataQ5.add(ser.getCharts().get(4).get(i2));
                    series5.getData().add(new XYChart.Data(xSeriesData, dataQ5.remove()));
                }
                if (controller.getCheckbox6Selection() == true) {
                    dataQ6.add(ser.getCharts().get(5).get(i2));
                    series6.getData().add(new XYChart.Data(xSeriesData, dataQ6.remove()));
                }
                i2++;
                xSeriesData++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.out.println("koniec pliku!?!?");
        }
        //xSeriesData = 0;
    }

    private class AddToQueueFromText implements Runnable {
        public AddToQueueFromText(File csvFile, int speed) {
            this.csvFile = csvFile;
            this.speed = speed;
        }

        int i = 0;
        private File csvFile;
        private int speed;

        BufferedReader br = null;
        String line = "";

        public void run() {
            while (running) {
                try {
                    try {
                        try {
                            br = new BufferedReader(new FileReader(csvFile));
                            while ((line = br.readLine().trim()) != null) {
                                ser.generateSeries(line);
                                a = ser.getCharts().get(0).get(i);
                                b = ser.getCharts().get(1).get(i);
                                c = ser.getCharts().get(2).get(i);
                                d = ser.getCharts().get(3).get(i);
                                e = ser.getCharts().get(4).get(i);
                                f = ser.getCharts().get(5).get(i);

                                dataQ.add(a);
                                dataQ2.add(b);
                                dataQ3.add(c);
                                dataQ4.add(d);
                                dataQ5.add(e);
                                dataQ6.add(f);

                                Platform.runLater(() -> {
                                    clearTextFields();
                                    controller.getTxt1().appendText(String.valueOf(a));
                                    controller.getTxt2().appendText(String.valueOf(b));
                                    controller.getTxt3().appendText(String.valueOf(c));
                                    controller.getTxt4().appendText(String.valueOf(d));
                                    controller.getTxt5().appendText(String.valueOf(e));
                                });
                                Thread.sleep(playerTime / speed);
                                i++;
                                xSeriesData++;
                            }
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        } catch (NullPointerException e) {
                            Platform.runLater(() -> {
                                controller.getLogger().clear();
                                controller.getLogger().appendText("Koniec nagrania");
                            });
                            System.out.println("koniec pliku???");
                            running = false;
                            //e.printStackTrace();
                        }
                    } catch (IOException ex) {
                        System.out.println("Nie moge odczytac pliku!");
                    }
                    executor.execute(this);
                } catch (InterruptedException exe) {
                    exe.printStackTrace();
                }
            }
        }
    }

    public void prepareTimeline() {
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                addDataToSeries();
            }
        }.start();
    }

    public void nadrabianko(int xData, ConcurrentLinkedQueue data, XYChart.Series series) {
        int s = data.size();
        int buf;
        for (int i = 0; i < s; i++) {
            buf = (xData - data.size()) + 1;
            series.getData().add(new XYChart.Data(buf, data.remove()));
        }
    }

    private void addDataToSeries() {
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
        //}
        if (controller.getSkalowanie().getSelectedToggle().getUserData().equals(0)) {
            xAxis.setLowerBound(0);
            xAxis.setUpperBound(xSeriesData - 1);
        } else {
            xAxis.setLowerBound(xSeriesData - (int) controller.getSkalowanie().getSelectedToggle().getUserData());
            xAxis.setUpperBound(xSeriesData - 1);
        }
        zoom();
    }

    private void zoom() {
        for (int x : kolejka) {
            switch (x) {
                case 1:
                    pomniejsz();
                    break;
                case 2:
                    powieksz();
                    break;
                case 3:
                    lewo();
                    break;
                case 4:
                    prawo();
            }
        }


        double down = xAxis.getLowerBound();
        double up = xAxis.getUpperBound();
        double dif = (up - down) / 2;
        if (down <= 0) {
            controller.getLeft().setDisable(true);
        } else {
            controller.getLeft().setDisable(false);
        }
        if (up >= xSeriesData - 1) {
            controller.getRight().setDisable(true);
        } else {
            controller.getRight().setDisable(false);
        }

        if (down <= 0 && up >= xSeriesData) {
            controller.getZoomOut().setDisable(true);
        } else {
            controller.getZoomOut().setDisable(false);
        }

        if (up - down < 6) {
            controller.getZoom().setDisable(true);
        } else {
            controller.getZoom().setDisable(false);
        }
    }


    public void lewo() {
        double down = xAxis.getLowerBound();
        double up = xAxis.getUpperBound();
        double dif = up - down;
        xAxis.setUpperBound(down);
        xAxis.setLowerBound(Math.max(0, down - dif));
    }

    public void prawo() {
        double down = xAxis.getLowerBound();
        double up = xAxis.getUpperBound();
        double dif = up - down;
        xAxis.setUpperBound(Math.min(xSeriesData, up + dif));
        xAxis.setLowerBound(up);
    }

    public void powieksz() {
        double down = xAxis.getLowerBound();
        double up = xAxis.getUpperBound();
        double dif = (up - down) / 2;
        if (xSeriesData - xAxis.getLowerBound() > 8) {
            xAxis.setLowerBound(down + dif / 2);
            xAxis.setUpperBound(up - dif / 2);
        } else {
            controller.getZoom().setDisable(true);
        }
    }

    public void pomniejsz() {
        double down = xAxis.getLowerBound();
        double up = xAxis.getUpperBound();
        double dif = (up - down) / 2;

        xAxis.setLowerBound(Math.max(0, down - dif));
        xAxis.setUpperBound(Math.min(xSeriesData, up + dif));
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

    public int getxSeriesData() {
        return xSeriesData;
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

    public int getPlayerTime() {
        return playerTime;
    }

    public void setPlayerTime(int playerTime) {
        this.playerTime = playerTime;
    }

    public javafx.scene.chart.XYChart.Series getSeries() {
        return series;
    }

    public javafx.scene.chart.XYChart.Series getSeries2() {
        return series2;
    }

    public javafx.scene.chart.XYChart.Series getSeries3() {
        return series3;
    }

    public javafx.scene.chart.XYChart.Series getSeries4() {
        return series4;
    }

    public javafx.scene.chart.XYChart.Series getSeries5() {
        return series5;
    }

    public javafx.scene.chart.XYChart.Series getSeries6() {
        return series6;
    }
}

