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

/**
 * 
 * Stores the configuration, and provides
 * a loading function to load a config file.
 * 
 * @author Yohan Chalier
 *
 */
public class Config {
	
	public static int    PORT               = 8080;
	
	public static int    SOCKET_TIMEOUT     = 10000;
	
	public static int    FETCH_AMOUNT       = 999;
	public static int    REFRESH_AMOUNT     = 50;

	public static int    WEIGHT_FAME        = 2;
	public static int    WEIGHT_QUALITY     = 1;
	
	public static int    MATCH_SCORE_MAIN   = 3;
	public static int    MATCH_SCORE_SUBS   = 1;
	
	public static double WEIGHT_SONG_SCORE  = 1;
	public static double WEIGHT_MATCH_SCORE = 1;
	
	public static String FILE_LIBRARY       = "library.json";
	public static String FILE_TOKEN         = "token.json";
	public static String FILE_LOG           = ".log";
	
	public static String USER_AGENT         = "Mozilla/5.0";
	
	public static Map<String, List<String>> correspondences;
	static {
		correspondences = new HashMap<String, List<String>>();
		correspondences.put("hiphop", Arrays.asList(new String[] {"hip-hop", "hip hop"}));
		correspondences.put("chillhop", Arrays.asList(new String[] {"chill hop"}));
		correspondences.put("rnb", Arrays.asList(new String[] {"r&b"}));
		correspondences.put("rock&roll", Arrays.asList(new String[] {"rock'n'roll"}));
		correspondences.put("electro", Arrays.asList(new String[] {"electronica", "electronic", "electonic"}));
		correspondences.put("psych", Arrays.asList(new String[] {"psychedelic"}));
		correspondences.put("alt", Arrays.asList(new String[] {"alternative"}));
		correspondences.put("cappella", Arrays.asList(new String[] {"acappella"}));
		correspondences.put("chill", Arrays.asList(new String[] {"chillout", "chillwave", "downtempo", "ambient"}));
		correspondences.put("pop electro", Arrays.asList(new String[] {"electopop", "electropop"}));
		correspondences.put("punk electro", Arrays.asList(new String[] {"electropunk"}));
		correspondences.put("rock electro", Arrays.asList(new String[] {"electrorock"}));
		correspondences.put("edm", Arrays.asList(new String[] {"idm"}));
		correspondences.put("indie", Arrays.asList(new String[] {"indi"}));
		correspondences.put("jazz", Arrays.asList(new String[] {"jazzhop"}));
	}
	
	public static void load(File configFile) {
		
		// Opening stream
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(configFile));
		} catch (FileNotFoundException e) {
			System.out.println("Config file not found. Using default values.");
			return;
		}
		
		String line = null;
		boolean newCorr = false;
		
		try {
			
			while((line = reader.readLine()) != null) {
				
				// Do not read comments or empty line
				if (line.length() > 0 && line.charAt(0) != '#') {
					
					String[] split = line.split("=");
					
					if (split.length == 2) {
						
						if (split[0].equals("PORT"))
							PORT = Integer.parseInt(split[1]);
						
						else if (split[0].equals("SOCKET_TIMEOUT"))
							SOCKET_TIMEOUT = Integer.parseInt(split[1]);
						
						else if (split[0].equals("FETCH_AMOUNT"))
							FETCH_AMOUNT = Integer.parseInt(split[1]);
						
						else if (split[0].equals("REFRESH_AMOUNT"))
							REFRESH_AMOUNT = Integer.parseInt(split[1]);
						
						else if (split[0].equals("WEIGHT_FAME"))
							WEIGHT_FAME = Integer.parseInt(split[1]);
						
						else if (split[0].equals("WEIGHT_QUALITY"))
							WEIGHT_QUALITY = Integer.parseInt(split[1]);
						
						else if (split[0].equals("MATCH_SCORE_MAIN"))
							MATCH_SCORE_MAIN = Integer.parseInt(split[1]);
						
						else if (split[0].equals("MATCH_SCORE_SUBS"))
							MATCH_SCORE_SUBS = Integer.parseInt(split[1]);
						
						else if (split[0].equals("WEIGHT_SONG_SCORE"))
							WEIGHT_SONG_SCORE = Double.parseDouble(split[1]);
						
						else if (split[0].equals("WEIGHT_MATCH_SCORE"))
							WEIGHT_MATCH_SCORE = Double.parseDouble(split[1]);
						
						else if (split[0].equals("FILE_LIBRARY"))
							FILE_LIBRARY = split[1];
						
						else if (split[0].equals("FILE_TOKEN"))
							FILE_TOKEN = split[1];
						
						else if (split[0].equals("FILE_LOG"))
							FILE_LOG = split[1];
						
						else if (split[0].equals("USER_AGENT"))
							USER_AGENT = split[1];
						
						else if (split[0].equals("CORR")) {
							
							if (!newCorr)
								correspondences = new HashMap<String, List<String>>();
							newCorr = true;
							
							String[] split2 = split[1].split(":");
							if (split2.length == 2) {
								correspondences.put(
										split2[0], 
										Arrays.asList(split2[1].split(";")));
							} else {
								System.out.println("Illegal correspondence line: " + line);
							}
						} else {
							System.out.println("Unknown config var: " + line);
						}							
						
					} else {
						System.out.println("Illegal config line: " + line);
					}
				}
			
			}
			
		} catch (IOException e) {
			System.out.println("Error in config file");
		} finally {
			try {
				reader.close();
			} catch (IOException e) {}
		}
		
	}
	
}
