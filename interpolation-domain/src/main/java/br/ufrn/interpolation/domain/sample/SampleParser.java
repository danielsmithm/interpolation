package br.ufrn.interpolation.domain.sample;

import br.ufrn.interpolation.domain.sample.Sample;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;

public interface SampleParser {
    Sample parseToSample(String row);

    Collection<Sample> parseFile(Path filePath) throws IOException;
}
