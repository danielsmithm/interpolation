package br.ufrn.interpolation.infrastructure.repository.sensor;

import br.ufrn.interpolation.domain.sensor.Sensor;
import br.ufrn.interpolation.infrastructure.utils.CsvParser;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SensorCSVParser {

    /**
     * Constant value that holds the separator regex for the CSV file.
     */
    private static final String PARSING_REGEX = "(.{0,}),(.{0,}),\"(.{0,})\",(.{0,}),(.{0,}),(.{0,}),(.{0,}),(.{0,}),(.{0,})";

    /**
     * Constant for the date time format.
     */
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private static final byte BASE_HEIGHT_POSITION = 1;
    private static final byte ACTIVE_POSITION = 2;
    private static final byte SOURCE_POSITION = 3;
    private static final byte SENSOR_HEIGHT_POSITION = 4;
    private static final byte NAME_POSITION = 5;
    private static final byte LATEST_POSITION = 6;
    private static final byte TYPE_POSITION = 7;
    private static final byte LONGITUDE_POSITION = 8;
    private static final byte LATITUDE_POSITION = 9;

    public Sensor parseToSensor(String row){

        Pattern pattern = Pattern.compile(PARSING_REGEX);

        Matcher matcher = pattern.matcher(row);

        if(matcher.matches()){

            String baseHeightAsString = matcher.group(BASE_HEIGHT_POSITION);
            String activeAsString = matcher.group(ACTIVE_POSITION);
            //String sourceAsString = matcher.group(SOURCE_POSITION);//TODO: Source
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

            return new Sensor(Double.parseDouble(baseHeightAsString), active, null,
                    sensorHeightAsString.equals("None") ? 0.0 : Double.parseDouble(sensorHeightAsString), name,
                    LocalDateTime.parse(latestAsString,DateTimeFormatter.ofPattern(DATE_TIME_PATTERN)),
                    type, Double.parseDouble(longitudeAsString),Double.parseDouble(latitudeAsString));

        }

        return null;

    }

    public Collection<Sensor> parseFile(Path filePath) throws IOException {
        return new CsvParser().parseFileUsingThreads(filePath, this::parseToSensor);
    }

}
