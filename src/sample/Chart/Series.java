package sample.Chart;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by E6420 on 2016-12-28.
 */
public class Series {
    final int PLACES_AFTER_DOT = 1;
    private List<List<Double>> charts = new ArrayList<List<Double>>();
    private String line;

    public Series(int countOfCharts) {
        addCharts(countOfCharts);
    }

    public void generateSeries(String text){
        this.line = text;
        setCharts();
    }

    private void addCharts(int countOfCharts){
        for (int i = 0; i<countOfCharts; i++){
            charts.add(i, new ArrayList<Double>());
        }
    }

    private void setCharts(){
        if (line.startsWith(" ")){
            line = line.substring(1);
        }
        //addCharts(5);
        for(List<Double> chart : charts)
            chart.add(getNumber());
        //charts.get(0).add

    }

    private double getNumber(){
        line = line.replace(',', '.');
        line = line.replaceAll("[^\\d.]", "");
        int index = line.indexOf('.');
        double number = Double.parseDouble(line.substring(0, index+PLACES_AFTER_DOT+1));
        line = line.substring(index+PLACES_AFTER_DOT+1);
        return number;
    }

    public List<List<Double>> getCharts() {
        return charts;
    }
}
