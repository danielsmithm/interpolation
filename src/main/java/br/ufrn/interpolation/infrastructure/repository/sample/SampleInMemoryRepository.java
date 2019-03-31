package br.ufrn.interpolation.infrastructure.repository.sample;

import br.ufrn.interpolation.domain.sample.Sample;
import br.ufrn.interpolation.domain.sample.SampleRepository;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class SampleInMemoryRepository implements SampleRepository {

    private final Collection<Sample> sampleCollection;

    private SampleInMemoryRepository(Collection<Sample> sampleCollection) {
        this.sampleCollection = new ArrayList<>(sampleCollection);
    }

    public static SampleRepository fromFile(Path samplesFilePath) throws IOException {
        SampleCSVParser sampleParser = new SampleCSVParser();

        return new SampleInMemoryRepository(sampleParser.parseFile(samplesFilePath));
    }

    @Override
    public List<Sample> findFromPredicate(Predicate<Sample> predicate){
        return sampleCollection.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

}
