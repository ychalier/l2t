package tools;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tools.Logger;

public class Config {
	
	public static int    PORT               = 8080;
	
	public static int    FETCH_AMOUNT       = 999;

	public static int    WEIGHT_FAME        = 2;
	public static int    WEIGHT_QUALITY     = 1;
	
	public static int    MATCH_SCORE_MAIN   = 3;
	public static int    MATCH_SCORE_SUBS   = 1;
	
	public static double WEIGHT_SONG_SCORE  = 1;
	public static double WEIGHT_MATCH_SCORE = 1;
	
	public static String FILE_LIBRARY       = "library.json";
	public static String FILE_TOKEN         = "token.json";
	
	public static String USER_AGENT         = "Mozilla/5.0";
	
	public static Map<String, List<String>> correspondences;
	static {
		correspondences = new HashMap<String, List<String>>();
		
		correspondences.put("hiphop", 
				Arrays.asList(new String[] {"hip-hop", "hip hop"}));
		
		correspondences.put("chillhop", 
				Arrays.asList(new String[] {"chill hop"}));
		
		correspondences.put("rnb", 
				Arrays.asList(new String[] {"r&b"}));
		
		correspondences.put("rock&roll", 
				Arrays.asList(new String[] {"rock'n'roll"}));
		
		correspondences.put("electro", 
				Arrays.asList(new String[] {"electronica", "electronic", "electonic"}));
		
		correspondences.put("psychedelic", 
				Arrays.asList(new String[] {"psych"}));
		
		correspondences.put("alternative", 
				Arrays.asList(new String[] {"alt"}));
		
		correspondences.put("acapella", 
				Arrays.asList(new String[] {"cappella"}));
		
		correspondences.put("chill", 
				Arrays.asList(new String[] {"chillout", "chillwave", "downtempo", "ambient"}));
		
		correspondences.put("pop electro", 
				Arrays.asList(new String[] {"electopop", "electropop"}));
		
		correspondences.put("punk electro", 
				Arrays.asList(new String[] {"electropunk"}));
		
		correspondences.put("rock electro", 
				Arrays.asList(new String[] {"electrorock"}));
		
		correspondences.put("edm", 
				Arrays.asList(new String[] {"idm"}));
		
		correspondences.put("indie", 
				Arrays.asList(new String[] {"indi"}));
		
		correspondences.put("jazz", 
				Arrays.asList(new String[] {"jazzhop"}));
	}
	
	public static void load(File configFile) {
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(configFile));
		} catch (FileNotFoundException e) {
			Logger.wr("Config file not found. Using default values.");
			System.out.println("Config file not found. Using default values.");
			return;
		}
		
		String line = null;
		boolean newCorr = false;
		try {
			while((line = reader.readLine()) != null) {
				String[] split = line.split("=");
				if (split.length == 2) {
					if (split[0].equals("PORT"))
						PORT = Integer.parseInt(split[1]);
					else if (split[0].equals("FETCH_AMOUNT"))
						FETCH_AMOUNT = Integer.parseInt(split[1]);
					else if (split[0].equals("WEIGHT_FAME"))
						WEIGHT_FAME = Integer.parseInt(split[1]);
					else if (split[0].equals("WEIGHT_QUALITY"))
						WEIGHT_QUALITY = Integer.parseInt(split[1]);
					else if (split[0].equals("MATCH_SCORE_MAIN"))
						MATCH_SCORE_MAIN = Integer.parseInt(split[1]);
					else if (split[0].equals("MATCH_SCORE_SUBS"))
						MATCH_SCORE_SUBS = Integer.parseInt(split[1]);
					else if (split[0].equals("WEIGHT_SONG_SCORE"))
						WEIGHT_SONG_SCORE = Integer.parseInt(split[1]);
					else if (split[0].equals("WEIGHT_MATCH_SCORE"))
						WEIGHT_MATCH_SCORE = Integer.parseInt(split[1]);
					else if (split[0].equals("FILE_LIBRARY"))
						FILE_LIBRARY = split[1];
					else if (split[0].equals("FILE_TOKEN"))
						FILE_TOKEN = split[1];
					else if (split[0].equals("USER_AGENT"))
						USER_AGENT = split[1];
					else if (split[0].equals("CORR")) {
						if (!newCorr) {
							newCorr = true;
							correspondences = new HashMap<String, List<String>>();
						}
						String[] split2 = split[1].split(":");
						if (split2.length == 2) {
							correspondences.put(split2[0], Arrays.asList(split2[1].split(";")));
						} else {
							throw new IOException();
						}
					}
				}
			}
		} catch (IOException e1) {
			e1.printStackTrace();
			Logger.wr("Error in config file at line: " + line);
			System.out.println("Error in config file at line: " + line);
		} finally {
			try {
				reader.close();
			} catch (IOException e) {}
		}
		
	}
	
}
