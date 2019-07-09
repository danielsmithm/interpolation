package br.ufrn.interpolation.domain.sample;

import java.time.LocalDateTime;

public class Sample {

    private final String sensorName;
    private final String variable;
    private final String units;
    private final LocalDateTime time;
    private final double value;

    public Sample(String sensorName, String variable, String units, LocalDateTime time, double value) {
        this.sensorName = sensorName;
        this.variable = variable;
        this.units = units;
        this.time = time;
        this.value = value;
    }

    public String getSensorName() {
        return sensorName;
    }

    public String getVariable() {
        return variable;
    }

    public String getUnits() {
        return units;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public double getValue() {
        return value;
    }
}
