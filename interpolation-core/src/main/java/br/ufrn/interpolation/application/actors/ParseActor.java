package br.ufrn.interpolation.application.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.Creator;
import akka.routing.ActorRefRoutee;
import akka.routing.RoundRobinRoutingLogic;
import akka.routing.Routee;
import akka.routing.Router;
import br.ufrn.interpolation.domain.sample.Sample;
import br.ufrn.interpolation.infrastructure.parsers.SampleCSVParser;
import br.ufrn.interpolation.infrastructure.repository.cassandra.CassandraSparkSampleRepository;
import br.ufrn.interpolation.infrastructure.repository.cassandra.Partition;
import br.ufrn.interpolation.infrastructure.repository.cassandra.connector.CassandraConnector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ParseActor extends AbstractActor {

    private final Creator<PersistenceActor> persistenceActorFactory;
    private SampleCSVParser sampleCSVParser;

    private ParseActor(Creator<PersistenceActor> persistenceActorFactory, SampleCSVParser sampleCSVParser) {
        this.persistenceActorFactory = persistenceActorFactory;
        this.sampleCSVParser = sampleCSVParser;
    }

    public static ParseActor create(Creator<PersistenceActor> persistenceActorFactory, SampleCSVParser sampleCSVParser) {
        return new ParseActor(persistenceActorFactory, sampleCSVParser);
    }

    @Override
    public AbstractActor.Receive createReceive() {
        return receiveBuilder()
                .match(ParseMessage.class, parseMessage -> {

                    Partition<Sample> partition = Partition.ofSize(parseMessage.rowsToParse.stream().map(sampleCSVParser::parseToSample).collect(Collectors.toList()), 1000);

                    List<Routee> persistenceActors = new ArrayList<>();
                    for (int i = 0; i < partition.size(); i++) {
                        ActorRef persistenceActor = getContext().actorOf(Props.create(PersistenceActor.class, persistenceActorFactory));
                        this.getContext().watch(persistenceActor);
                        persistenceActors.add(new ActorRefRoutee(persistenceActor));
                    }

                    Router persistentenceRouter = new Router(new RoundRobinRoutingLogic(), persistenceActors);

                    for (int i = 0; i < partition.size(); i++) {
                        persistentenceRouter.route(new PersistenceActor.PersistenceMessage(partition.get(i)), getSelf());
                    }
                })
                .build();
    }

    public static final class ParseMessage {

        private Collection<String> rowsToParse;

        public ParseMessage(Collection<String> rowsToParse) {
            this.rowsToParse = rowsToParse;
        }
    }


}
