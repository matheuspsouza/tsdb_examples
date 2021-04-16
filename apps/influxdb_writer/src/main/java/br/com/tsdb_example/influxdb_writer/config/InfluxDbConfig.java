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

//	@Bean
//	public InfluxDB influxDBConnect() {
//		InfluxDB influxDB = InfluxDBFactory.connect("http://127.0.0.1:8086", "root", "root");
//		influxDBConnectToDataBase(influxDB, "dbName");
//		influxDBRetentionPolicy(influxDB, "retentionPolicyName", "dbName");
//		influxDBBatchOptions(influxDB);
//		return influxDB;
//	}
//
//	private void influxDBBatchOptions(InfluxDB influxDB) {
//		influxDB.enableBatch(100, 100, TimeUnit.MILLISECONDS);
//	}
//
//	private void influxDBRetentionPolicy(InfluxDB influxDB, String retentionPolicyName, String dbName) {
//		String queryString = "CREATE RETENTION POLICY" + retentionPolicyName + " ON " + dbName
//				+ " DURATION 4w REPLICATION 1 DEFAULT";
//		influxDB.query(new Query(queryString, dbName));
//		influxDB.setRetentionPolicy(retentionPolicyName);
//
//	}
//
//	private void influxDBConnectToDataBase(InfluxDB influxDB, String dbName) {
//		QueryResult queryResult= influxDB.query(new Query("SHOW DATABASES"));		
////		for(QueryResult.Result result: queryResult.getResults()) {
////			result.getSeries().get(arg0).contains(dbName);
////		}
////		if(!queryResult.getResults().contains(dbName)) {
////			influxDB.query(new Query("CREATE DATABASE "+ dbName));
////		}		
//		if (!influxDB.databaseExists(dbName)) {
//            influxDB.createDatabase(dbName);
//        }
//		
//		
//		influxDB.setDatabase(dbName);
//		
//
//	}
	

	private String retentionPolicyName = "firstRetention";
	
	@Value("${influxdb.url}")
	private String url;
	
	@Value("${influxdb.db}")
	private String dbName;
	

	@Bean
	public InfluxDB influxDBConfig() {
		InfluxDB influxDB = InfluxDBFactory.connect("http://127.0.0.1:8086", "root", "root");
		influxDB.setDatabase(dbName);
		String queryString = "CREATE RETENTION POLICY " + retentionPolicyName + " ON " + dbName
				+ " DURATION 1w REPLICATION 1 DEFAULT";
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
