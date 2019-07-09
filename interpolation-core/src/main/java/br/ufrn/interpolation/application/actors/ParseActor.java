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
import br.ufrn.interpolation.domain.sample.SampleParser;
import br.ufrn.interpolation.domain.utils.Partition;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ParseActor extends AbstractActor {

    private final Creator<PersistenceActor> persistenceActorFactory;
    private SampleParser sampleCSVParser;

    private ParseActor(Creator<PersistenceActor> persistenceActorFactory, SampleParser sampleCSVParser) {
        this.persistenceActorFactory = persistenceActorFactory;
        this.sampleCSVParser = sampleCSVParser;
    }

    public static ParseActor create(Creator<PersistenceActor> persistenceActorFactory, SampleParser sampleCSVParser) {
        return new ParseActor(persistenceActorFactory, sampleCSVParser);
    }

    @Override
    public AbstractActor.Receive createReceive() {
        return receiveBuilder()
                .match(ParseMessage.class, this::parseAndPersist)
                .build();
    }

    private ActorRefRoutee createPersistenceActorRoutee(ActorRef persistenceActor) {
        this.getContext().watch(persistenceActor);
        return new ActorRefRoutee(persistenceActor);
    }

    private void parseAndPersist(ParseMessage parseMessage) {
        List<Sample> samples = parseMessage.rowsToParse.stream().map(sampleCSVParser::parseToSample).collect(Collectors.toList());

        Partition<Sample> partition = Partition.ofSize(samples, 1000);

        List<Routee> persistenceActors = IntStream.range(0, partition.size())
                .mapToObj(i -> getContext().actorOf(Props.create(PersistenceActor.class, persistenceActorFactory)))
                .map(this::createPersistenceActorRoutee)
                .collect(Collectors.toList());

        Router persistenceRouter = new Router(new RoundRobinRoutingLogic(), persistenceActors);

        partition.forEach(samplesSlice -> persistenceRouter.route(new PersistenceActor.PersistenceMessage(samplesSlice), getSelf()));
    }

    public static final class ParseMessage {

        private Collection<String> rowsToParse;

        public ParseMessage(Collection<String> rowsToParse) {
            this.rowsToParse = rowsToParse;
        }
    }


}
