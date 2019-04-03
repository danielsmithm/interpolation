package br.ufrn.interpolation.infrastructure.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CsvParserSequential implements CsvParser {

    @Override
    public <T> Collection<T> parseFile(Path filePath, Function<String, T> parsingFunction) throws IOException {
        return Files.lines(filePath)
                .skip(1)
                .map(parsingFunction)
                .collect(Collectors.toList());
    }
}
