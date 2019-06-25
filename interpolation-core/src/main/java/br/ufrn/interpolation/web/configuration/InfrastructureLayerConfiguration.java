package br.ufrn.interpolation.web.configuration;

import br.ufrn.interpolation.domain.sample.SampleRepository;
import br.ufrn.interpolation.domain.sensor.SensorRepository;
import br.ufrn.interpolation.infrastructure.repository.memory.SampleInMemoryRepository;
import br.ufrn.interpolation.infrastructure.repository.memory.SensorInMemoryRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Lazy
@Configuration
public class InfrastructureLayerConfiguration {

    @Bean
    public SampleRepository sampleRepository() throws IOException {
        return SampleInMemoryRepository.fromFile(getSamplesFilePath());
    }

    @Bean
    public SensorRepository sensorRepository() throws IOException {
        return SensorInMemoryRepository.fromFile(Paths.get("src","main","resources", "src/main/resources/samples", "sensors.csv"));
    }

    public static Path getSamplesFilePath() {
        return Paths.get("src","main","resources", "src/main/resources/samples", "data.csv");
    }

}
