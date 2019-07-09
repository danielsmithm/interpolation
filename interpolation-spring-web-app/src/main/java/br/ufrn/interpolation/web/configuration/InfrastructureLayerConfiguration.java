package br.ufrn.interpolation.web.configuration;

import br.ufrn.interpolation.application.databaseLoader.DataLoader;
import br.ufrn.interpolation.domain.sample.SampleRepository;
import br.ufrn.interpolation.domain.sensor.SensorRepository;
import br.ufrn.interpolation.infrastructure.parsers.SampleParserImpl;
import br.ufrn.interpolation.infrastructure.repository.cassandra.CassandraSparkSampleRepository;
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

            new DataLoader().loadDatabase(getSamplesFilePath(), new SampleParserImpl(new CsvParserSequential()), new CassandraSparkSampleRepository(), new CsvParserSequential());
            return new CassandraSparkSampleRepository();
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
