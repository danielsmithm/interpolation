package br.ufrn.interpolation.jmh;

import br.ufrn.interpolation.application.SampleReporterTests;
import org.openjdk.jmh.annotations.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;


public class SampleReporterJmhTests {

    SampleReporterTests temperatureReporterTests = new SampleReporterTests();

    {
        try {
            temperatureReporterTests.setup();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    public void run() throws IOException {
        temperatureReporterTests.testCalculateTemperature();
    }

}
