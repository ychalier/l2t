package data;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tools.Config;
import tools.JSONHandler;
import tools.Logger;

/**
 * 
 * Represents the library of songs retrieved from Reddit.
 * 
 * @author Yohan Chalier
 *
 */
public class Library {
			
	private Map<String, Song> songs;
	private Set<String>       likes;
	private Jury jury;
	
	public Library() {
		songs = new HashMap<String, Song>();
		likes = new HashSet<String>();
		jury  = new Jury(this);
	}
	
	/**
	 * Builds a Library from a list of Reddit posts.
	 * If a SongException occurs, then the song is not
	 * added to the database.
	 * 
	 * @param posts The JSON array containing the posts.
	 * 				Each post pertinent data are located in ["data"]
	 */
	public Library(JSONArray posts) {
		
		this();
		
		Logger.wrI("LIBRARY", "Building library from posts");
				
		for(int i=0; i<posts.length(); i++) {
			
			try {
				try {
					PostSong tmp = new PostSong(posts.getJSONObject(i).getJSONObject("data")); 
					songs.put(tmp.id, tmp);
				} catch (JSONException e) {
					throw new SongException("invalid post json");
				}
			} catch (SongException e) {
				Logger.wrW("LIBRARY", e.toString());
			}
				
		}
		
		Logger.wrI("LIBRARY", "Library built");
	}
	
	/**
	 * Builds a Library from an external JSON file.
	 * 
	 * @param file
	 * @throws JSONException
	 * @throws IOException
	 */
	public Library(File file) throws JSONException, IOException {
		
		this();
		
		Logger.wrI("LIBRARY", "Building library from JSON");
		
		JSONObject json = JSONHandler.load(file);
		
		JSONArray array = json.getJSONArray("list");
		for(int i = 0; i < array.length(); i++) {
			Song tmp = new Song(array.getJSONObject(i));
			songs.put(tmp.id, tmp);
		}
		array = json.getJSONArray("likes");
		for(int i = 0; i < array.length(); i++) {
			likes.add(array.getString(i));
		}
		
		Logger.wrI("LIBRARY", "Library built");
	}
	
	
	public Map<String, Song> getSongs(){
		return songs;
	}
	
	/**
	 * format:
	 * 001: Artist - Title [Genre] (Fame score/Quality score)
	 */
	@Override
	public String toString() {
		StringBuilder out = new StringBuilder();
		int i = 0;
		int maxSize = Integer.toString(songs.size()).length();
		
		for(String key: songs.keySet()) {
			// Add '0' to align all numbers
			for(int k = 0; k < maxSize - Integer.toString(i).length(); k++)
				out.append("0");
			out.append(Integer.toString(i) + ": " + songs.get(key).toString() + "\n");
		}
		
		return out.toString();
	}
	
	/**
	 * Creates a JSON object containing all info.
	 * The object has one key 'list' that contains
	 * an array of JSON objects, each one representing
	 * a song.
	 * 
	 * @return The library as a JSON object
	 * @throws JSONException
	 */
	public JSONObject toJSON() throws JSONException {
		
		// Adding songs
		JSONArray array = new JSONArray();
		for(String key : songs.keySet())
			array.put(songs.get(key).toJSON());
		JSONObject json = new JSONObject();
		json.put("list", array);
		
		// Adding likes
		array = new JSONArray();
		for(String id: likes)
			array.put(id);
		json.put("likes", array);
		
		return json;
	}
	
	
	public void like(String songId) {
		likes.add(songId);
	}
	
	
	public void append(JSONArray posts) {
		
		Logger.wrI("LIBRARY", "Refreshing library");
		
		for(int i=0; i<posts.length(); i++) {
			
			try {
				try {
					PostSong tmp = new PostSong(posts.getJSONObject(i).getJSONObject("data"));
					if (!songs.containsKey(tmp.id))
						songs.put(tmp.id, tmp);
				} catch (JSONException e) {
					throw new SongException("invalid post json");
				}
			} catch (SongException e) {
				Logger.wrW("LIBRARY", e.toString());
			}
				
		}
		
		Logger.wrI("LIBRARY", "Library refreshed");
		
	}
	
	
	public void save() throws IOException, JSONException {
		save(Config.FILE_LIBRARY);
	}
	
	
	public void save(String filename) throws IOException, JSONException {
		JSONHandler.save(toJSON(), filename);
	}
	
	/**
	 * Computes and set the scores (fame & quality)
	 * for each song.
	 */
	public void computeScores() {
		Logger.wrI("LIBRARY", "Computing scores");
		jury.computeScores();
		Logger.wrI("LIBRARY", "Scores computed");
	}
	
	/**
	 * Get the list of main genre in the library,
	 * and the number of occurrences.
	 * 
	 * @return A map of the genres and their number
	 * 		   of occurrences.
	 */
	public Map<String, Integer> getGenres(){
		Map<String, Integer> map = new HashMap<String, Integer>();
		for (String key: songs.keySet())
			for (Genre genre: songs.get(key).genres)
				if (map.containsKey(genre.main))
					map.put(genre.main, map.get(genre.main) + 1);
				else
					map.put(genre.main, 1);
		return map;
	}
	
}
