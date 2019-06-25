package br.ufrn.interpolation.web.controller;

import br.ufrn.interpolation.application.SampleQueryResult;
import br.ufrn.interpolation.application.SampleReporter;
import br.ufrn.interpolation.domain.sample.SampleRepository;
import br.ufrn.interpolation.domain.sensor.SensorRepository;
import br.ufrn.interpolation.infrastructure.parsers.SampleCSVParser;
import br.ufrn.interpolation.infrastructure.repository.memory.SampleInMemoryRepository;
import br.ufrn.interpolation.infrastructure.parsers.SensorCSVParser;
import br.ufrn.interpolation.infrastructure.repository.memory.SensorInMemoryRepository;
import br.ufrn.interpolation.infrastructure.utils.CsvParser;
import br.ufrn.interpolation.infrastructure.utils.CsvParserParallelStream;
import br.ufrn.interpolation.infrastructure.utils.CsvParserSequential;
import br.ufrn.interpolation.infrastructure.utils.CsvParserThreads;
import br.ufrn.interpolation.web.controller.request.TemperatureReportRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * Controller that permits to test the performance of the application over HTTP.
 */
@RestController
@RequestMapping("/benchmark")
public class SampleReportJMeterController {

    /**
     * Tests the application using an sequential parser for the CSV file.
     *
     * @param request
     * @return
     * @throws IOException
     */
    @PostMapping(value = "/evaluate/sequential", consumes = "application/json" )
    public SampleQueryResult getTemperatureUsingSequentialParser(@RequestBody TemperatureReportRequest request) throws IOException {
        SampleReporter sampleReporter = createSampleReporter(new CsvParserSequential());

        return sampleReporter.evaluateTemperature(request.getLatitude(), request.getLongitude(), request.getLocalDateTime());
    }

    /**
     * Tests the application using a parallel stream based parser.
     *
     * @param request
     * @return
     * @throws IOException
     */
    @PostMapping(value = "/evaluate/parallel", consumes = "application/json" )
    public SampleQueryResult getTemperatureUsingParallelParser(@RequestBody TemperatureReportRequest request) throws IOException {
        SampleReporter sampleReporter = createSampleReporter(new CsvParserParallelStream());

        return sampleReporter.evaluateTemperature(request.getLatitude(), request.getLongitude(), request.getLocalDateTime());
    }

    /**
     * Tests the application using a simple thread based parser (Java 5- aproach)
     *
     * @param request
     * @return
     * @throws IOException
     */
    @PostMapping(value = "/evaluate/thread", consumes = "application/json" )
    public SampleQueryResult getTemperatureUsingThreadParser(@RequestBody TemperatureReportRequest request) throws IOException {
        SampleReporter sampleReporter = createSampleReporter(new CsvParserThreads());

        return sampleReporter.evaluateTemperature(request.getLatitude(), request.getLongitude(), request.getLocalDateTime());
    }

    //JUST Setup methods.

    private SampleReporter createSampleReporter(CsvParser parsingStrategy) throws IOException {
        SensorCSVParser sensorCSVParser = new SensorCSVParser(parsingStrategy);
        SampleCSVParser sampleCSVParser = new SampleCSVParser(parsingStrategy);

        return new SampleReporter(createSampleRepository(sampleCSVParser), createSensorRepository(sensorCSVParser));
    }

    private SampleRepository createSampleRepository(SampleCSVParser sampleCSVParser) throws IOException {
        return new SampleInMemoryRepository(sampleCSVParser.parseFile(Paths.get("src","main","resources", "samples", "data.csv")));
    }

    private SensorRepository createSensorRepository(SensorCSVParser sensorCSVParser) throws IOException {
        return new SensorInMemoryRepository(sensorCSVParser.parseFile(Paths.get("src","main","resources", "samples", "sensors.csv")));
    }


}
