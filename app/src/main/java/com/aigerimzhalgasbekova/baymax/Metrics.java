package com.aigerimzhalgasbekova.baymax;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Created by aigerimzhalgasbekova on 14/10/2018.
 */

public class Metrics {

    private final String[] userInput;
    private int[] userLocation;
    private final Set<Map<String, Object>> parterProoducts;

    public Metrics(Builder builder) {
        this.userInput = builder.userInput;
        this.parterProoducts = builder.parterProoducts;
    }

    public String[] getUserInput() {
        return userInput;
    }

    public Set<Map<String, Object>> getParterProoducts() {
        return parterProoducts;
    }

    public String getCheap() {
        return userInput[0];
    }

    public String getBio() {
        return userInput[1];
    }

    public String getDiscount() {
        return userInput[2];
    }

    public List<String> getMetricValues(String key) {
        final List<String> values = new LinkedList<>();
        for (Map<String, Object> product : getParterProoducts()) {
            values.add((String) product.get(key));
        }
        return values;
    }

    public Set<Double> getMetricValues() {
        final Set<Double> names = new LinkedHashSet<>();
        for (Map<String, Object> product : getParterProoducts()) {
            names.add((Double) product.get("Price"));
        }
        return names;
    }

    public static Builder with() {
        return new Builder();
    }

    public static class Builder {
        private String[] userInput;
        private Set<Map<String, Object>> parterProoducts;

        public Builder userInput(String[] userInput) {
            this.userInput = userInput;
            return this;
        }

        public Builder partnerProducts(Set<Map<String, Object>> parterProoducts) {
            this.parterProoducts = parterProoducts;
            return this;
        }

        public Metrics build() {
            return new Metrics(this);
        }
    }
}
