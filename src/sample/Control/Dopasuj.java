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

    private double search(double value, List<Double> b) {
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

    public double oblicz(List<Double> data) {
        int minIndex = data.lastIndexOf(Collections.min(data));
        min = data.get(minIndex);

        int maxIndex = data.indexOf(Collections.max(data));
        max = data.get(maxIndex);
        double point = search((max-min)*0.632,  data);
        stalaCzasowa = data.indexOf(point+min)-minIndex;
        return stalaCzasowa;
    }

    public Series getSer() {
        return ser;
    }

    public void setSer(Series ser) {
        this.ser = ser;
    }
}
