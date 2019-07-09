package br.ufrn.interpolation.infrastructure.utils;

import br.ufrn.interpolation.domain.utils.CsvParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CsvParserSequential implements CsvParser {

    @Override
    public <T> Collection<T> parseFile(Path filePath, Function<String, T> parsingFunction) throws IOException {
        try(Stream<String> lines = Files.lines(filePath)) {
            return lines
                    .skip(1)
                    .map(parsingFunction)
                    .collect(Collectors.toList());
        }
    }
}
