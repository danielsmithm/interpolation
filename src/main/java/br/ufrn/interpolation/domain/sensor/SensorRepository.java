package br.ufrn.interpolation.domain.sensor;

import java.util.AbstractMap;
import java.util.List;

public interface SensorRepository {
     List<AbstractMap.SimpleEntry<Sensor,Double>> findSensorsWithinDistance(double latitude,
                                                                            double longitude,
                                                                            double maximumDistanceInMeters);
}
