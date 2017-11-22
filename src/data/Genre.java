package data;

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
	
	public Genre(String genra) {
		// Removing spaces before and after the string
		String genraNoSpace = Regex.parse(Regex.PATTERN_SPACES, genra.toLowerCase());
		
		if (genraNoSpace != null) { // The string should be well formatted then
			String[] split = genraNoSpace
					.replace("hip-hop",   "hiphop")
					.replace("hip hop",   "hiphop")
					.replace("chill hop", "chillhop")
					.split(" |-");
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
