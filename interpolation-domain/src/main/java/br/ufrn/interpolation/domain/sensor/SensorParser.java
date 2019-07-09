package br.ufrn.interpolation.domain.sensor;

import br.ufrn.interpolation.domain.sensor.Sensor;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;

public interface SensorParser {
    Sensor parseToSensor(String row);

    Collection<Sensor> parseFile(Path filePath) throws IOException;
}
