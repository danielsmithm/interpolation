package br.ufrn.interpolation.infrastructure.repository.cassandra.connector;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.time.Duration;

import static org.junit.Assert.*;

@Ignore
public class CassandraConnectorTest {

    private static final String CQL = "db.cql";

    private CassandraConnector cassandraConnector;

    @Before
    public void setUp() throws Exception {
        cassandraConnector = new CassandraConnector();
    }

    @Test
    public void connect() {
        cassandraConnector.connect();

        assertNotNull(cassandraConnector.getSession());
    }

    @Test
    public void getSession() {
        cassandraConnector.connect();

        assertNotNull(cassandraConnector.getSession());
    }

    @Test
    public void close() {
        cassandraConnector.connect();
        cassandraConnector.close();

        assertTrue(cassandraConnector.getSession().isClosed());
    }
}