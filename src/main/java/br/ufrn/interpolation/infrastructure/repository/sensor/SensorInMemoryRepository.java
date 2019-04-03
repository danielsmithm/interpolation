package br.ufrn.interpolation.infrastructure.repository.sensor;

import br.ufrn.interpolation.domain.sensor.Sensor;
import br.ufrn.interpolation.domain.sensor.SensorRepository;
import br.ufrn.interpolation.domain.DistanceCalculator;
import br.ufrn.interpolation.infrastructure.utils.CsvParserSequential;

import java.io.IOException;
import java.nio.file.Path;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class SensorInMemoryRepository implements SensorRepository {

    private final Collection<Sensor> sensorCollection;

    public SensorInMemoryRepository(Collection<Sensor> sensorCollection) {
        this.sensorCollection = new CopyOnWriteArrayList<>(sensorCollection);
    }

    public static SensorInMemoryRepository fromFile(Path filePath) throws IOException {
        SensorCSVParser sensorCSVParser = new SensorCSVParser(new CsvParserSequential());

        return new SensorInMemoryRepository(sensorCSVParser.parseFile(filePath));
    }

    @Override
    public List<AbstractMap.SimpleEntry<Sensor,Double>> findSensorsWithinDistance(double latitude, double longitude, double maximumDistanceInMeters){
        return sensorCollection.stream()
                .map(sensor -> new AbstractMap.SimpleEntry<>(sensor, DistanceCalculator.distanceInMeters(latitude, sensor.getLatitude(), longitude, sensor.getLongitude())))
                .filter(sensorDistancePair -> sensorDistancePair.getValue() < maximumDistanceInMeters)
                .collect(Collectors.toList());
    }

}
