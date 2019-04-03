package br.ufrn.interpolation.jmh;

import br.ufrn.interpolation.domain.sensor.SensorCSVParserTests;
import org.openjdk.jmh.annotations.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class SensorCSVParsingTests {

    public void run() throws Exception {
        SensorCSVParserTests sampleCSVParserTest = new SensorCSVParserTests();

        sampleCSVParserTest.testParseCsvSensorFileToSensorCollection();
    }

}
