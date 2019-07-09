package br.ufrn.interpolation.infrastructure.repository.memory;

import br.ufrn.interpolation.domain.sample.Sample;
import br.ufrn.interpolation.domain.sample.SampleRepository;
import br.ufrn.interpolation.domain.sample.SampleParser;
import br.ufrn.interpolation.infrastructure.parsers.SampleParserImpl;
import br.ufrn.interpolation.infrastructure.utils.CsvParserSequential;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class SampleInMemoryRepository implements SampleRepository {

    private final Collection<Sample> sampleCollection;

    public SampleInMemoryRepository(Collection<Sample> sampleCollection) {
        this.sampleCollection = new CopyOnWriteArrayList<>(sampleCollection);
    }

    public static SampleRepository fromFile(Path samplesFilePath) throws IOException {
        SampleParser sampleParser = new SampleParserImpl(new CsvParserSequential());

        return new SampleInMemoryRepository(sampleParser.parseFile(samplesFilePath));
    }

    @Override
    public List<Sample> findFromPredicate(Predicate<Sample> predicate){
        return sampleCollection.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    @Override
    public void save(Sample sample) {
        sampleCollection.add(sample);
    }

    @Override
    public void save(Collection<Sample> samples) {
        samples.forEach(System.out::println);
        sampleCollection.addAll(samples);
    }

}
