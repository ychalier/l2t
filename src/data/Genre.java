package data;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tools.Regex;

/**
 * 
 * Represents one of the genre of a song.
 * 
 * @author Yohan Chalier
 *
 */
public class Genre {
		
	public String   main;
	public String[] subs;
	
	private static Map<String, List<String>> correspondences;
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
	
	public Genre(String genra) {
		// Removing spaces before and after the string
		String genraNoSpace = Regex.parse(Regex.PATTERN_SPACES, genra
				.toLowerCase()
				.replace("&amp;", "&"));
		
		if (genraNoSpace != null) { // The string should be well formatted then
			for (String main: correspondences.keySet())
				for (String sub: correspondences.get(main))
					genraNoSpace = genraNoSpace.replace(sub, main);
			String[] split = genraNoSpace.split(" |-");
			main = split[split.length-1];      // Main word in English is at the end
			subs = new String[split.length-1]; // The others are adjectives-like
			for(int i=0; i<subs.length; i++) subs[i] = split[i];
		}
	}
	
	/**
	 * Constructor from a previously generated JSON file
	 * 
	 * @param json The JSON object equivalent to toJSON
	 * @throws JSONException
	 */
	public Genre(JSONObject json) throws JSONException {
		main = json.getString("main");
		JSONArray arr = json.getJSONArray("subs");
		subs = new String[arr.length()];
		for(int i = 0; i < subs.length; i++) {
			subs[i] = arr.getString(i);
		}
	}
	
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		if (subs.length > 0) {
			builder.append("(");
			for (int i = 0; i < subs.length; i++) {
				builder.append(subs[i]);
				if (i < subs.length - 1) {
					builder.append(", ");
				}
			}
			builder.append(") ");
		}
		builder.append(main);
		return builder.toString();
	}
	
	/**
	 * Format: {"main": main, "subs": [sub1, ...]}
	 * 
	 * @return JSON containing all data from the object
	 * @throws JSONException
	 */
	public JSONObject toJSON() throws JSONException {
		JSONObject json = new JSONObject();
		json.put("main", main);
		JSONArray arr = new JSONArray();
		for (int i = 0; i < subs.length; i++)
			arr.put(subs[i]);
		json.put("subs", arr);
		return json;
	}

	
}
