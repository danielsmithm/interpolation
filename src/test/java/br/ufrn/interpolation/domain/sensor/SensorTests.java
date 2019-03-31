package br.ufrn.interpolation.domain.sensor;

import br.ufrn.interpolation.infrastructure.repository.sensor.SensorCSVParser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class SensorTests {


    @Test
    public void testCreateSensor() {
        String sensorRow = "99.5923600049,True,\"{'third_party': True, 'fancy_name': 'Wunderground API', 'web_display_name': 'Wunderground API (Third Party)', 'db_name': 'Wunderground', 'document': ''}\",None,ICRAWCRO3,2018-08-29 18:09:00,Weather,-1.778519,54.965347";

        //base_height,active,source,sensor_height,name,latest,type,lon,lat

        Sensor sensor = new Sensor(99.5923600049, true, null, 0.0, "ICRAWCRO3", LocalDateTime.parse("2018-08-29 18:09:00", DateTimeFormatter.ofPattern(SensorCSVParser.DATE_TIME_PATTERN)),"Weather",-1.778519,54.965347);

        assertEquals(99.5923600049, sensor.getBaseHeight(),0.00000000000);
        assertEquals(true, sensor.isActive());
        assertEquals(0.0, sensor.getSensorHeight(), 0.00000000000);

    }
}
