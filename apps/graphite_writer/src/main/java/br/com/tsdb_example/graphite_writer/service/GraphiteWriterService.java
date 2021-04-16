package br.com.tsdb_example.graphite_writer.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.tsdb_example.graphite_writer.config.GraphiteConfig;

@Service
public class GraphiteWriterService {
	private static final Logger logger = LoggerFactory.getLogger(GraphiteWriterService.class);


	@Autowired
	private GraphiteConfig graphite;

	public void writeToGraphite() {

		for (int i = 0; i < 500; i++) {
			double value=Math.random()*10;
			//logger.info("tag_" + i + " "+ System.currentTimeMillis()/1000 +" "+ value+"\n");
			graphite.write("matheus.test.health", System.currentTimeMillis()/1000, value);
			graphite.write("matheus.test.tag_"+i, System.currentTimeMillis()/1000, value);
		}
		
		try {
			Thread.sleep(1000);
			writeToGraphite();
		} catch (InterruptedException e) {
			graphite.closeGraphiteConnection();
			e.printStackTrace();
		}
	}

}
