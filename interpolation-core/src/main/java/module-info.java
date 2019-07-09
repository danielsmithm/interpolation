module br.ufrn.interpolation.core {
    exports br.ufrn.interpolation.application;
    exports br.ufrn.interpolation.application.databaseLoader;
    requires akka.actor;
    requires br.ufrn.interpolation.domain;
}