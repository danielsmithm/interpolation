package br.ufrn.interpolation.infrastructure.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CsvParserParallelStream implements CsvParser {
    @Override
    public <T> Collection<T> parseFile(Path filePath, Function<String, T> parsingFunction) throws IOException {

        try(Stream<String> fileStream = Files.lines(filePath)){
            return fileStream
                    .skip(1)
                    .parallel()
                    .map(parsingFunction).collect(Collectors.toList());
        }

    }


}
