package br.ufrn.interpolation.application;

import br.ufrn.interpolation.infrastructure.repository.sample.SampleInMemoryRepository;
import br.ufrn.interpolation.infrastructure.repository.sensor.SensorInMemoryRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class TemperatureReporterTests {

    private TemperatureReporter temperatureReporter;

    @Before
    public void setup() throws IOException {
        this.temperatureReporter = new TemperatureReporter(SampleInMemoryRepository.fromFile(
                Paths.get("src","test","resources", "samples", "data.csv")),
                SensorInMemoryRepository.fromFile(Paths.get("src","test","resources", "samples", "sensors.csv")));
    }

    @Test
    public void testCalculateTemperature() {

        double latitude = 54.964936;
        double longitude = -1.779070;

        TemperatureQueryResult queryResult = temperatureReporter.evaluateTemperature(latitude, longitude, LocalDateTime.of(2018, 7, 31, 23, 4));

        assertEquals(15.7, queryResult.getTemperatureValue(),0.000000000000);

    }

}
