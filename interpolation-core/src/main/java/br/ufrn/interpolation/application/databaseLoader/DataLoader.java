package br.ufrn.interpolation.application.databaseLoader;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import br.ufrn.interpolation.application.actors.ParseActor;
import br.ufrn.interpolation.application.actors.PersistenceActor;
import br.ufrn.interpolation.application.actors.ReadFileActor;
import br.ufrn.interpolation.domain.sample.SampleParser;
import br.ufrn.interpolation.domain.sample.SampleRepository;
import br.ufrn.interpolation.domain.utils.CsvParser;

import java.nio.file.Path;

public class DataLoader {

    public void loadDatabase(Path filePath, SampleParser sampleParser, SampleRepository sampleRepository, CsvParser csvParser) {
        final ActorSystem system = ActorSystem.create("sampleActorSystem");

        final ActorRef parseActor = system.actorOf(Props.create(ParseActor.class, () -> ParseActor.create(() ->  new PersistenceActor(sampleRepository), sampleParser)));
        final ActorRef readingActor = system.actorOf(Props.create(ReadFileActor.class, () -> new ReadFileActor(csvParser, parseActor)));

        readingActor.tell(new ReadFileActor.ReadFileMessage(filePath), ActorRef.noSender());
    }


}
