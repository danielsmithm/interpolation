package br.ufrn.interpolation.infrastructure.repository.cassandra;

import br.ufrn.interpolation.domain.sample.Sample;
import br.ufrn.interpolation.infrastructure.parsers.SampleParserImpl;
import br.ufrn.interpolation.infrastructure.repository.cassandra.connector.CassandraConnector;
import br.ufrn.interpolation.infrastructure.utils.CsvParserSequential;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Ignore
public class CassandraSparkSampleRepositoryTest {

    private CassandraConnector cassandraConnector;
    private CassandraSparkSampleRepository cassandraSparkSampleRepository;

    @Before
    public void setUp() throws Exception {
        cassandraConnector = new CassandraConnector();
        cassandraConnector.connect();
        cassandraSparkSampleRepository = new CassandraSparkSampleRepository();
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

        //SparkSession spark = SparkSession.builder().appName("SparkApp").config("spark.master", "local").getOrCreate();
    }

    @Test
    public void save() {
        Sample sample = new SampleParserImpl(new CsvParserSequential()).parseToSample("ICRAWCRO3,Temperature,Celsius,2018-07-31 23:04:00,15.8");

        cassandraSparkSampleRepository.save(sample);
    }

    @Test
    public void saveBatch() {
        Sample sample = new SampleParserImpl(new CsvParserSequential()).parseToSample("ICRAWCRO3,Temperature,Celsius,2018-07-31 23:04:00,15.8");

        cassandraSparkSampleRepository.save(IntStream.range(1,1000).mapToObj(i -> sample).collect(Collectors.toList()));
    }
}