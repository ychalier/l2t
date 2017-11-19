package data;

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
			String[] split = genraNoSpace.split(" |-");
			main = split[split.length-1];      // Main word in English is at the end
			subs = new String[split.length-1]; // The others are adjectives-like
			for(int i=0; i<subs.length; i++) subs[i] = split[i];
		}
	}
	
	public String toString() {
		return main;
	}

}
