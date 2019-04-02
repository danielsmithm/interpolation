package br.ufrn.interpolation.infrastructure.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CsvParser {

    public <T> Collection<T> parseFile(Path filePath, Function<String,T> parsingFunction) throws IOException {
        List<String> fileLines = Files.readAllLines(filePath);

        //Is needed to remove the first line of the file because it's not a value line.

        String headerLine = fileLines.remove(0);

        //Return an empty collection if the header line is invalid
        if(headerLine == null || headerLine.isEmpty()){
            return new ArrayList<>();
        }

        List<T> list = new ArrayList<>();
        for (String fileLine : fileLines) {
            T t = parsingFunction.apply(fileLine);
            list.add(t);
        }

        return list;

    }

    public <T> Collection<T> parseFileUsingSliceParsing(Path filePath, Function<String,T> parsingFunction) throws IOException {
        return Files.lines(filePath)
                .skip(1)
                .parallel()
                .map(parsingFunction).collect(Collectors.toList());
    }

    public <T> Collection<T> parseFileUsingThreads(Path filePath, Function<String,T> parsingFunction) throws IOException {

        List<String> fileLines = Files.readAllLines(filePath);

        List<T> list = new LinkedList<>();
        List<Thread> threads = new CopyOnWriteArrayList<>();

        int processorCount = Runtime.getRuntime().availableProcessors();

        int sliceSize = (int) Math.ceil(Double.valueOf(fileLines.size()) / processorCount);
        for(int i = 0; i < processorCount; i++ ) {
            final int initIndex = i * sliceSize;
            final int finalIndex = initIndex + sliceSize < fileLines.size() ? initIndex + sliceSize : fileLines.size();

            threads.add(new Thread(() -> {

                List<T> threadParsingResult = new ArrayList<>();
                for (int j = initIndex; j < finalIndex; j++) {
                    if (j == 0) {
                        continue;
                    }

                    T t = parsingFunction.apply(fileLines.get(j));

                    threadParsingResult.add(t);

                }

                synchronized (list){
                    list.addAll(threadParsingResult);
                }

            }));

        }

        threads.forEach(Thread::start);

        threads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        return list;
    }

}
