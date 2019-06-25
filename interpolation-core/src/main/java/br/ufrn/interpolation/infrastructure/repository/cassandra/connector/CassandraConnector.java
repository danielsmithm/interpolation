package br.ufrn.interpolation.infrastructure.repository.cassandra.connector;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

public class CassandraConnector {

    private Cluster cluster;
    private Session session;

    public Session connectAndGetSession(){
        connect();
        return getSession();
    }

    public void connect() {
        String node = "127.0.0.1";
        Integer port = 9042;

        if (cluster == null || session == null) {
            Cluster.Builder b = Cluster.builder().withoutJMXReporting().addContactPoint(node);
            if (port != null) {
                b.withPort(port);
            }
            cluster = b.build();

            session = cluster.connect();
        }

    }

    public Session getSession() {
        return this.session;
    }

    public void close() {
        session.close();
        cluster.close();
    }

}
