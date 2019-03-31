package br.ufrn.interpolation.jmh;

import br.ufrn.interpolation.application.TemperatureReporterTests;
import org.openjdk.jmh.annotations.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
public class TemperatureReporterJmhTests {

    TemperatureReporterTests temperatureReporterTests = new TemperatureReporterTests();

    {
        try {
            temperatureReporterTests.setup();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    @Benchmark
    @Fork(value = 1, warmups = 2)
    @BenchmarkMode(Mode.AverageTime)
    @Warmup(iterations = 1)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void run() throws IOException {
        temperatureReporterTests.testCalculateTemperature();
    }

}
