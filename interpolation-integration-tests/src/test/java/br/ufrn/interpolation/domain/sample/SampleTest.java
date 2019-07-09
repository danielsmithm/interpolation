package br.ufrn.interpolation.domain.sample;

import br.ufrn.interpolation.infrastructure.parsers.SampleParserImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class SampleTest {

    /**
     * This test case creates a sample with values retrieved from the .csv file.
     */
    @Test
    public void testCreateSample() {
        Sample sample = new Sample("ICRAWCRO3","Temperature","Celsius", LocalDateTime.parse("2018-07-31 23:04:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),15.8);

        assertEquals("ICRAWCRO3",sample.getSensorName());
        assertEquals("Temperature", sample.getVariable());
        assertEquals("Celsius",sample.getUnits());
        assertEquals("2018-07-31 23:04:00", sample.getTime().format(DateTimeFormatter.ofPattern(SampleParserImpl.DATE_TIME_PATTERN)));
        assertEquals(15.8, sample.getValue(), 0.0);
    }
}
