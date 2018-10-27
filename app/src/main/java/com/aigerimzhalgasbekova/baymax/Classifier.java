package com.aigerimzhalgasbekova.baymax;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by aigerimzhalgasbekova on 14/10/2018.
 */

public class Classifier {

    private final Metrics metrics;

    public Classifier(Metrics metrics) {
        this.metrics = metrics;
    }

    private double scoreMetric(String userInput) {
        if (userInput.equals("YES")){
            return 9.0;
        }
        return 1.0;
    }

    private List<Double> giveLevels(Set<Double> prices, double[] range) {
        double average = (range[1] - range[0])/2;
        List<Double> levels = new LinkedList<>();
        for (Double price : prices) {
            if (price <= range[0]) {
                levels.add(9.0);
            } else if (price < average && price > range[0]) {
                levels.add(7.0);
            } else if (price == average) {
                levels.add(5.0);
            } else if (price > average && price < range[1]) {
                levels.add(3.0);
            } else {
                levels.add(1.0);
            }
        }
        return levels;
    }

    private List<Double> scoreMetric(Collection<String> values, String userInput) {
        List<Double> scores = new LinkedList<>();
        for (String value : values) {
            if (value.equals(userInput)) {
                scores.add(9.0);
            } else {
                scores.add(1.0);
            }
        }
        return scores;
    }

    public double compare(double a, double b){
        double val = 0;
        if(a==b){
            val = 1.00;
        }
        else if (a==9 && b==7){
            val = 3.00;
        }
        else if (a==9 && b==5){
            val = 5.00;
        }
        else if (a==9 && b==3){
            val = 7.00;
        }
        else if (a>b && b==1){
            val = a;
        }
        else if (a==1 && a<b){
            val = 1.0 / b;
        }
        else if (a==7 && b==9){
            val = 1.0 / 3.0;
        }
        else if (a==7 && b==5){
            val = 3.00;
        }
        else if (a==7 && b==3){
            val = 5.00;
        }
        else if (a==5 && b==9){
            val = 1.0 / 5.0;
        }
        else if (a==5 && b==7){
            val = 1.0 / 3.0;
        }
        else if (a==5 && b==3){
            val = 3.00;
        }
        else if (a==3 && b==9){
            val = 1.0 / 7.0;
        }
        else if (a==3 && b==7){
            val = 1.0 / 5.0;
        }
        else if (a==3 && b==5){
            val = 1.0 / 3.0;
        }
        return val;
    }

    public ArrayList<Double> compArr(List<Double> a){
        ArrayList <Double> c = new ArrayList<>();
        double r = 0;
        for (int i=0; i<a.size(); i++){
            for (int j= i+1; j<a.size(); j++){
                if (i==a.size()-1){
                    break;
                } else {
                    r = compare(a.get(i), a.get(j));
                }

                c.add(r);
            }
        }
        Log.i("compare", c + "");
        return c;
    }

    public List<Double> setPriority(List<Double> metricValues) {
        List<Double> attr = new LinkedList<>();
        AHP ahp = new AHP(metricValues.size());

        int d = ahp.getNrOfPairwiseComparisons();

        double compArray[] = ahp.getPairwiseComparisonArray();

        ArrayList<Double> metrics = compArr(metricValues);
        Log.i("metrics", ""+metrics);

        // Set the pairwise comparison values
        for (int i=0; i<metrics.size(); i++){
            compArray[i] = metrics.get(i);
        }

        ahp.setPairwiseComparisonArray(compArray);

        //add the values if the consistency is acceptable
        if (ahp.getConsistencyRatio() < 10){
            double val;
            for (int k=0; k<ahp.getWeights().length; k++) {
                val = ahp.getWeights()[k]*100;
                attr.add(k, val);
            }
        } else {
            Log.i("Consistency is", "not acceptable");
        }
        return attr;
    }

    public double getAverage(List<Double> b){
        int sum = 0;
        for (int i=0; i<b.size(); i++){
            sum += b.get(i);
        }
        Log.i("averages", sum / b.size() + "");
        return sum / b.size();
    }

    public HashMap<Integer, Double> prioritirize(){
        HashMap<Integer, Double> productsPriority = new HashMap<>();
        List<Double> priceLevels = giveLevels(metrics.getMetricValues(), new double[]{1.8, 7.5});
        //Log.i("price levels", priceLevels+"");
        List<Double> bioLevels = scoreMetric(metrics.getMetricValues("Bio"), metrics.getBio());
        Log.i("bio levels", bioLevels+"");
        List<Double> discountLevels = scoreMetric(metrics.getMetricValues("Discount"),
                                                  metrics.getDiscount());
        List<Double> metrPriority = setPriority(Arrays.asList(getAverage(priceLevels),
                                                              getAverage(bioLevels),
                                                              getAverage(discountLevels)));
        List<Double> pricePriority = setPriority(priceLevels);
        List<Double> bioPriority = setPriority(bioLevels);
        List<Double> discountPriority = setPriority(discountLevels);
        double pr;
        Log.i("bio priority", bioPriority+"");
        Log.i("price priority", pricePriority+"");
        Log.i("discount priority", discountPriority+"");
        for (int i=0; i<bioPriority.size(); i++){
            pr = metrPriority.get(0)*pricePriority.get(i) + metrPriority.get(1)*bioPriority.get(i)
                    + metrPriority.get(2)*discountPriority.get(i);
            productsPriority.put(i, pr);
        }
        return productsPriority;
    }
}
