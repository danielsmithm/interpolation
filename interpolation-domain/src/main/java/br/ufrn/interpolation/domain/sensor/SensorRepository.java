package br.ufrn.interpolation.domain.sensor;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.List;

public interface SensorRepository {
     void save(Sensor sensor);
     void save(Collection<Sensor> sensors);
     List<AbstractMap.SimpleEntry<Sensor,Double>> findSensorsWithinDistance(double latitude,
                                                                            double longitude,
                                                                            double maximumDistanceInMeters);
}
