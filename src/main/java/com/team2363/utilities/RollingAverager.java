package com.team2363.utilities;

import java.util.ArrayList;
import java.util.List;

public class RollingAverager {

    private List<Double> values;

    public RollingAverager(int queueSize) {
        values = new ArrayList<>();
        for (int i = 0; i < queueSize; i++) {
            values.add(0.0);
        }
    }

    public double getNewAverage(double newValue) {
        values.add(newValue);
        values.remove(0);

        return values.stream().mapToDouble(d -> d).average().getAsDouble();
    }
}
