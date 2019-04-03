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

public class CsvParserThreads implements CsvParser{

    @Override
    public <T> Collection<T> parseFile(Path filePath, Function<String, T> parsingFunction) throws IOException {

        List<String> fileLines = Files.readAllLines(filePath);
        List<T> list = new ArrayList<>();

        int processorCount = Runtime.getRuntime().availableProcessors();
        int sliceSize = (int) Math.ceil(Double.valueOf(fileLines.size()) / processorCount);

        CountDownLatch countDownLatch = new CountDownLatch(processorCount);
        for(int i = 0; i < processorCount; i++ ) {
            final int initIndex = i * sliceSize;
            final int finalIndex = initIndex + sliceSize < fileLines.size() ? initIndex + sliceSize : fileLines.size();

            new Thread(() -> {

                List<T> threadLocalParsingResult = new ArrayList<>();
                for (int j = initIndex; j < finalIndex; j++) {
                    if (j == 0) {
                        continue;
                    }

                    T t = parsingFunction.apply(fileLines.get(j));

                    threadLocalParsingResult.add(t);

                }

                synchronized (list){
                    list.addAll(threadLocalParsingResult);
                }

                countDownLatch.countDown();

            }).start();

        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException("Error during csv parsing. See the cause for more details", e);
        }

        return list;
    }

}
