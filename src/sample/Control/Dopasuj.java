package sample.Control;

import sample.Chart.Series;

import java.util.Collections;
import java.util.Comparator;
import java.util.stream.IntStream;

/**
 * Created by E6420 on 2017-01-17.
 */
public class Dopasuj {
    private Series ser;
    private double max, min;
    private int maxIx, minIx;


    public void oblicz(Series ser) {
//        double max = Collections.max(ser.getCharts().get(0));
//        double min = Collections.min(ser.getCharts().get(0));
//        int t = 0;
//        for (double i : ser.getCharts().get(0)) {
//
//        }
//        IntStream.range(0, ser.getCharts().get(0).size())
//                .boxed().min(Comparator.comparing(ser.getCharts().get(0)::get))
//                .ifPresent(ix -> {
//                    System.out.println("Index min" + ix + ", value min" + ser.getCharts().get(0).get(ix));
//                    min = ser.getCharts().get(0).get(ix);
//                    minIx = ix;
//                });
//
//        IntStream.range(0, ser.getCharts().get(0).size())
//                .boxed().max(Comparator.comparing(ser.getCharts().get(0)::get))
//                .ifPresent(ix -> {
//                    System.out.println("Index " + ix + ", value " + ser.getCharts().get(0).get(ix));
//                    max = ser.getCharts().get(0).get(ix);
//                    maxIx = ix;
//                });
//
//        System.out.println("MaxIx " + maxIx + ", value " + max);
//        System.out.println("MinIx " + minIx + ", value " + min);
//        int minIndex = ser.getCharts().get(0).indexOf(Collections.min(ser.getCharts().get(0)));
        int minIndex = ser.getCharts().get(0).lastIndexOf(Collections.min(ser.getCharts().get(0)));
        System.out.println("MinIx " + minIndex + ", value " + ser.getCharts().get(0).get(minIndex));

        int maxIndex = ser.getCharts().get(0).indexOf(Collections.max(ser.getCharts().get(0)));
        System.out.println("MaxIx " + maxIndex + ", value " + ser.getCharts().get(0).get(maxIndex));
    }

    public Series getSer() {
        return ser;
    }

    public void setSer(Series ser) {
        this.ser = ser;
    }
}
