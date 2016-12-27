package Tigerek;

import javafx.animation.AnimationTimer;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by E6420 on 2016-11-30.
 */
public class Chartek {
    private ArrayList<String> out = new ArrayList<>();
    private ConcurrentLinkedQueue<Number> dataQ = new ConcurrentLinkedQueue<Number>();
    private ExecutorService executor;
//    private AddToQueue addToQueue;
    private AddToQueueFromText addToQueueFromText;
    private AddToQueueRealTime addToQueueRealTime;
    private int xSeriesData = 0;
    private static final int MAX_DATA_POINTS = 30;
    private NumberAxis xAxis;
    private NumberAxis yAxis;
    private XYChart.Series series;
    final AreaChart<Number, Number> areaChart;

    public Chartek() {
        xAxis = new NumberAxis(0, MAX_DATA_POINTS, MAX_DATA_POINTS / 10);
        xAxis.setForceZeroInRange(false);
        xAxis.setAutoRanging(false);

        yAxis = new NumberAxis();
        yAxis.setAutoRanging(true);

        areaChart = new AreaChart<Number, Number>(xAxis, yAxis) {
            //Wywalenie Kropek, można dodać
            // Override to remove symbols on each data point
            @Override
            protected void dataItemAdded(Series<Number, Number> series, int itemIndex, Data<Number, Number> item) {
            }
        };
        areaChart.setAnimated(false);
        areaChart.setId("liveAreaChart");
        areaChart.setTitle("Animated Area Chart");

        //-- Chart Series
        series = new AreaChart.Series<Number, Number>();
        series.setName("Area Chart Series");
        areaChart.getData().add(series);
    }

    public void createChart() {
        //-- Prepare Executor Services
        executor = Executors.newCachedThreadPool();
        addToQueueFromText = new AddToQueueFromText();
        executor.execute(addToQueueFromText);
        //-- Prepare Timeline
        prepareTimeline();
    }

    public void createRealtimeChart() {
        //-- Prepare Executor Services
        executor = Executors.newCachedThreadPool();
        addToQueueRealTime = new AddToQueueRealTime();
        executor.execute(addToQueueRealTime);
        //-- Prepare Timeline
        prepareTimeline();
    }

    private class AddToQueueRealTime implements Runnable {
        public void run() {
            try {
                // add a item of random data to queue
                //dataQ.add(Math.random());
                Thread.sleep(1000);
                try {
                    out.add(Communicator.temporary);
                    dataQ.add(Double.parseDouble(Communicator.temporary));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                executor.execute(this);
            } catch (InterruptedException ex) {
                //Logger.getLogger(AreaChartSample.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private class AddToQueueFromText implements Runnable {
        String csvFile = "test.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";

        public void run() {
            try {
                try {
                    try {
                        br = new BufferedReader(new FileReader(csvFile));
                        while ((line = br.readLine().trim()) != null) {
                            dataQ.add(Double.parseDouble(line));
                            System.out.println(Double.parseDouble(line));
                            Thread.sleep(100);
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
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

    //-- Timeline gets called in the JavaFX Main thread
    private void prepareTimeline() {
        // Every frame to take any data from queue and add to chart
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                addDataToSeries();
            }
        }.start();
    }

    private void addDataToSeries() {
        //W ORYGINALE:
//        for (int i = 0; i < 1000; i++) { //-- add 20 numbers to the plot+
//            if (dataQ.isEmpty()) break;
//            series.getData().add(new AreaChart.Data(xSeriesData++, dataQ.remove()));
//        }
        if (!dataQ.isEmpty()) {
            series.getData().add(new AreaChart.Data(xSeriesData++, dataQ.remove()));
        }
        //SKALOWANIE
//        // remove points to keep us at no more than MAX_DATA_POINTS
//        if (series.getData().size() > MAX_DATA_POINTS) {
//            series.getData().remove(0, series.getData().size() - MAX_DATA_POINTS);
//        }
//        // update
        xAxis.setLowerBound(xSeriesData - MAX_DATA_POINTS);
        xAxis.setUpperBound(xSeriesData - 1);
    }

    public ArrayList<String> getOut() {
        return out;
    }

    public void setOut(ArrayList<String> out) {
        this.out = out;
    }
}
