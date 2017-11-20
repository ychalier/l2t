package tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class Logger {
	
	private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
	
	private static File logfile;
	
	public Logger() {
		logfile = new File(".log");
	}
	
	public static void wr(String msg) {
		FileWriter writer;
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		try {
			writer = new FileWriter(logfile, true);
			writer.write(SDF.format(timestamp) + ": " + msg + "\n");
			writer.close();
		} catch (IOException e) {}
	}

}
