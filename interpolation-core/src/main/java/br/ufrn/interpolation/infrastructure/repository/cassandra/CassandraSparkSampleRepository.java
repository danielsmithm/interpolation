package br.ufrn.interpolation.infrastructure.repository.cassandra;

import br.ufrn.interpolation.domain.sample.Sample;
import br.ufrn.interpolation.domain.sample.SampleRepository;
import br.ufrn.interpolation.infrastructure.repository.cassandra.connector.CassandraConnector;
import br.ufrn.interpolation.infrastructure.repository.memory.SampleInMemoryRepository;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class CassandraSparkSampleRepository implements SampleRepository {

    private static final String INSERT_STATEMENT = "insert into interpolation.sample (id, sensorName, variable, units, time, value) values (uuid(),?, ?, ?, ?, ?);";
    public static final int CHUNK_SIZE = 100;
    private static final SampleInMemoryRepository cache = new SampleInMemoryRepository(new CopyOnWriteArrayList<>());

    public CassandraSparkSampleRepository(CassandraConnector cassandraConnector) {

    }

    @Override
    public List<Sample> findFromPredicate(Predicate<Sample> predicate) {
        return cache.findFromPredicate(predicate);
    }

    @Override
    public void save(Sample sample) {

        try (Session session = new CassandraConnector().connectAndGetSession()) {

            PreparedStatement preparedStatement = session.prepare(INSERT_STATEMENT);

            session.execute(preparedStatement.bind(sample.getSensorName(), sample.getVariable(), sample.getUnits(), java.sql.Timestamp.valueOf(sample.getTime()), sample.getValue()));
        }

        cache.save(sample);
    }

    @Override
    public void save(Collection<Sample> sampleCollection) {
        Partition<Sample> lists = Partition.ofSize(sampleCollection, CHUNK_SIZE);

        for(int i = 0; i< lists.size(); i++){
            insertBatch(lists.get(0));
        }

        cache.save(sampleCollection);

    }

    private void insertBatch(Collection<Sample> sampleCollection) {
        StringBuilder batchInsertStatement = new StringBuilder();

        batchInsertStatement.append("BEGIN BATCH ");

        batchInsertStatement.append(IntStream.range(0,sampleCollection.size()).mapToObj(i -> INSERT_STATEMENT).collect(Collectors.joining()));

        batchInsertStatement.append("APPLY BATCH");

        try (Session session = new CassandraConnector().connectAndGetSession()) {
            /*PreparedStatement preparedStatement = session.prepare(INSERT_STATEMENT);
            for(Sample sample : sampleCollection) {
                session.execute(preparedStatement.bind(sample.getSensorName(), sample.getVariable(), sample.getUnits(), java.sql.Timestamp.valueOf(sample.getTime()), sample.getValue()));
            }*/
            executeBatch(sampleCollection, session,batchInsertStatement.toString());

        }
    }

    private void executeBatch(Collection<Sample> sampleCollection, Session session, String s) {
        PreparedStatement preparedStatement = session.prepare(s);

        session.execute(preparedStatement.bind(sampleCollection.stream()
                .map(sample -> new Object[]{sample.getSensorName(), sample.getVariable(), sample.getUnits(), java.sql.Timestamp.valueOf(sample.getTime()), sample.getValue()})
                .flatMap(Stream::of)
                .toArray()));
    }


}
