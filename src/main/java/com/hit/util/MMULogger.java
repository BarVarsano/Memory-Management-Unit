package com.hit.util;
 
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import com.hit.memoryunits.HardDisk;

/*
* Bar Varsano 307929497
* Amit Levy 305650517
*/
public class MMULogger {
	/* Members */
	public final static String DEFAULT_FILE_NAME = "log.txt";
	private FileHandler handler;
	private static final Object lock = new Object();
	private static MMULogger instance;
	private Formatter localFormatter;
	private LogRecord recordToSave;

	/* Constructor */
	private MMULogger(){
		try{
		handler = new FileHandler(DEFAULT_FILE_NAME);
		localFormatter = new OnlyMessageFormatter();
		handler.setFormatter(localFormatter);
		}
		catch (SecurityException | IOException e) {
			e.printStackTrace();
		} 
	}

	/* Public Methods */
	public synchronized void write(String command, Level level) {
		recordToSave = new LogRecord(level, command + "\r\n");
		handler.publish(recordToSave);
	}

	public static MMULogger getInstance() {
		// Checking whether the instance has been created before.
		if (instance == null) {
			// preventing from other threads to interrupt current thread.
			synchronized (lock) {
				// Checking again whether the instance has been created or not,
				// double check.
				if (instance == null)
					instance = new MMULogger();
			}
		}

		return instance;
	}

	public class OnlyMessageFormatter extends Formatter {
		public OnlyMessageFormatter() {
			super();
		}

		@Override
		public String format(final LogRecord record) {
			return record.getMessage();
		}
	}
}