package sample.Chart;

import com.sun.javafx.scene.control.skin.VirtualFlow;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mateusz.Blaszczak on 2016-12-28.
 */
public class Series {
//    final int PLACES_AFTER_DOT = 1;
//    private List<List<Double>> charts = new ArrayList<List<Double>>();
//    private String line;
//    //private List<Double> temp = new ArrayList<>();
//    //private double[] temp = new double[]{50.0, 50.0, 50.0, 50.0, 50.0, 50.0};
//    private boolean first = true;
//    private double nr;
//    private double[] temp = new double[6];
//
//    public Series(int countOfCharts) {
//        addCharts(countOfCharts);
//    }
//
//    public void generateSeries(String text) {
//        this.line = text;
//        setCharts();
//    }
//
//    private void addCharts(int countOfCharts) {
//        for (int i = 0; i < countOfCharts; i++) {
//            charts.add(i, new ArrayList<Double>());
//        }
//    }
//
////    private void setCharts() {
//////        temp
//////        temp.clear();
////        double nr;
////        if (line.startsWith(" ")) {
////            line = line.substring(1);
////        }
////        //addCharts(5);
////        int i = 0;
////        for (List<Double> chart : charts) {
////            nr = getNumber();
////            if (nr < 127 && nr > 1 && !second) {
////                chart.add(nr);
////                temp[i] = nr;
////            } else if (nr < temp[i] + 10 && nr > temp[i] + 10) {
////                chart.add(nr);
////                temp[i] = nr;
////            } else {
////                try {
////                    if (chart.get(i) != null) {
////                        chart.add(temp[i]);
////                    } else {
////                        chart.add(25.0);
////                        temp[i] = 25.0;
////                    }
////                } catch (Exception e) {
////                    chart.add(25.0);
////                    temp[i] = 25.0;
////                }
////            }
////            i++;
////        }
////        second = true;
////        //charts.get(0).add
////
////    }
//
//    private void setCharts() {
//        if (line.startsWith(" ")) {
//            line = line.substring(1);
//        }
//        //addCharts(5);
//        for (List<Double> chart : charts) {
//            nr = getNumber();
//            //if (nr > 20 && nr < 70) {
//            chart.add(nr);
//            // } else chart.add(0.0);
//        }
//        //charts.get(0).add
//
//    }
//
//    int i = 0;
//
//    private double getNumber() {
//        line = line.replace(',', '.');
//        line = line.replaceAll("[^\\d.]", "");
//        int index = line.indexOf('.');
//        double number = Double.parseDouble(line.substring(0, index + PLACES_AFTER_DOT + 1));
//        line = line.substring(index + PLACES_AFTER_DOT + 1);
//        if (first || (number > 1 && number < 127 && number - temp[i] < 10)) {
//            temp[i] = number;
//            i++;
//            if (i == 6) {
//                first = false;
//            }
//
//            return number;
//        } else {
//            i++;
//            return 0.0;
//        }
//
//    }
//
//    public List<List<Double>> getCharts() {
//        return charts;
//    }
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

        private double getNumber() {
        line = line.replace(',', '.');
        line = line.replaceAll("[^\\d.]", "");
        int index = line.indexOf('.');
        double number = Double.parseDouble(line.substring(0, index + PLACES_AFTER_DOT + 1));
        line = line.substring(index + PLACES_AFTER_DOT + 1);
        if (number > 10 && number < 127) {
//            temp[i] = number;
//            i++;
//            if (i == 6) {
//                first = false;
//            }
            return number;
        } else {
            //i++;
            return 0.0;
        }

    }

//    private double getNumber(){
//        line = line.replace(',', '.');
//        line = line.replaceAll("[^\\d.]", "");
//        int index = line.indexOf('.');
//        double number = Double.parseDouble(line.substring(0, index+PLACES_AFTER_DOT+1));
//        line = line.substring(index+PLACES_AFTER_DOT+1);
//        return number;
//    }

    public List<List<Double>> getCharts() {
        return charts;
    }
}
