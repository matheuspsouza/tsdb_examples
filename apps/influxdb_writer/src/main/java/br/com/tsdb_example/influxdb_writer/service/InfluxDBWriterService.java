package br.com.tsdb_example.influxdb_writer.service;

import java.util.concurrent.TimeUnit;

import org.influxdb.InfluxDB;
import org.influxdb.dto.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InfluxDBWriterService {
	
	@Autowired
	private InfluxDB influxDB;

	
	private static final Logger logger = LoggerFactory.getLogger(InfluxDBWriterService.class);
	
	public void writeToInfluxDB() {		
		
		for(int i=0;i<500;i++) {
			Point point=Point.measurement("matheus.test.a.b.c.d.e.tag_"+i).time(System.currentTimeMillis(), TimeUnit.MILLISECONDS).addField("value", Math.random()*10).build();
			logger.info(point.toString());
			influxDB.write(point);
		}
		
		
		try {
			Thread.sleep(1000);
			writeToInfluxDB();
		} catch (InterruptedException e) {
			influxDB.close();
			e.printStackTrace();
		}
	}
	
	
	

}
