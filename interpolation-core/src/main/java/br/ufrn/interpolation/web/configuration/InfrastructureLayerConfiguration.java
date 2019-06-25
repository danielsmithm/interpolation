package br.ufrn.interpolation.web.configuration;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import br.ufrn.interpolation.application.actors.ParseActor;
import br.ufrn.interpolation.application.actors.PersistenceActor;
import br.ufrn.interpolation.application.actors.ReadFileActor;
import br.ufrn.interpolation.domain.sample.SampleRepository;
import br.ufrn.interpolation.domain.sensor.SensorRepository;
import br.ufrn.interpolation.infrastructure.parsers.SampleCSVParser;
import br.ufrn.interpolation.infrastructure.repository.cassandra.CassandraSparkSampleRepository;
import br.ufrn.interpolation.infrastructure.repository.cassandra.connector.CassandraConnector;
import br.ufrn.interpolation.infrastructure.repository.memory.SampleInMemoryRepository;
import br.ufrn.interpolation.infrastructure.repository.memory.SensorInMemoryRepository;
import br.ufrn.interpolation.infrastructure.utils.CsvParserSequential;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Lazy
@Configuration
public class InfrastructureLayerConfiguration {

    @Value("${persistence.strategy}")
    private String persistenceStragegy;

    @Bean
    public SampleRepository sampleRepository() throws IOException {
        if("cassandra".equals(persistenceStragegy)){
            final ActorSystem system = ActorSystem.create("sampleActorSystem");

            final ActorRef parseActor = system.actorOf(Props.create(ParseActor.class, () -> ParseActor.create(() ->  new PersistenceActor(new CassandraSparkSampleRepository(new CassandraConnector())), new SampleCSVParser(new CsvParserSequential()))));
            final ActorRef readingActor = system.actorOf(Props.create(ReadFileActor.class, () -> new ReadFileActor(new CsvParserSequential(), parseActor)));

            readingActor.tell(new ReadFileActor.ReadFileMessage(getSamplesFilePath()), ActorRef.noSender());

            return new CassandraSparkSampleRepository(new CassandraConnector());
        }

        return SampleInMemoryRepository.fromFile(getSamplesFilePath());
    }

    @Bean
    public SensorRepository sensorRepository() throws IOException {
        return SensorInMemoryRepository.fromFile(Paths.get("interpolation-core", "src","main","resources", "samples", "sensors.csv"));
    }

    public static Path getSamplesFilePath() {
        return Paths.get("interpolation-core", "src","main","resources", "samples", "data.csv");
    }

}
