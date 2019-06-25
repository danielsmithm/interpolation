package br.ufrn.interpolation.jmh;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Warmup;

import br.ufrn.interpolation.domain.sample.SampleCSVParserTest;

public class SampleCSVParserUsingExecutorServiceAndCallableTests {

	@Benchmark
    @Fork(value = 1, warmups = 2)
    @BenchmarkMode(Mode.AverageTime)
    @Warmup(iterations = 1)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void run() throws IOException {
        SampleCSVParserTest sampleCSVParserTest = new SampleCSVParserTest();

        sampleCSVParserTest.testParseCSVUsingExecutorAndCallable();
    }
	
}
