package br.ufrn.interpolation.domain.sample;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Predicate;

public class SampleSpecification {

    public static Predicate<Sample> sampleCollectedAtExactDate(LocalDateTime dateTime) {
        return sample -> sample.getTimestamp().equals(dateTime);
    }

    public static Predicate<Sample> variableIsTemperature() {
        return sample -> "Temperature".equals(sample.getVariable());
    }

    public static Predicate<Sample> sampleIsOfSelectedSensors(List<String> sensorNames) {
        return sample -> sensorNames.contains(sample.getSensorName());
    }

}
