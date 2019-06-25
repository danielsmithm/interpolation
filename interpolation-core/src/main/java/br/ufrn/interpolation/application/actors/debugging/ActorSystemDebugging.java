package br.ufrn.interpolation.application.actors.debugging;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import br.ufrn.interpolation.application.actors.ParseActor;
import br.ufrn.interpolation.application.actors.PersistenceActor;
import br.ufrn.interpolation.application.actors.ReadFileActor;
import br.ufrn.interpolation.infrastructure.parsers.SampleCSVParser;
import br.ufrn.interpolation.infrastructure.repository.cassandra.CassandraSparkSampleRepository;
import br.ufrn.interpolation.infrastructure.repository.cassandra.connector.CassandraConnector;
import br.ufrn.interpolation.infrastructure.utils.CsvParserSequential;

import java.nio.file.Paths;

public class ActorSystemDebugging {
    public static void main(String[] args) {
        final ActorSystem system = ActorSystem.create("sampleActorSystem");

        final ActorRef parseActor = system.actorOf(Props.create(ParseActor.class, () -> ParseActor.create(() ->  new PersistenceActor(new CassandraSparkSampleRepository(new CassandraConnector())), new SampleCSVParser(new CsvParserSequential()))));
        final ActorRef readingActor = system.actorOf(Props.create(ReadFileActor.class, () -> new ReadFileActor(new CsvParserSequential(), parseActor)));

        readingActor.tell(new ReadFileActor.ReadFileMessage(Paths.get("interpolation-core","src", "main", "resources", "samples", "samples.csv")), ActorRef.noSender());

    }
}
