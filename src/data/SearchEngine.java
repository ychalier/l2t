package data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SearchEngine {
	
	private static final int    MATCH_SCORE_MAIN   = 3;
	private static final int    MATCH_SCORE_SUBS   = 1;
	private static final double WEIGHT_SONG_SCORE  = 1;
	private static final double WEIGHT_MATCH_SCORE = 1;
	
	private final Library library;
	
	private Set<String>        parse;
	private ArrayList<Song>    results;
	private Map<Song, Integer> scores;
	
		
	public SearchEngine(Library library) {
		this.library = library;
	}
	
	
	private double score(Song song) {
		return WEIGHT_SONG_SCORE * song.meanScore()
				+ WEIGHT_MATCH_SCORE * (double) scores.get(song);
	}
	
	
	public ArrayList<Song> search(String query){
		parse   = new HashSet<String>(Arrays.asList(query.toLowerCase().split(" |-")));
		results = new ArrayList<Song>();
		scores  = new HashMap<Song, Integer>();
				
		for (Song song: library.getSongs()) {
			
			if (query.equals("")) {
				results.add(song);
				scores.put(song, 0);
			} else {
			
				for (String word: parse) {
					
					for (Genra genra: song.genras)
						if (genra.main.equals(word) && !results.contains(song)) {
							results.add(song);
							break;
						}
					
					if (results.contains(song) && song.genra.toLowerCase().contains(word)) {
						
						int matchScore = MATCH_SCORE_SUBS;
						
						for (Genra genra: song.genras)
							if (genra.main.equals(word)) {
								matchScore = MATCH_SCORE_MAIN;
								break;
							}
						
						if (scores.containsKey(song))
							scores.put(song, scores.get(song) + matchScore);
						else
							scores.put(song, matchScore);
					}
				}
				
			}
			
		}
		
		results.sort(new Comparator<Song>() {

			@Override
			public int compare(Song song0, Song song1) {
				if (score(song0) > score(song1)) return -1;
				if (score(song0) < score(song1)) return  1;
				return 0;
			}
			
		});
		
		return results;
	}

}
