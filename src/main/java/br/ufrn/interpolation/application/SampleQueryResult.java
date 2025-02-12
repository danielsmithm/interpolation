package br.ufrn.interpolation.application;

import java.util.List;

public class SampleQueryResult {

    private double temperatureValue;

    private List<SensorDTO> sensorDTOS;

    public SampleQueryResult(double temperatureValue, List<SensorDTO> sensorDTOS) {
        this.temperatureValue = temperatureValue;
        this.sensorDTOS = sensorDTOS;
    }

    public double getTemperatureValue() {
        return temperatureValue;
    }

    public List<SensorDTO> getSensorDTOS() {
        return sensorDTOS;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SampleQueryResult that = (SampleQueryResult) o;

        return Double.compare(that.temperatureValue, temperatureValue) == 0;
    }

    @Override
    public int hashCode() {
        long temp = Double.doubleToLongBits(temperatureValue);
        return (int) (temp ^ (temp >>> 32));
    }
}
