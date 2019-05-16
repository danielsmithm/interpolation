package br.ufrn.interpolation.infrastructure.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CsvParserForkRecursiveTask implements CsvParser {

private static final int THRESHOLD = 100000;
	
	private ForkJoinPool forkJoinPool;

	public CsvParserForkRecursiveTask(ForkJoinPool forkJoinPool) {
		this.forkJoinPool = forkJoinPool;
	}
	
	@Override
	public <T> Collection<T> parseFile(Path filePath, Function<String, T> parsingFunction) throws IOException {
		List<String> fileLines = Files.readAllLines(filePath);
		
		ForkJoinTask<T> forkJoin = new ForkJoinTask<T>(fileLines.stream().skip(1).collect(Collectors.toList()),parsingFunction);
        
        return forkJoinPool.invoke(forkJoin);
	}
	
	
	class ForkJoinTask<T> extends RecursiveTask<Collection<T>>{
		
		private List<String> rowsToParse;
		private Function<String, T> parsingFunction;
		
		public ForkJoinTask(List<String> subList,  Function<String, T> parsingFunction2) {
			this.rowsToParse = subList;
			this.parsingFunction = parsingFunction2;
		}

		@Override
		protected Collection<T> compute() {
						
			if (rowsToParse.size() <= THRESHOLD) {
				return rowsToParse.stream().map(parsingFunction).collect(Collectors.toList());
			} else { // recursive case
				int mid = rowsToParse.size() / 2;
				ForkJoinTask<T> firstSubtask = new ForkJoinTask<T>(rowsToParse.subList(0, mid),parsingFunction);
				ForkJoinTask<T> secondSubtask = new ForkJoinTask<T>(rowsToParse.subList(mid, rowsToParse.size()),parsingFunction);
				firstSubtask.fork();
				
				List<T> list = new CopyOnWriteArrayList<T>();
				
				list.addAll(secondSubtask.compute());
				list.addAll(firstSubtask.join());
				
				return list;
			}
			
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
