package br.ufrn.interpolation.jmh;

import br.ufrn.interpolation.domain.sample.SampleCSVParserTest;
import org.openjdk.jmh.annotations.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class SampleCSVParsingTests {

    @Benchmark
    @Fork(value = 1, warmups = 2)
    @BenchmarkMode(Mode.AverageTime)
    @Warmup(iterations = 1)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void run() throws IOException {
        SampleCSVParserTest sampleCSVParserTest = new SampleCSVParserTest();

        sampleCSVParserTest.testParseFromSmallCSVFile();
    }

}
