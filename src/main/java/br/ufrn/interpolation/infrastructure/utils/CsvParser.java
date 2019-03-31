package br.ufrn.interpolation.infrastructure.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CsvParser {

    public <T> Collection<T> parseFile(Path filePath, Function<String,T> parsingFunction) throws IOException {
        List<String> fileLines = Files.readAllLines(filePath);

        //Is needed to remove the first line of the file because it's not a value line.

        String headerLine = fileLines.remove(0);

        //Return an empty collection if the header line is invalid
        if(headerLine == null || headerLine.isEmpty()){
            return new ArrayList<>();
        }

        return fileLines.stream()
                .map(parsingFunction)
                .collect(Collectors.toList());

    }

}
