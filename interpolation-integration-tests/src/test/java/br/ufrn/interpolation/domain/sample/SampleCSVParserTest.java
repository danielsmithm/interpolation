package br.ufrn.interpolation.domain.sample;

import br.ufrn.interpolation.infrastructure.parsers.SampleParserImpl;
import br.ufrn.interpolation.infrastructure.utils.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(JUnit4.class)
public class SampleCSVParserTest {

    /**
     * This test case parses a sample row from the CSV file into a <code>Sample</code>.
     * <p>
     * <br>
     * It also assert if the values were parsed correctly.
     * </br>
     *
     * @see Sample
     */
    @Test
    public void testParseSampleFromCSV() {
        String csv = "ICRAWCRO3,Temperature,Celsius,2018-07-31 23:04:00,15.8";

        Sample sample = new SampleParserImpl(new CsvParserSequential()).parseToSample(csv);

        assertEquals("ICRAWCRO3", sample.getSensorName());
        assertEquals("Temperature", sample.getVariable());
        assertEquals("Celsius", sample.getUnits());
        assertEquals("2018-07-31 23:04:00", sample.getTime().format(DateTimeFormatter.ofPattern(SampleParserImpl.DATE_TIME_PATTERN)));
        assertEquals(15.8, sample.getValue(), 0.0);

    }

    @Test
    public void testParseCSVUsingSequentialStrategy() throws IOException {
        Path path = Paths.get("src", "main", "resources", "samples", "data.csv");

        Collection<Sample> samples = new SampleParserImpl(new CsvParserSequential()).parseFile(path);

        assertNotNull(samples);
        assertEquals(953768, samples.size());

    }

    @Test
    public void testParseCSVUsingThreadStrategy() throws IOException {
        Path path = Paths.get("src", "main", "resources", "samples", "data.csv");

        Collection<Sample> samples = new SampleParserImpl(new CsvParserThreads()).parseFile(path);

        assertNotNull(samples);
        assertEquals(953768, samples.size());

    }

    @Test
    public void testParseCSVUsingParallelStreamStrategy() throws IOException {
        Path path = Paths.get("src", "main", "resources", "samples", "data.csv");

        Collection<Sample> samples = new SampleParserImpl(new CsvParserParallelStream()).parseFile(path);

        assertNotNull(samples);
        assertEquals(953768, samples.size());

    }

    @Test
    public void testParseCSVUsingParallelStreamStrategyAnd12ThreadsInForkJoinPool() throws IOException {
        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "12");

        Path path = Paths.get("src", "main", "resources", "samples", "data.csv");

        Collection<Sample> samples = new SampleParserImpl(new CsvParserParallelStream()).parseFile(path);

        System.out.println(samples.hashCode());
        assertNotNull(samples);
        assertEquals(953768, samples.size());

    }

    @Test
    public void testParseCSVUsingForkJoinRecursiveActionStrategy() throws IOException {
        Path path = Paths.get("src", "main", "resources", "samples", "data.csv");

        Collection<Sample> samples = new SampleParserImpl(new CsvParserForkJoinRecursiveAction(ForkJoinPool.commonPool())).parseFile(path);

        assertNotNull(samples);
        assertEquals(953768, samples.size());

    }

    @Test
    public void testParseCSVUsingForkJoinRecursiveTaskStrategy() throws IOException {
        Path path = Paths.get("src", "main", "resources", "samples", "data.csv");

        Collection<Sample> samples = new SampleParserImpl(new CsvParserForkRecursiveTask(ForkJoinPool.commonPool())).parseFile(path);

        assertNotNull(samples);
        assertEquals(953768, samples.size());

    }

    @Test
    public void testParseCSVUsingExecutorAndRunnable() throws IOException {
        Path path = Paths.get("src", "main", "resources", "samples", "data.csv");

        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        try {
            Collection<Sample> samples = new SampleParserImpl(new CsvParserExecutorUsingRunnable(executorService)).parseFile(path);

            assertNotNull(samples);
            assertEquals(953768, samples.size());
        } finally {
            executorService.shutdownNow();
        }

    }

    @Test
    public void testParseCSVUsingExecutorAndCallable() throws IOException {
        Path path = Paths.get("src", "main", "resources", "samples", "data.csv");

        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        try {
            Collection<Sample> samples = new SampleParserImpl(new CsvParserExecutorUsingCallable(executorService)).parseFile(path);

            assertNotNull(samples);
            assertEquals(953768, samples.size());

        } finally {
            executorService.shutdownNow();
        }
    }

}
