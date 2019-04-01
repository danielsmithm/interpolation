package br.ufrn.interpolation.application;

import br.ufrn.interpolation.domain.sample.Sample;
import br.ufrn.interpolation.domain.sample.SampleRepository;
import br.ufrn.interpolation.domain.sensor.Sensor;
import br.ufrn.interpolation.domain.sensor.SensorRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static br.ufrn.interpolation.domain.sample.SampleSpecification.sampleCollectedAtExactDate;
import static br.ufrn.interpolation.domain.sample.SampleSpecification.sampleIsOfSelectedSensors;
import static br.ufrn.interpolation.domain.sample.SampleSpecification.variableIsTemperature;

public class TemperatureReporter {

    private static final int MAXIMUM_DISTANCE = 10000;

    private final SampleRepository sampleRepository;
    private final SensorRepository sensorRepository;

    public TemperatureReporter(SampleRepository sampleRepository, SensorRepository sensorRepository) {
        this.sampleRepository = sampleRepository;
        this.sensorRepository = sensorRepository;
    }

    public TemperatureQueryResult evaluateTemperature(double latitude, double longitude, LocalDateTime dateTime) {

        List<AbstractMap.SimpleEntry<Sensor, Double>> sensorsWithinDistance = sensorRepository.findSensorsWithinDistance(latitude, longitude, MAXIMUM_DISTANCE);

        List<String> sensorNames = sensorsWithinDistance.stream()
                .map(AbstractMap.SimpleEntry::getKey)
                .map(Sensor::getName)
                .collect(Collectors.toList());

        List<Sample> samplesFromClosestSensors = sampleRepository.findFromPredicate(sampleIsOfSelectedSensors(sensorNames)
                .and(variableIsTemperature())
                .and(sampleCollectedAtExactDate(dateTime)));

        double temperature = calculateMedian(samplesFromClosestSensors);

        return assembleResponse(sensorsWithinDistance, temperature);

    }

    private TemperatureQueryResult assembleResponse(List<AbstractMap.SimpleEntry<Sensor, Double>> sensorsWithinDistance, double temperature) {
        List<TemperatureQueryResult.SensorDTO> sensorsAsDTO = sensorsWithinDistance.stream()
                .map(sensorAndDistance -> {
                    Sensor sensor = sensorAndDistance.getKey();

                    return new TemperatureQueryResult.SensorDTO(sensor.getName(), sensor.getLatitude(), sensor.getLongitude(), sensorAndDistance.getValue());
                })
                .collect(Collectors.toList());

        return new TemperatureQueryResult(temperature, sensorsAsDTO);
    }

    private double calculateMedian(List<Sample> samplesFromClosestSensors) {

        if (samplesFromClosestSensors.isEmpty()) {
            throw new IllegalArgumentException("No data was found.");
        }

        //Order and take the median value
        samplesFromClosestSensors.sort(Comparator.comparingDouble(Sample::getValue));

        int samplesCount = samplesFromClosestSensors.size();

        double median = 0.0;
        if (samplesCount % 2 == 0) {
            median = BigDecimal.valueOf(samplesFromClosestSensors.get((samplesCount / 2) - 1).getValue())
                    .add(BigDecimal.valueOf(samplesFromClosestSensors.get((samplesCount / 2)).getValue()))
                    .divide(BigDecimal.valueOf(2))
                    .doubleValue();
        }

        if (samplesCount % 2 == 1) {
            median = samplesFromClosestSensors.get((samplesCount % 2)).getValue();
        }

        return median;

    }

}
