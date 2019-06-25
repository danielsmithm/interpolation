package br.ufrn.interpolation.jcstress;

import br.ufrn.interpolation.application.SampleQueryResult;
import br.ufrn.interpolation.application.SampleReporter;
import br.ufrn.interpolation.infrastructure.repository.memory.SampleInMemoryRepository;
import br.ufrn.interpolation.infrastructure.repository.memory.SensorInMemoryRepository;
import org.openjdk.jcstress.annotations.*;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import org.openjdk.jcstress.infra.results.II_Result;

public class SampleReporterTests {

    @State
    public static class MyState {

        public SampleReporter objectUnderTests = getTemperatureReporter();

        public MyState() {
        }

        public SampleReporter getTemperatureReporter() {
            try {
                return new SampleReporter(SampleInMemoryRepository.fromFile(
                        Paths.get("src", "main", "resources", "src/main/resources/samples", "data.csv")),
                        SensorInMemoryRepository.fromFile(Paths.get("src", "main", "resources", "src/main/resources/samples", "sensors.csv")));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

    }

    @Description("test racily getting temperature")
    @JCStressTest
    @Outcome(id = {"642318336"}, expect = Expect.ACCEPTABLE)
    @Outcome(id = {"642318336, 642318336"}, expect = Expect.ACCEPTABLE)
    public static class StressTest2 {

        @Actor
        public void actor1(MyState myState, II_Result r) {
            double latitude = 54.964936;
            double longitude = -1.779070;

            SampleQueryResult queryResult = myState.objectUnderTests.evaluateTemperature(latitude, longitude, LocalDateTime.of(2018, 7, 31, 23, 4));

            r.r1 = queryResult.hashCode();
        }

        @Actor
        public void actor2(MyState myState, II_Result r) {
            double latitude = 54.964936;
            double longitude = -1.779070;

            SampleQueryResult queryResult = myState.objectUnderTests.evaluateTemperature(latitude, longitude, LocalDateTime.of(2018, 7, 31, 23, 4));

            r.r2 = queryResult.hashCode();
        }

    }
}
