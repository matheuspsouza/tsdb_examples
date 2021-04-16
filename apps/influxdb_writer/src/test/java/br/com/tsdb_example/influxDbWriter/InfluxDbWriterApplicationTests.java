package br.com.tsdb_example.influxDbWriter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.tsdb_example.influxdb_writer.InfluxDbWriterApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = InfluxDbWriterApplication.class )
public class InfluxDbWriterApplicationTests {

	@Test
	public void contextLoads() {
	}

}
