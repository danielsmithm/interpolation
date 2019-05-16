package br.ufrn.interpolation.infrastructure.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CsvParserExecutorUsingCallable implements CsvParser {

	private ExecutorService executorService;

	public CsvParserExecutorUsingCallable(ExecutorService executorService) {		
		this.executorService = executorService;
	}

	@Override
	public <T> Collection<T> parseFile(Path filePath, Function<String, T> parsingFunction) throws IOException {
		List<String> fileLines = Files.readAllLines(filePath);        

        int processorCount = Runtime.getRuntime().availableProcessors();
        int sliceSize = (int) Math.ceil(Double.valueOf(fileLines.size()) / processorCount);

		List<T> collect = IntStream.range(0, processorCount)
				.mapToObj((k) -> parseSliceCallable(parsingFunction, fileLines, sliceSize, k)) //Creating the callables
				.map((callable) -> executorService.submit(callable)) //Submitting the callables
				.map((future) -> joinFuture(future)) //Retrieving the result
				.flatMap((list) -> list.stream()) //Joining the result
				.collect(Collectors.toList()); //Collecting the results.            
        
        return collect;
	}

	private <T> List<T> joinFuture(Future<List<T>> future) {
		try {
			return future.get();
		} catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		}
	}

	private <T> Callable<List<T>> parseSliceCallable(Function<String, T> parsingFunction, 
			List<String> fileLines,
			int sliceSize, int k) {
		return () -> {

			final int initIndex = k * sliceSize;
			final int finalIndex = initIndex + sliceSize < fileLines.size() ? initIndex + sliceSize : fileLines.size();
			
			List<T> threadLocalParsingResult = new ArrayList<>();
			for (int j = initIndex; j < finalIndex; j++) {
				if (j == 0) {
					continue;
				}

				T t = parsingFunction.apply(fileLines.get(j));

				threadLocalParsingResult.add(t);

			}

			return threadLocalParsingResult;

		};
	}

}
