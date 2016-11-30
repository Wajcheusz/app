package Tigerek;

import javafx.animation.AnimationTimer;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by E6420 on 2016-11-30.
 */
public class Chartek {
    private ConcurrentLinkedQueue<Number> dataQ = new ConcurrentLinkedQueue<Number>();
    private ExecutorService executor;
    private AddToQueue addToQueue;
    private int xSeriesData = 0;

    private static final int MAX_DATA_POINTS = 50;
    private NumberAxis xAxis;
    private XYChart.Series series;
    private NumberAxis yAxis;
    final AreaChart<Number, Number> sc;

    public Chartek(){
        xAxis = new NumberAxis(0,MAX_DATA_POINTS,MAX_DATA_POINTS/10);
        xAxis.setForceZeroInRange(false);
        xAxis.setAutoRanging(false);

        yAxis = new NumberAxis();
        yAxis.setAutoRanging(true);

        sc = new AreaChart<Number, Number>(xAxis, yAxis) {
            // Override to remove symbols on each data point
            @Override protected void dataItemAdded(Series<Number, Number> series, int itemIndex, Data<Number, Number> item) {}
        };
        sc.setAnimated(false);
        sc.setId("liveAreaChart");
        sc.setTitle("Animated Area Chart");

        //-- Chart Series
        series = new AreaChart.Series<Number, Number>();
        series.setName("Area Chart Series");
        sc.getData().add(series);
    }

//    public static void main(String[] args) {
//        launch(args);
//    }

    private class AddToQueue implements Runnable {
        public void run() {
            try {
                // add a item of random data to queue
                //dataQ.add(Math.random());
                try {
                    dataQ.add(Double.parseDouble(Communicator.temporary));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                Thread.sleep(100);
                executor.execute(this);
            } catch (InterruptedException ex) {
                //Logger.getLogger(AreaChartSample.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    //-- Timeline gets called in the JavaFX Main thread
    private void prepareTimeline() {
        // Every frame to take any data from queue and add to chart
        new AnimationTimer() {
            @Override public void handle(long now) {
                addDataToSeries();
            }
        }.start();
    }

    private void addDataToSeries() {
        for (int i = 0; i < 20; i++) { //-- add 20 numbers to the plot+
            if (dataQ.isEmpty()) break;
            series.getData().add(new AreaChart.Data(xSeriesData++, dataQ.remove()));
        }
        // remove points to keep us at no more than MAX_DATA_POINTS
        if (series.getData().size() > MAX_DATA_POINTS) {
            series.getData().remove(0, series.getData().size() - MAX_DATA_POINTS);
        }
        // update
        xAxis.setLowerBound(xSeriesData-MAX_DATA_POINTS);
        xAxis.setUpperBound(xSeriesData-1);
    }


    public void createChart() {
        //-- Prepare Executor Services
        executor = Executors.newCachedThreadPool();
        addToQueue = new AddToQueue();
        executor.execute(addToQueue);
        //-- Prepare Timeline
        prepareTimeline();
    }
}
