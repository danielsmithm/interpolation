package br.ufrn.interpolation.domain.sample;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

public interface SampleRepository {
    List<Sample> findFromPredicate(Predicate<Sample> predicate);
    void save(Sample sample);
    void save(Collection<Sample> sampleCollection);
}
