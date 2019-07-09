module br.ufrn.interpolation.infrastructure {
    exports br.ufrn.interpolation.infrastructure.utils;
    exports br.ufrn.interpolation.infrastructure.parsers;
    exports br.ufrn.interpolation.infrastructure.repository.cassandra;
    exports br.ufrn.interpolation.infrastructure.repository.memory;
    requires br.ufrn.interpolation.domain;
    requires cassandra.driver.core;
    requires java.sql;
}