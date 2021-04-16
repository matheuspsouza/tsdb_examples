package br.com.tsdb_example.influxdb_writer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import br.com.tsdb_example.influxdb_writer.service.InfluxDBWriterService;

@SpringBootApplication
public class InfluxDbWriterApplication {


	public static void main(String[] args) {
		ConfigurableApplicationContext context=SpringApplication.run(InfluxDbWriterApplication.class, args);
		context.getBean(InfluxDBWriterService.class).writeToInfluxDB();
		
	}

}
