package tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class Logger {
	
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
	
	public Logger(boolean log, int priority) {
		Logger.priority = priority;
		if (log)
			logfile = new File(Config.FILE_LOG);
	}
	
	public static void wrE(String header, String body) {
		wr(header, body, PRIORITY.get("error"));
	}
	
	public static void wrW(String header, String body) {
		wr(header, body, PRIORITY.get("warning"));
	}
	
	public static void wrI(String header, String body) {
		wr(header, body, PRIORITY.get("information"));
	}
	
	public static void wrD(String header, String body) {
		wr(header, body, PRIORITY.get("debug"));
	}
	
	public static void wrV(String header, String body) {
		wr(header, body, PRIORITY.get("verbose"));
	}
	
	private static void wr(String header, String body, int p) {
		
		if (p >= priority) {
			
			String line = "[" + header + "] ";
			while (line.length() < 20)
				line += " ";
			line += body;
			
			System.out.println(line);
			
			if (p >= PRIORITY.get("information") && logfile != null) {
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
