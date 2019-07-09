package br.ufrn.interpolation.domain.sensor;

import br.ufrn.interpolation.infrastructure.parsers.SensorParserImpl;
import br.ufrn.interpolation.infrastructure.utils.CsvParserSequential;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(JUnit4.class)
public class SensorCSVParserTests {

    @Test
    public void testParseCsvRowToSensor() {
        String sensorRow = "99.5923600049,True,\"{'third_party': True, 'fancy_name': 'Wunderground API', 'web_display_name': 'Wunderground API (Third Party)', 'db_name': 'Wunderground', 'document': ''}\",None,ICRAWCRO3,2018-08-29 18:09:00,Weather,-1.778519,54.965347";

        //base_height,active,source,sensor_height,name,latest,type,lon,lat
        Sensor sensor = new SensorParserImpl(new CsvParserSequential()).parseToSensor(sensorRow);

        //TODO: Source
        assertEquals(99.5923600049, sensor.getBaseHeight(),0.0000000000000);
        assertEquals(true,sensor.isActive());
        assertEquals(0.0, sensor.getSensorHeight(),0.0000000000000);
        assertEquals("ICRAWCRO3", sensor.getName());
        assertEquals("2018-08-29 18:09:00",sensor.getLatest().format(DateTimeFormatter.ofPattern(SensorParserImpl.DATE_TIME_PATTERN)));
        assertEquals(-1.778519,sensor.getLongitude(),0.0000000000000);
        assertEquals(54.965347, sensor.getLatitude(),0.0000000000000);

    }

    @Test
    public void testParseCsvSensorFileToSensorCollection() throws Exception {
        Path path = Paths.get("src","main","resources", "samples", "sensors.csv");

        Collection<Sensor> sensors = new SensorParserImpl(new CsvParserSequential()).parseFile(path);

        assertNotNull(sensors);
    }
}
