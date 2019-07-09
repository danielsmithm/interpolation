package br.ufrn.interpolation.infrastructure.repository.cassandra;

import br.ufrn.interpolation.domain.sensor.Sensor;
import br.ufrn.interpolation.infrastructure.repository.memory.SensorInMemoryRepository;

import java.util.Collection;

public class CassandraSparkSensorRepository extends SensorInMemoryRepository {

    public CassandraSparkSensorRepository(Collection<Sensor> sensorCollection) {
        super(sensorCollection);
    }

}
