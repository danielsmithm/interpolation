package br.ufrn.interpolation.infrastructure.parsers;

import br.ufrn.interpolation.domain.sensor.Sensor;
import br.ufrn.interpolation.domain.sensor.SensorParser;
import br.ufrn.interpolation.domain.utils.CsvParser;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SensorParserImpl implements SensorParser {

    /**
     * Constant value that holds the separator regex for the CSV file.
     */
    private static final String PARSING_REGEX = "(.{0,}),(.{0,}),\"(.{0,})\",(.{0,}),(.{0,}),(.{0,}),(.{0,}),(.{0,}),(.{0,})";
    public static final Pattern SENSOR_CSV_PATTERN = Pattern.compile(PARSING_REGEX);

    /**
     * Constant for the date time format.
     */
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);

    private static final byte BASE_HEIGHT_POSITION = 1;
    private static final byte ACTIVE_POSITION = 2;
    private static final byte SENSOR_HEIGHT_POSITION = 4;
    private static final byte NAME_POSITION = 5;
    private static final byte LATEST_POSITION = 6;
    private static final byte TYPE_POSITION = 7;
    private static final byte LONGITUDE_POSITION = 8;
    private static final byte LATITUDE_POSITION = 9;

    private CsvParser parsingStrategy;

    public SensorParserImpl(CsvParser parsingStrategy){
        this.parsingStrategy = parsingStrategy;
    }

    @Override
    public Sensor parseToSensor(String row){

        Pattern pattern = SENSOR_CSV_PATTERN;

        Matcher matcher = pattern.matcher(row);

        if(matcher.matches()){

            String baseHeightAsString = matcher.group(BASE_HEIGHT_POSITION);
            String activeAsString = matcher.group(ACTIVE_POSITION);
            String sensorHeightAsString = matcher.group(SENSOR_HEIGHT_POSITION);
            String name = matcher.group(NAME_POSITION);
            String latestAsString = matcher.group(LATEST_POSITION);
            String type = matcher.group(TYPE_POSITION);
            String longitudeAsString = matcher.group(LONGITUDE_POSITION);
            String latitudeAsString = matcher.group(LATITUDE_POSITION);

            Boolean active = null;
            switch (activeAsString){
                case "True":
                    active = Boolean.TRUE;
                    break;
                case "False":
                    active = Boolean.FALSE;
                    break;
                case "None":
                    break;
                default:
                    throw new IllegalArgumentException("Error while parsing field active.");
            }

            return new Sensor(Double.parseDouble(baseHeightAsString), active,
                    sensorHeightAsString.equals("None") ? 0.0 : Double.parseDouble(sensorHeightAsString), name,
                    LocalDateTime.parse(latestAsString, DATE_TIME_FORMATTER),
                    type, Double.parseDouble(longitudeAsString),Double.parseDouble(latitudeAsString));

        }

        return null;

    }

    @Override
    public Collection<Sensor> parseFile(Path filePath) throws IOException {
        return parsingStrategy.parseFile(filePath, this::parseToSensor);
    }

}
