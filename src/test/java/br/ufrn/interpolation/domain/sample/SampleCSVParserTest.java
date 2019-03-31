package br.ufrn.interpolation.domain.sample;

import br.ufrn.interpolation.infrastructure.repository.sample.SampleCSVParser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

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

        Sample sample = new SampleCSVParser().parseToSample(csv);

        assertEquals("ICRAWCRO3", sample.getSensorName());
        assertEquals("Temperature", sample.getVariable());
        assertEquals("Celsius", sample.getUnits());
        assertEquals("2018-07-31 23:04:00", sample.getTimestamp().format(DateTimeFormatter.ofPattern(SampleCSVParser.DATE_TIME_PATTERN)));
        assertEquals(15.8, sample.getValue(), 0.0);

    }

    @Test
    public void testParseFromSmallCSVFile() throws IOException {
        Path path = Paths.get("src","test","resources", "samples", "data.csv");

        Collection<Sample> samples = new SampleCSVParser().parseFile(path);

        assertNotNull(samples);

    }

}
