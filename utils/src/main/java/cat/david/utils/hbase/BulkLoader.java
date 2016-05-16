package cat.david.utils.hbase;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.RetriesExhaustedWithDetailsException;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BulkLoader {

	private BlockingQueue<KeyValue> queue;
	private int bufferSize = 65536;
	private int waitingTime		= 1000;
	private ExecutorService executorService;
	private Logger log = LoggerFactory.getLogger(BulkLoader.class);
	private boolean ended = false;

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
	 * 
	 * @return how many entries can the queue hold
	 */
	public int getBufferSize() {
		return bufferSize;
	}

	/**
	 * 
	 * @param bufferSize number of entries the queue can hold
	 */
	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	}

	/**
	 * 
	 * @return the time in milliseconds we'll wait before the next queue check
	 */
	public int getWaitingTime() {
		return waitingTime;
	}

	/**
	 * 
	 * @param waitingTime during ending, we'll check the queue is empty 
	 * again after waitingTime milliseconds
	 */
	public void setWaitingTime(int waitingTime) {
		this.waitingTime = waitingTime;
	}

	/**
	 * Starts a new thread that will listen through the 
	 * {@link #put(String, String) put} method until
	 * {@link #end end} is called
	 * @param conf use HBaseConfiguration.create() to create it
	 * @param table where all data will go
	 * @param columnFamily to make it simple, one BulkLoader per family
	 * @param columnValue where the data will lay
	 * @param waitingTime during ending, we'll check the queue is empty 
	 * again after waitingTime milliseconds
	 * @param bufferSize elements in the queue
	 * @throws IOException
	 */
	public BulkLoader(Configuration conf, String table, byte[] columnFamily, byte[] columnValue, Integer waitingTime, Integer bufferSize) throws IOException {
		super();
		if (waitingTime!=null)
			this.waitingTime = waitingTime;
		if (bufferSize!=null)
			this.bufferSize = bufferSize;
		execute(conf, table, columnFamily, columnValue);
	}
	
	/**
	 * Starts a new thread that will listen through the 
	 * {@link #put(String, String) put} method until
	 * {@link #end end} is called
	 * @param conf use HBaseConfiguration.create() to create it
	 * @param table where all data will go
	 * @param columnFamily to make it simple, one BulkLoader per family
	 * @param columnValue where the data will lay
	 * @throws IOException if the object to refer to the table could not be created
	 */
	public BulkLoader(Configuration conf, String table, byte[] columnFamily, byte[] columnValue) throws IOException {
		super();
		execute(conf, table, columnFamily, columnValue);
	}
	
	/**
	 * 
	 * @param conf hadoop conf
	 * @param table where data goes
	 * @param columnFamily where the column lays
	 * @param columnValue where the data will lay
	 * @throws IOException if the object to refer to the table could not be created
	 */
	public void execute(Configuration conf, String table, byte[] columnFamily, byte[] columnValue) throws IOException {
		htable = new HTable(conf, table);
		final byte[] cf = columnFamily;
		final byte[] cv = columnValue;
		queue = new ArrayBlockingQueue<KeyValue>(bufferSize);
		executorService = Executors.newSingleThreadExecutor();
		executorService.execute(new Runnable() {
		    public void run() {
		        while(!ended){
		        	KeyValue keyValue;
					try {
						keyValue = queue.take();
						if(ended) break;
						writeToHbase(keyValue, cf, cv);
					} catch (InterruptedException e) {
						if(ended) break;
						log.error("Interrupted while waiting for {}", htable.getName());
						log.error("{}", e);
						return;
					}
		        }
		    }
		});
	}
	
	/**
	 * Actually does the job
	 * @param keyValue data to be written
	 * @param columnFamily where the column lays
	 * @param columnValue where the data goes
	 */
	private void writeToHbase(KeyValue keyValue, byte[] columnFamily, byte[] columnValue){
    	Put p = new Put(keyValue.key);
    	byte[] value = Bytes.toBytes(keyValue.value);
		p.add(columnFamily, columnValue, value);
    	try {
			htable.put(p);
		} catch (RetriesExhaustedWithDetailsException e) {
			log.error("{}", e);
		} catch (InterruptedIOException e) {
			log.error("{}", e);
		}
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
			log.error("Interrupted while putting in {}", htable.getName());
			log.error("{}", e);
		}
	}
	
	/**
	 * the queue is waiting in an eternal take. 
	 * Let's inject a pill to unstuck it
	 */
	private void honorEndOfQueue(){
		ended = true;
		try {
			queue.put(new KeyValue(new byte[]{},""));
		} catch (InterruptedException e) {
			log.error("{}", e);
		}		
	}
	
	/**
	 * Waits until all jobs are terminated and closes all services
	 */
	public void end(){
		int queueSize = queue.size();
		log.info("Terminating BulkLoader with {} pending entries", queueSize);
		while(queue.size()>0)
			try {
				Thread.sleep(waitingTime);
				if(queue.size()==queueSize)//oops, we're not consuming?
					Thread.sleep(waitingTime*9);
				queueSize = queue.size();
				if (queueSize>0)
					log.info("Still {} for {}", queueSize, htable.getName());
				else
					log.info("The queue is clean");
			} catch (InterruptedException e) {
				log.error("{}", e);
			}
		try {
			this.htable.close();
		} catch (IOException e) {
			log.error("{}", e);
		}
		honorEndOfQueue();
		log.info("Table {} closed. Shutting down", htable);
		executorService.shutdown();
		try {
		    if (!executorService.awaitTermination(waitingTime, TimeUnit.MILLISECONDS)) {
		        executorService.shutdownNow();
		    }
		} catch (InterruptedException e) {
		    List<Runnable> shutable = executorService.shutdownNow();
		    log.error("Shutting down {} threads", shutable.size());
		}
		log.info("Service shut down");
	}
}
