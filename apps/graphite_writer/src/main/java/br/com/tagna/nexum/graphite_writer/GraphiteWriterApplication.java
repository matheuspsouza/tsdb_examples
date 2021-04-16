package br.com.tagna.nexum.graphite_writer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import br.com.tagna.nexum.graphite_writer.service.GraphiteWriterService;

@SpringBootApplication
public class GraphiteWriterApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context=SpringApplication.run(GraphiteWriterApplication.class, args);
		context.getBean(GraphiteWriterService.class).writeToGraphite();
	}

}
