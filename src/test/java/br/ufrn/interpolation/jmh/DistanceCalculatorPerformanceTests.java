package br.ufrn.interpolation.jmh;

import br.ufrn.interpolation.domain.util.DistanceCalculatorTests;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

public class DistanceCalculatorPerformanceTests {

    
    public void run(){
        new DistanceCalculatorTests().calculateDistanceOfPointsCloseToIMD();
    }
}
