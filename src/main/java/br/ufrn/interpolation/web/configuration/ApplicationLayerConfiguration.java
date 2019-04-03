package br.ufrn.interpolation.web.configuration;

import br.ufrn.interpolation.application.SampleReporter;
import br.ufrn.interpolation.domain.sample.SampleRepository;
import br.ufrn.interpolation.domain.sensor.SensorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Lazy
@Configuration
public class ApplicationLayerConfiguration {

    private final SampleRepository sampleRepository;

    private final SensorRepository sensorRepository;

    @Autowired
    public ApplicationLayerConfiguration(SampleRepository sampleRepository, SensorRepository sensorRepository) {
        this.sampleRepository = sampleRepository;
        this.sensorRepository = sensorRepository;
    }

    @Bean
    public SampleReporter temperatureReporter(){
        return new SampleReporter(sampleRepository, sensorRepository);
    }

}
