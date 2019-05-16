package br.ufrn.interpolation.web.configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExecutorServiceConfiguration {

	@Bean @Qualifier("cpuOperationsExecutorService")
	public ExecutorService cpuOperationsExecutorService() {
		return Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	}
	
	@Bean @Qualifier("ioOperationsExecutorService")
	public ExecutorService ioOperationsExecutorService() {
		return Executors.newFixedThreadPool(100);
	}
}
