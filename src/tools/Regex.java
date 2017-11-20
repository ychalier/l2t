package tools;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * Provides tools to ease regular expressions usage.
 * 
 * @author Yohan Chalier
 *
 */
public class Regex {
	
	// Detects spaces before or after a string
	public static final String PATTERN_SPACES   = " *(\\S.*\\S) *";
	
	// Handles Reddit /r/listenttothis title format.
	// Beware of different dashes used to split artist and title
	public static final String PATTERN_TITLE = "([^\\[]+) (--?|—|–) ([^\\[]+) ?\\[([^\\[]+)\\]";
	
	// Detects YouTube video id, of length 11
	// Adapted for youtube.com, youtu.be
	// and what seems to be the share option on youtube.com
	public static final String PATTERN_VIDEO_ID = "(v=|be\\/|%3Fv%3D)(.{11})";
	
	// Handles HTTP GET requests, to remove GET and HTTP/1.1
	public static final String PATTERN_ROUTE    = "\\/(.*) ";
	
	/**
	 * Applies a pattern to a string.
	 * @param pattern String of the regular expression to use
	 * @param target String to test the pattern on
	 * @return The first match got from applying the pattern to the target,
	 * 		   null if none is found.
	 */
	public static String parse(String pattern, String target) {
		return parse(pattern, target, 1, false);
	}
	
	/**
	 * Applies a pattern to a string
	 * @param pattern String of the regular expression to use
	 * @param target String to test the pattern on
	 * @param group Index of the group result to retrieves
	 * @return The [group]-th match got from applying the pattern to the target,
	 * 		   null if none is found.
	 */
	public static String parse(String pattern, String target, int group) {
		return parse(pattern, target, group, false);
	}
	

	/**
	 * Applies a pattern to a string
	 * @param pattern String of the regular expression to use
	 * @param target String to test the pattern on
	 * @param group Index of the group result to retrieves
	 * @param log Enable printing lines giving errors for debug
	 * @return The [group]-th match got from applying the pattern to the target,
	 * 		   null if none is found.
	 */
	public static String parse(String pattern, String target, int group, boolean log) {
		Matcher matcher = Pattern.compile(pattern).matcher(target);
		if (matcher.find())
			return matcher.group(group);
		if (log)
			System.out.println("Error parsing\t" + pattern + "\t" + target);
		return null;
	}
	
	/**
	 * Applies a pattern to a string and retrieve all matching groups
	 * @param pattern String of the regular expression to use
	 * @param target String to test the pattern on
	 * @return A list of the matchs, or null if none is found.
	 */
	public static List<String> parseAll(String pattern, String target){
		List<String> matchs = new ArrayList<String>();
		
		Matcher matcher = Pattern.compile(pattern).matcher(target);
		
		while (matcher.find())
			for(int i = 0; i<= matcher.groupCount(); i++)
				matchs.add(matcher.group(i));
		
		if (matchs.size() == 0) return null;
		return matchs;
	}
	
}
