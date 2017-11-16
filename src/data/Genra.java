package data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Genra {
	
	private static final String PATTERN_SPACES = " *(\\S.*\\S) *";
	
	public String   main;
	public String[] subs;
	
	private String applyPattern(String patternString, String target) {
		Pattern pattern = Pattern.compile(patternString);
		Matcher matcher = pattern.matcher(target);
		if (matcher.find()) return matcher.group(1);
		System.out.println(patternString + "\t" + target);
		return null;
	}
	
	public Genra(String genraString) {		
		String[] split = applyPattern(PATTERN_SPACES, genraString).split(" |-");
		main = split[split.length-1];
		subs = new String[split.length-1];
		for(int i=0; i<subs.length; i++) subs[i] = split[i];
	}
	
	public String toString() {
		return main;
	}

}
