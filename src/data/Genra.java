package data;

import tools.Regex;

public class Genra {
		
	public String   main;
	public String[] subs;
	
	public Genra(String genra) {		
		String[] split = Regex.parse(Regex.PATTERN_SPACES, genra.toLowerCase()).split(" |-");
		main = split[split.length-1];
		subs = new String[split.length-1];
		for(int i=0; i<subs.length; i++) subs[i] = split[i];
	}
	
	public String toString() {
		return main;
	}

}
