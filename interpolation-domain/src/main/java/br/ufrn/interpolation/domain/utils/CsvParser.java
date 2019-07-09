package br.ufrn.interpolation.domain.utils;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.function.Function;

public interface CsvParser {
    public <T> Collection<T> parseFile(Path filePath, Function<String,T> parsingFunction) throws IOException;
}
