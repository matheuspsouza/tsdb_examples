package br.com.tsdb_example.graphite_writer.config;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GraphiteConfig {
	private static final Logger logger = LoggerFactory.getLogger(GraphiteConfig.class);

	private final String graphiteHost;
	private final int graphitePort;

	private Socket graphiteSocket;
	private OutputStream out;
	private Writer pickleWriter;

	List<GraphiteMetricTuple> metrics = new ArrayList<GraphiteMetricTuple>();

	@Autowired
	public GraphiteConfig(@Value("${graphite.host}") String graphiteHost,
			@Value("${graphite.pickle.port}") int graphitePort) {
		this.graphitePort = graphitePort;
		this.graphiteHost = graphiteHost;
		connectToGraphiteSocket();
	}

	private void connectToGraphiteSocket() {
		try {
			this.graphiteSocket = new Socket(this.graphiteHost, this.graphitePort);
			this.out = this.graphiteSocket.getOutputStream();
			this.pickleWriter = new OutputStreamWriter(this.out); // out,"ISO-8859-1"
			logger.info("\nConected to Graphite socket.");
		} catch (IOException e) {
			logger.warn("\nCan't connect to graphite. Connection error:\n" + e);
		}
	}

	public void write(String metricName, Long timestamp, double value) {

		metrics.add(new GraphiteMetricTuple(metricName, timestamp, value));
		if (metrics.size() >= 500 && this.graphiteSocket != null) {
			sendDataPickle();
			logger.info("Sended package.\n");
		}

	}

	private void sendDataPickle() {
		String payload = preparePickleData();
		try {
			sendPayload(payload);
		} catch (Exception e) {
			logger.warn("\nCan't send the data to graphite. Connection error:\n" + e);
			e.printStackTrace();
			logger.info("\nClosing Graphite socket.");
			closeGraphiteConnection();

		}

	}

	private String preparePickleData() {

		final char MARK = '(', STOP = '.', LONG = 'L', STRING = 'S', APPEND = 'a', LIST = 'l', TUPLE = 't';

		StringBuilder pickled = new StringBuilder();
		pickled.append(MARK);
		pickled.append(LIST);

		for (GraphiteMetricTuple metric : metrics) {

			// lines.append(part[0] + " " + part[1] + " " + part[2] + "\n");
			// start the outer tuple
			pickled.append(MARK);
			// the metric name is a string.
			pickled.append(STRING);
			// the single quotes are to match python's repr("abcd")
			pickled.append('\'');
			pickled.append(metric.name);
			pickled.append('\'');
			pickled.append('\n');

			// start the inner tuple
			pickled.append(MARK);

			// timestamp is a long
			pickled.append(LONG);
			pickled.append(metric.timestamp);
			// the trailing L is to match python's repr(long(1234))
			pickled.append('L');
			pickled.append('\n');

			// and the value is a string.
			pickled.append(STRING);
			pickled.append('\'');
			pickled.append(metric.value);
			pickled.append('\'');
			pickled.append('\n');

			pickled.append(TUPLE); // inner close
			pickled.append(TUPLE); // outer close

			pickled.append(APPEND);
		}
		// every pickle ends with STOP
		pickled.append(STOP);
		return pickled.toString();
	}

	private void sendPayload(String payload) throws UnknownHostException, IOException {
		int length = payload.length();
		byte[] header = ByteBuffer.allocate(4).putInt(length).array();

		// Check if connection is established
//		if (this.graphiteSocket == null || this.graphiteSocket.isClosed()) {
//			connectToGraphiteSocket();
//		}
		this.out.write(header);
		this.pickleWriter.write(payload);
		this.pickleWriter.flush();
		metrics.clear();
	}

	public void closeGraphiteConnection() {
		try {
			
			if (this.graphiteSocket != null || this.graphiteSocket.isConnected()) {
				this.pickleWriter.close();
				this.graphiteSocket.close();
			}
			
			
		} catch (IOException e2) {// TODO null pointer exception
			logger.warn("\nCan't close graphite socket. " + e2 + "\n");
		}
		
	}

}

class GraphiteMetricTuple {
	String name;
	long timestamp;
	String value;

	GraphiteMetricTuple(String name, long timestamp, Double value) {
		this.name = name;
		this.timestamp = timestamp;
		this.value = value.toString();
	}
}
