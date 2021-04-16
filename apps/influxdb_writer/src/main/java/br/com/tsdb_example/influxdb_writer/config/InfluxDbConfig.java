package br.com.tsdb_example.influxdb_writer.config;

import java.util.concurrent.TimeUnit;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Query;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InfluxDbConfig {

	private final String retentionPolicyName = "firstRetention";
	
	@Value("${influxdb.url}")
	private String url;
	
	@Value("${influxdb.db}")
	private String dbName;
	
	@Value("${influxdb.admin.user}")
	private String user;
	
	@Value("${influxdb.admin.password}")
	private String password;
	

	@Bean
	public InfluxDB influxDBConfig() {
		InfluxDB influxDB = InfluxDBFactory.connect(url, user, password);
	
		influxDB.setDatabase(dbName);
		String queryString = "CREATE RETENTION POLICY " + retentionPolicyName + " ON " + dbName
				+ " DURATION 1d REPLICATION 1 DEFAULT";
		System.out.println(queryString);
		influxDB.query(new Query(queryString, dbName));
		influxDB.setRetentionPolicy(retentionPolicyName);
		influxDB.setLogLevel(InfluxDB.LogLevel.BASIC);
		influxDB.enableBatch(500, 1000, TimeUnit.MILLISECONDS);

		return influxDB;
	}
	
	@Bean
	public BatchPoints batchPoints() {
		return BatchPoints.database(dbName).retentionPolicy(retentionPolicyName).build();
	}

}
