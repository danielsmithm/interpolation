package br.ufrn.interpolation.jcstress;

import br.ufrn.interpolation.application.TemperatureQueryResult;
import br.ufrn.interpolation.application.TemperatureReporter;
import br.ufrn.interpolation.infrastructure.repository.sample.SampleInMemoryRepository;
import br.ufrn.interpolation.infrastructure.repository.sensor.SensorInMemoryRepository;
import org.openjdk.jcstress.annotations.*;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import org.openjdk.jcstress.infra.results.II_Result;

public class TemperatureReporterTests {

    @State
    public static class MyState {

        public TemperatureReporter objectUnderTests = getTemperatureReporter();

        public MyState() {
        }

        public TemperatureReporter getTemperatureReporter() {
            try {
                return new TemperatureReporter(SampleInMemoryRepository.fromFile(
                        Paths.get("src", "test", "resources", "samples", "data.csv")),
                        SensorInMemoryRepository.fromFile(Paths.get("src", "test", "resources", "samples", "sensors.csv")));
            } catch (IOException e) {
                throw new RuntimeException();
            }
        }

    }

    @Description("test racily getting temperature")
    @JCStressTest
    @Outcome(id = "[642318336, 642318336]", expect = Expect.ACCEPTABLE, desc = "get back expected character 'a' and character 'b'")
    public static class StressTest2 {

        @Actor
        public void actor1(MyState myState, II_Result r) {
            double latitude = 54.964936;
            double longitude = -1.779070;

            TemperatureQueryResult queryResult = myState.objectUnderTests.evaluateTemperature(latitude, longitude, LocalDateTime.of(2018, 7, 31, 23, 4));

            r.r1 = queryResult.hashCode();
        }

        @Actor
        public void actor2(MyState myState, II_Result r) {
            double latitude = 54.964936;
            double longitude = -1.779070;

            TemperatureQueryResult queryResult = myState.objectUnderTests.evaluateTemperature(latitude, longitude, LocalDateTime.of(2018, 7, 31, 23, 4));

            r.r2 = queryResult.hashCode();
        }

    }
}
