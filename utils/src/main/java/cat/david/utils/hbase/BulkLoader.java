package cat.david.utils.hbase;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.RetriesExhaustedWithDetailsException;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BulkLoader {

	private BlockingQueue<KeyValue> queue;
	public static int bufferSize = 65536;//static, yes, improvable after the platform
	private int WAITING_TIME		= 1000;
	private ExecutorService executorService;
	private Logger log = LoggerFactory.getLogger(BulkLoader.class);

	private HTable htable;

	private class KeyValue{
		byte[] key;
		String value;
		public KeyValue(byte[] key, String value) {
			super();
			this.key = key;
			this.value = value;
		}
	}
	
	/**
	 * Starts a new thread that will listen through the 
	 * {@link #put(String, String) put} method until
	 * {@link #end end} is called
	 * @param conf use HBaseConfiguration.create() to create it
	 * @param table where all data will go
	 * @param columnFamily to make it simple, one BulkLoader per family
	 * @throws IOException
	 */
	public BulkLoader(Configuration conf, String table, byte[] columnFamily, byte[] columnValue) throws IOException {
		super();
		htable = new HTable(conf, table);
		final byte[] cf = columnFamily;
		final byte[] cv = columnValue;
		queue = new ArrayBlockingQueue<KeyValue>(bufferSize);
		executorService = Executors.newSingleThreadExecutor();
		executorService.execute(new Runnable() {
		    public void run() {
		        while(true){
		        	KeyValue keyValue;
					try {
						keyValue = queue.take();
			        	Put p = new Put(keyValue.key);
			        	byte[] value = Bytes.toBytes(keyValue.value);
			    		p.add(cf, cv, value);
			        	htable.put(p);
					} catch (InterruptedException e) {
						log.error("{}", e);
						Thread.currentThread().interrupt();
					} catch (RetriesExhaustedWithDetailsException e) {
						log.error("{}", e);
					} catch (InterruptedIOException e) {
						log.error("{}", e);
						Thread.currentThread().interrupt();
					}
		        }
		    }
		});
	}
	
	/**
	 * Puts this new pair into the table. If the buffer is full, it waits 
	 * until there's room
	 * @param key
	 * @param value
	 */
	public void put(byte[] key, String value){
		try {
			queue.put(new KeyValue(key, value));
		} catch (InterruptedException e) {
			log.error("{}", e);
		}
	}
	
	/**
	 * Waits until all jobs are terminated and closes all services
	 */
	public void end(){
		log.info("Terminating BulkLoader with {} pending entries", queue.size());
		while(queue.size()>0)
			try {
				Thread.sleep(WAITING_TIME);
			} catch (InterruptedException e) {
				log.error("{}", e);
			}
		executorService.shutdown();
		try {
			this.htable.close();
		} catch (IOException e) {
			log.error("{}", e);
		}
	}
}
