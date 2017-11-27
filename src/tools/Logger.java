package tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * Implements basic logging utilities.
 * 
 * @author Yohan Chalier
 *
 */
public class Logger {
	
	/**
	 * A set of priorities (inspired from Android)
	 */
	private static final Map<String, Integer> PRIORITY = new HashMap<String, Integer>();
	static {
		PRIORITY.put("verbose", 0);
		PRIORITY.put("debug", 1);
		PRIORITY.put("information", 2);
		PRIORITY.put("warning", 3);
		PRIORITY.put("error", 4);
	}
	
	private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
	
	private static File logfile;
	private static int  priority;
	
	/**
	 * @param log	   Open logfile?
	 * @param priority Minimum priority to display messages
	 */
	public Logger(boolean log, int priority) {
		Logger.priority = priority;
		if (log)
			logfile = new File(Config.FILE_LOG);
	}
	
	/**
	 * Logs message with priority error
	 * 
	 * @param header Where the message comes from
	 * @param body	 Actual message
	 */
	public static void wrE(String header, String body) {
		wr(header, body, PRIORITY.get("error"));
	}
	
	/**
	 * Logs message with priority warning
	 * 
	 * @param header Where the message comes from
	 * @param body	 Actual message
	 */
	public static void wrW(String header, String body) {
		wr(header, body, PRIORITY.get("warning"));
	}
	
	/**
	 * Logs message with priority information
	 * 
	 * @param header Where the message comes from
	 * @param body	 Actual message
	 */
	public static void wrI(String header, String body) {
		wr(header, body, PRIORITY.get("information"));
	}
	
	/**
	 * Logs message with priority debug
	 * 
	 * @param header Where the message comes from
	 * @param body	 Actual message
	 */
	public static void wrD(String header, String body) {
		wr(header, body, PRIORITY.get("debug"));
	}
	
	/**
	 * Logs message with priority verbose
	 * 
	 * @param header Where the message comes from
	 * @param body	 Actual message
	 */
	public static void wrV(String header, String body) {
		wr(header, body, PRIORITY.get("verbose"));
	}
	
	/**
	 * Logs a message
	 * 
	 * @param header Where the message comes from
	 * @param body	 Actual message
	 * @param p		 Message's priority
	 */
	private static void wr(String header, String body, int p) {
		
		// Only handles messages with a priority high enough
		if (p >= priority) {
			
			// Building message
			String line = "[" + header + "] ";
			while (line.length() < 20) // Gives a fixed length to header
				line += " ";
			line += body;
			
			// Printing message
			System.out.println(line);
			
			// Appending message to file
			if (logfile != null) {
				FileWriter writer;
				Timestamp timestamp = new Timestamp(System.currentTimeMillis());
				try {
					writer = new FileWriter(logfile, true);
					writer.write(SDF.format(timestamp) + " " + line + "\n");
					writer.close();
				} catch (IOException e) {
					System.out.println("[LOG] Error writing to log file: " + line);
				}
			}
			
		}
	}

}
