package br.ufrn.interpolation.domain.util;

import br.ufrn.interpolation.domain.DistanceCalculator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class DistanceCalculatorTests {
    @Test
    public void calculateDistanceOfPointsCloseToIMD() {
        Double latitudeA = -5.831710;
        Double longitudeA =  -35.205583;
        Double latitudeB = -5.831910;
        Double longitudeB =  -35.204990;

        Double distance = DistanceCalculator.distanceInMeters(latitudeA, latitudeB, longitudeA, longitudeB);
        assertEquals(69.24,distance,0.3);// Tolerating a distance 3 centimeters of Delta.
    }
}
