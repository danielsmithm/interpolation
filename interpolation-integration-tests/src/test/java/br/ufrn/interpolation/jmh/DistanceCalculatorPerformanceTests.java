package br.ufrn.interpolation.jmh;

import br.ufrn.interpolation.domain.util.DistanceCalculatorTests;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

public class DistanceCalculatorPerformanceTests {

    @Benchmark
    @Fork(value = 1, warmups = 2)
    @BenchmarkMode(Mode.AverageTime)
    @Warmup(iterations = 1)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void run(){
        new DistanceCalculatorTests().calculateDistanceOfPointsCloseToIMD();
    }
}
