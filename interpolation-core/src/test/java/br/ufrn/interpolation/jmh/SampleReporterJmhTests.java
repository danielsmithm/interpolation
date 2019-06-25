package br.ufrn.interpolation.jmh;

import br.ufrn.interpolation.application.SampleReporterTests;
import org.openjdk.jmh.annotations.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
public class SampleReporterJmhTests {

    SampleReporterTests temperatureReporterTests = new SampleReporterTests();

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
