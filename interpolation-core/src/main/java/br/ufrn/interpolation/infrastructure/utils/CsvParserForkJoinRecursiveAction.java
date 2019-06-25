package br.ufrn.interpolation.infrastructure.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CsvParserForkJoinRecursiveAction implements CsvParser {

	private static final int THRESHOLD = 100000;
	
	private ForkJoinPool forkJoinPool;

	public CsvParserForkJoinRecursiveAction(ForkJoinPool forkJoinPool) {
		this.forkJoinPool = forkJoinPool;
	}
	
	@Override
	public <T> Collection<T> parseFile(Path filePath, Function<String, T> parsingFunction) throws IOException {
		List<String> fileLines = Files.readAllLines(filePath);
		
		List<T> sharedResult = new CopyOnWriteArrayList<>();
        ForkJoin<T> forkJoin = new ForkJoin<>(fileLines.stream().skip(1).collect(Collectors.toList()),sharedResult,parsingFunction);
        
        forkJoinPool.invoke(forkJoin);

        return sharedResult;
	}
	
	
	static class ForkJoin<T> extends RecursiveAction{
		
		private transient List<T> sharedList;
		private transient List<String> rowsToParse;
		private transient Function<String, T> parsingFunction;
		
		public ForkJoin(List<String> subList, List<T> sharedList2, Function<String, T> parsingFunction2) {
			this.rowsToParse = subList;
			this.sharedList = sharedList2;
			this.parsingFunction = parsingFunction2;
		}

		@Override
		protected void compute() {
			if (rowsToParse.size() <= THRESHOLD) {
				sharedList.addAll(rowsToParse.stream().map(parsingFunction).collect(Collectors.toList()));
			} else {				
				
				int mid = rowsToParse.size() / 2;
				ForkJoin<T> firstSubtask = new ForkJoin<>(rowsToParse.subList(0, mid),sharedList,parsingFunction);
				ForkJoin<T> secondSubtask = new ForkJoin<>(rowsToParse.subList(mid, rowsToParse.size()),sharedList,parsingFunction);
				firstSubtask.fork(); // queue the first task
				secondSubtask.compute(); // compute the second task
				firstSubtask.join(); // wait for the first task result				
			}
		}
		
	}

}
