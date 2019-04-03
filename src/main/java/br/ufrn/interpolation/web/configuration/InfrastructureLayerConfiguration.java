package br.ufrn.interpolation.web.configuration;

import br.ufrn.interpolation.infrastructure.repository.sample.SampleInMemoryRepository;
import br.ufrn.interpolation.infrastructure.repository.sensor.SensorInMemoryRepository;
import br.ufrn.interpolation.domain.sample.SampleRepository;
import br.ufrn.interpolation.domain.sensor.SensorRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.io.IOException;
import java.nio.file.Paths;

@Lazy
@Configuration
public class InfrastructureLayerConfiguration {

    @Bean
    public SampleRepository sampleRepository() throws IOException {
        return SampleInMemoryRepository.fromFile(Paths.get("src","main","resources", "samples", "data.csv"));
    }

    @Bean
    public SensorRepository sensorRepository() throws IOException {
        return SensorInMemoryRepository.fromFile(Paths.get("src","main","resources", "samples", "sensors.csv"));
    }

}
