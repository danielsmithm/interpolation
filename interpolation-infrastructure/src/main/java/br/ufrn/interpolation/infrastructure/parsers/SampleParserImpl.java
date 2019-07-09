package br.ufrn.interpolation.infrastructure.parsers;

import br.ufrn.interpolation.domain.sample.Sample;
import br.ufrn.interpolation.domain.sample.SampleParser;
import br.ufrn.interpolation.domain.utils.CsvParser;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

public class SampleParserImpl implements SampleParser {

    /**
     * Constant value that holds the separator regex for the CSV file.
     */
    private static final String VALUE_SEPARATOR = ",";

    private static final short SENSOR_NAME_POSITION = 0;
    private static final short VARIABLE_POSITION = 1;
    private static final short UNITS_POSITION = 2;
    private static final short TIMESTAMP_POSITION = 3;
    private static final short VALUE_POSITION = 4;

    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);

    private CsvParser parsingStrategy;

    public SampleParserImpl(CsvParser parsingStrategy) {
        this.parsingStrategy = parsingStrategy;
    }

    /**
     * Parse a row from the csv file to a <code>Sample</code>.
     *
     * @param row a row from the csv file
     * @return returns a sample from the row parsed.
     * @see Sample
     */
    @Override
    public Sample parseToSample(String row) {

        if (row == null) {
            throw new IllegalArgumentException("The row argument cannot be null.");
        }

        String[] rowSplited = row.split(VALUE_SEPARATOR);

        if (rowSplited.length != 5) {
            throw new IllegalArgumentException("A row containing 5 comma separated values was expected ");
        }

        String sensorName = rowSplited[SENSOR_NAME_POSITION];
        String variable = rowSplited[VARIABLE_POSITION];
        String units = rowSplited[UNITS_POSITION];
        String timestampAsString = rowSplited[TIMESTAMP_POSITION];
        String valueAsString = rowSplited[VALUE_POSITION];

        return new Sample(sensorName, variable, units, LocalDateTime.parse(timestampAsString, DATE_TIME_FORMATTER), Double.parseDouble(valueAsString));
    }

    /**
     * Read the file from the path and returns a collection of the parsed samples.
     *
     * @param filePath the path of the file.
     * @return a collection of the parsed samples
     * @throws IOException if any exception occur with the file reading.
     */
    @Override
    public Collection<Sample> parseFile(Path filePath) throws IOException {
        return parsingStrategy.parseFile(filePath, this::parseToSample);
    }

}
