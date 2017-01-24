package sample.Control;

import sample.Chart.Series;

import java.util.*;
import java.util.stream.IntStream;

/**
 * Created by Mateusz.Blaszczak on 2017-01-17.
 */
public class Dopasuj {
    private Series ser;
    private double max, min;
    private int maxIx, minIx;
    double stalaCzasowa;

    public double search(double value, List<Double> b) {
        List a = new ArrayList(b);
        for (ListIterator<Double> bs = a.listIterator(); bs.hasNext();) {
            Double element = bs.next();
            bs.set(element-min);
        }
        //Collections.sort(a);
        int lo = 0;
        int hi = a.size() - 1;

        double lastValue = 0;

        while (lo <= hi) {
            int mid = (lo + hi) / 2;
            lastValue = (double) a.get(mid);
            if (value < lastValue) {
                hi = mid - 1;
            } else if (value > lastValue) {
                lo = mid + 1;
            } else {
                return lastValue;
            }
        }
        return lastValue;
    }

    public double oblicz(List ser) {
        int minIndex = ser.lastIndexOf(Collections.min(ser));
        System.out.println("MinIx " + minIndex + ", value " + ser.getCharts().get(0).get(minIndex));
        min = ser.getCharts().get(0).get(minIndex);

        int maxIndex = ser.getCharts().get(0).indexOf(Collections.max(ser.getCharts().get(0)));
        System.out.println("MaxIx " + maxIndex + ", value " + ser.getCharts().get(0).get(maxIndex));

        max = ser.getCharts().get(0).get(maxIndex);
        double point = search((max-min)*0.632,  ser.getCharts().get(0));
        //System.out.println("Close " + close);
        //System.out.println(ser.getCharts().get(0).indexOf(close+min));
        System.out.println(ser.getCharts().get(0).indexOf(point+min)-minIndex);
        stalaCzasowa = ser.getCharts().get(0).indexOf(point+min)-minIndex;
        return stalaCzasowa;
    }

    public Series getSer() {
        return ser;
    }

    public void setSer(Series ser) {
        this.ser = ser;
    }
}
