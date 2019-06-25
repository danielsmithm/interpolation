package br.ufrn.interpolation.application.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import br.ufrn.interpolation.infrastructure.utils.CsvParserSequential;

import java.nio.file.Path;
import java.util.function.Function;

public class ReadFileActor extends AbstractActor {

    private final ActorRef parseActor;
    private CsvParserSequential csvParserSequential;

    public ReadFileActor(CsvParserSequential csvParserSequential, ActorRef parseActor) {
        this.csvParserSequential = csvParserSequential;
        this.parseActor = parseActor;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ReadFileMessage.class, readFileMessage -> parseActor.tell(new ParseActor.ParseMessage(csvParserSequential.parseFile(readFileMessage.filePath, Function.identity())), getSelf()))
                .build();
    }


    public static class ReadFileMessage {
        private Path filePath;

        public ReadFileMessage(Path filePath) {
            this.filePath = filePath;
        }
    }
}
