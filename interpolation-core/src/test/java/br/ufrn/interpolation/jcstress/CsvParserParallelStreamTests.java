package br.ufrn.interpolation.jcstress;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

import org.openjdk.jcstress.annotations.Actor;
import org.openjdk.jcstress.annotations.Description;
import org.openjdk.jcstress.annotations.Expect;
import org.openjdk.jcstress.annotations.JCStressTest;
import org.openjdk.jcstress.annotations.Outcome;
import org.openjdk.jcstress.annotations.State;
import org.openjdk.jcstress.infra.results.II_Result;

import br.ufrn.interpolation.domain.sample.Sample;
import br.ufrn.interpolation.infrastructure.parsers.SampleCSVParser;
import br.ufrn.interpolation.infrastructure.utils.CsvParserParallelStream;

public class CsvParserParallelStreamTests {

	@State
    public static class TestState {

        public SampleCSVParser objectUnderTests;

        public TestState() {
        	this.objectUnderTests = new SampleCSVParser(new CsvParserParallelStream()); 
        }

    }

    @Description("test racily getting temperature")
    @JCStressTest
    @Outcome(id = {"953768"}, expect = Expect.ACCEPTABLE)
    @Outcome(id = {"953768, 953768"}, expect = Expect.ACCEPTABLE)
    public static class StressTest2 {
    	
        @Actor
        public void actor1(TestState state, II_Result r) {
           
            Path path = Paths.get("src","main","resources", "samples", "data.csv");

            Collection<Sample> samples = null;
            try {
                samples = state.objectUnderTests.parseFile(path);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }

            r.r1 = samples.size();
        }

        @Actor
        public void actor2(TestState state, II_Result r){
        	Path path = Paths.get("src","main","resources", "samples", "data.csv");

            Collection<Sample> samples = null;
            try {
                samples = state.objectUnderTests.parseFile(path);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }

            r.r2 = samples.size();
        }

    }
	
}
