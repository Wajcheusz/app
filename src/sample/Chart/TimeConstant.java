package sample.Chart;

import sample.Chart.Series;

import java.util.*;
import java.util.stream.IntStream;

/**
 * Created by Mateusz.Blaszczak on 2017-01-17.
 */
public class TimeConstant {
    private double max, min;
    private int minIndex, maxIndex;
    private double stalaCzasowa;

    private double search(double value, List<Double> b) {
        List a = new ArrayList(b);
        for (ListIterator<Double> bs = a.listIterator(); bs.hasNext();) {
            Double element = bs.next();
            bs.set(element-min);
        }

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
        List<Double> dataWithoutZero = data;
        //dataWithoutZero.replaceAll();
        Collections.replaceAll(dataWithoutZero, 0.0, Collections.max(dataWithoutZero)-1);
        minIndex = dataWithoutZero.lastIndexOf(Collections.min(dataWithoutZero));
        min = dataWithoutZero.get(minIndex);

        maxIndex = data.indexOf(Collections.max(data));
        max = data.get(maxIndex);
        double point = search((max-min)*0.632,  data);
        stalaCzasowa = data.indexOf(point+min)-minIndex;
        return stalaCzasowa;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public int getMinIndex() {
        return minIndex;
    }

    public void setMinIndex(int minIndex) {
        this.minIndex = minIndex;
    }

    public int getMaxIndex() {
        return maxIndex;
    }

    public void setMaxIndex(int maxIndex) {
        this.maxIndex = maxIndex;
    }

    public double getStalaCzasowa() {
        return stalaCzasowa;
    }

    public void setStalaCzasowa(double stalaCzasowa) {
        this.stalaCzasowa = stalaCzasowa;
    }
}
