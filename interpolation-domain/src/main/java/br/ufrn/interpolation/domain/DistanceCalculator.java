package br.ufrn.interpolation.domain;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

public class DistanceCalculator {

    private DistanceCalculator() {}

    public static Double distanceInMeters(double latitudeA, double latitudeB, double longitudeA, double longitudeB){

        LatLng localA = new LatLng(latitudeA, longitudeA);
        LatLng localB = new LatLng(latitudeB, longitudeB);

        return LatLngTool.distance(localA,localB, LengthUnit.METER);

    }

}
