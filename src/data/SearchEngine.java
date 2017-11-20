package data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 
 * Implements a search engine to handle
 * genre queries with a library.
 * 
 * @author Yohan Chalier
 *
 */
public class SearchEngine {
	
	private static final int    MATCH_SCORE_MAIN   = 3;
	private static final int    MATCH_SCORE_SUBS   = 1;
	private static final double WEIGHT_SONG_SCORE  = 1;
	private static final double WEIGHT_MATCH_SCORE = 1;
	
	private final Library library;
	
	private Set<String>        parse;
	private ArrayList<Song>    results;  
	private Map<Song, Integer> scores;
	// Results are stored for further processing
		
	public SearchEngine(Library library) {
		this.library = library;
	}
	
	
	public ArrayList<Song> getResults(){
		return results;
	}
	
	/**
	 * Average of the song score and its score in
	 * genre matching.
	 * 
	 * @param song A song from the query to be scored
	 * @return The score of the song for the query
	 */
	private double score(Song song) {
		return WEIGHT_SONG_SCORE * song.meanScore()
				+ WEIGHT_MATCH_SCORE * (double) scores.get(song);
	}
	
	/**
	 * Finds songs corresponding to a query
	 * and orders them by relevance
	 * 
	 * @param query The search query containing a genre-like selection
	 * @return The order list of corresponding results.
	 */
	public ArrayList<Song> search(String query){
		// The set of unique words in the query
		parse   = new HashSet<String>(Arrays.asList(query.toLowerCase().split(" |-")));
		
		results = new ArrayList<Song>();
		scores  = new HashMap<Song, Integer>();
				
		for (Song song: library.getSongs()) {
			
			if (query.equals("")) { // Empty query means all song match
				results.add(song);
				scores.put(song, 0);
			} else {
			
				for (String word: parse) {
					
					// If one of the song main genre matches a word in the query,
					// the song is part of the results.
					for (Genre genre: song.genres)
						if (genre.main != null && genre.main.equals(word) && !results.contains(song)) {
							results.add(song);
							break;
						}
					
					// If the song has been selected, compute its score
					// A 'main' match is worth more points than a 'sub' one
					if (results.contains(song) && song.genre.toLowerCase().contains(word)) {
						
						int matchScore = MATCH_SCORE_SUBS;
						
						for (Genre genra: song.genres)
							if (genra.main.equals(word)) {
								matchScore = MATCH_SCORE_MAIN;
								break;
							}
						
						// Update of the hash map
						if (scores.containsKey(song))
							scores.put(song, scores.get(song) + matchScore);
						else
							scores.put(song, matchScore);
					}
				}
				
			}
			
		}
		
		// Sorting the results based on new songs score
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