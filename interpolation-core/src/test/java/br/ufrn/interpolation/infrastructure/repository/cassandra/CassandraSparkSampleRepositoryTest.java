package br.ufrn.interpolation.infrastructure.repository.cassandra;

import br.ufrn.interpolation.domain.sample.Sample;
import br.ufrn.interpolation.infrastructure.parsers.SampleCSVParser;
import br.ufrn.interpolation.infrastructure.repository.cassandra.connector.CassandraConnector;
import br.ufrn.interpolation.infrastructure.utils.CsvParserSequential;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

@Ignore
public class CassandraSparkSampleRepositoryTest {

    private CassandraConnector cassandraConnector;
    private CassandraSparkSampleRepository cassandraSparkSampleRepository;

    @Before
    public void setUp() throws Exception {
        cassandraConnector = new CassandraConnector();
        cassandraConnector.connect();
        cassandraSparkSampleRepository = new CassandraSparkSampleRepository(cassandraConnector);
    }

    @Test
    public void findFromPredicate() {
        /*SparkConf conf = new SparkConf(true)
                .setAppName("MyApp")//
                .setMaster("spark://192.168.0.17:7077");

        SparkSession spark = SparkSession.builder().appName("SparkApp").config("spark.master", "local").getOrCreate();

        JavaSparkContext sc = new JavaSparkContext(conf);
        JavaRDD<Integer> numbers = sc.parallelize(Arrays.asList(14,21,88,99,455));
        int num = numbers.first();


        sc.close();*/

        SparkSession spark = SparkSession.builder().appName("SparkApp").config("spark.master", "local").getOrCreate();
    }

    @Test
    public void save() {
        Sample sample = new SampleCSVParser(new CsvParserSequential()).parseToSample("ICRAWCRO3,Temperature,Celsius,2018-07-31 23:04:00,15.8");

        cassandraSparkSampleRepository.save(sample);
    }

    @Test
    public void saveBatch() {
        Sample sample = new SampleCSVParser(new CsvParserSequential()).parseToSample("ICRAWCRO3,Temperature,Celsius,2018-07-31 23:04:00,15.8");

        cassandraSparkSampleRepository.save(IntStream.range(1,1000).mapToObj(i -> sample).collect(Collectors.toList()));
    }
}