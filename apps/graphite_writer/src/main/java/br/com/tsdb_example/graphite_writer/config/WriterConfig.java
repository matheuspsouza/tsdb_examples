package br.com.tsdb_example.graphite_writer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.micrometer.core.instrument.MeterRegistry;

@Configuration
public class WriterConfig {
	

	@Bean
	public MeterRegistryCustomizer<MeterRegistry> commonTags(@Value("${application.name}") String applicationName) {
		return r -> r.config().commonTags("application", applicationName);

	}

}
