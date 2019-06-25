package br.ufrn.interpolation.application.actors;

import akka.actor.AbstractActor;
import br.ufrn.interpolation.domain.sample.Sample;
import br.ufrn.interpolation.domain.sample.SampleRepository;

import java.util.Collections;
import java.util.List;

public class PersistenceActor extends AbstractActor {

    private SampleRepository sampleRepository;

    public PersistenceActor(SampleRepository sampleRepository) {
        this.sampleRepository = sampleRepository;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(PersistenceMessage.class, parseMessage -> sampleRepository.save(parseMessage.samples))
                .build();
    }

    public static class PersistenceMessage {

        private final List<Sample> samples;

        public PersistenceMessage(List<Sample> samples) {
            this.samples = Collections.unmodifiableList(samples);
        }
    }
}
