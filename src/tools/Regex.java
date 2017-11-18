package tools;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regex {
	
	public static final String PATTERN_SPACES        = " *(\\S.*\\S) *";
	public static final String PATTERN_TITLE_FULL    = "(.*) --? (.*) \\[(.*)\\] ?\\((.*)\\)";
	public static final String PATTERN_TITLE_NO_YEAR = "(.*) --? (.*) \\[(.*)\\]";
	public static final String PATTERN_VIDEO_ID_COM  = "watch\\?v=(.{11})";
	public static final String PATTERN_VIDEO_ID_BE   = "youtu.be/(.{11})";
	public static final String PATTERN_ROUTE         = "\\/(.*) ";
	
	public static String parse(String pattern, String target) {
		return parse(pattern, target, 1, false);
	}
	
	public static String parse(String pattern, String target, int group) {
		return parse(pattern, target, group, false);
	}

	public static String parse(String pattern, String target, int group, boolean log) {
		Matcher matcher = Pattern.compile(pattern).matcher(target);
		if (matcher.find())
			return matcher.group(group);
		if (log)
			System.out.println("Error parsing\t" + pattern + "\t" + target);
		return null;
	}
	
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
