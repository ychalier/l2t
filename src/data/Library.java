package data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import scrapper.RedditAPI;
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
			
	private ArrayList<Song> songs;
	private Jury jury;
	
	/**
	 * Builds a Library from a list of Reddit posts.
	 * If a SongException occurs, then the song is not
	 * added to the database.
	 * 
	 * @param posts The JSON array containing the posts.
	 * 				Each post pertinent data are located in ["data"]
	 */
	public Library(JSONArray posts) {
		System.out.print("Building library...");
		Logger.wr("Building library...");
		// Printing new line as SongException are likely to occur
		songs = new ArrayList<Song>();
		
		for(int i=0; i<posts.length(); i++) {
			
			try {
				try {
					songs.add(new PostSong(posts.getJSONObject(i).getJSONObject("data")));
				} catch (JSONException e) {
					throw new SongException("invalid post json");
				}
			} catch (SongException e) {
				Logger.wr(e.toString());
			}
				
		}
		
		jury = new Jury(this);
		System.out.println(" Done.");
		Logger.wr("Building library done.");
	}
	
	/**
	 * Builds a Library from an external JSON file.
	 * 
	 * @param file
	 * @throws JSONException
	 * @throws IOException
	 */
	public Library(File file) throws JSONException, IOException {
		System.out.print("Building library...");
		songs = new ArrayList<Song>();
		
		JSONArray array = JSONHandler.load(file).getJSONArray("list");
		for(int i = 0; i < array.length(); i++)
			songs.add(new Song(array.getJSONObject(i)));
		
		jury = new Jury(this);
		System.out.println(" Done.");
	}
	

	public ArrayList<Song> getSongs(){
		return songs;
	}
	
	/**
	 * Format:
	 * 001: Artist - Title [Genre] (Fame score/Quality score)
	 */
	@Override
	public String toString() {
		StringBuilder out = new StringBuilder();
		for(int i = 0; i < songs.size(); i++) {
			// Add '0' to align all numbers
			int maxSize = Integer.toString(songs.size()).length();
			for(int k = 0; k < maxSize - Integer.toString(i+1).length(); k++)
				out.append("0");
			out.append(Integer.toString(i+1) + ": " + songs.get(i).toString() + "\n");
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
		JSONArray array = new JSONArray();
		for(Song song : songs) array.put(song.toJSON());
		JSONObject json = new JSONObject();
		json.put("list", array);
		return json;
	}
	
	/**
	 * Computes and set the scores (fame & quality)
	 * for each song.
	 */
	public void computeScores() {
		Logger.wr("Computing scores...");
		System.out.print("Computing scores... ");
		jury.computeScores();
		System.out.println("Done.");
		Logger.wr("Computing scores done.");
	}
	
	public Map<String, Integer> getGenres(){
		Map<String, Integer> map = new HashMap<String, Integer>();
		for (Song song: songs)
			for (Genre genre: song.genres)
				if (map.containsKey(genre.main))
					map.put(genre.main, map.get(genre.main) + 1);
				else
					map.put(genre.main, 1);
		return map;
	}
	
	/**
	 * If a library already exists in FILE_LIBRARY, loads it.
	 * Else, create a Reddit API to fetch a new library.
	 * 
	 * @return A new library.
	 * @throws Exception
	 */
	public static Object[] loadLibrary() throws Exception {
		Library library;
		Boolean newLibrary;
		if (new File(Config.FILE_LIBRARY).exists()) {
			library = new Library(new File(Config.FILE_LIBRARY));
			newLibrary = new Boolean(false);
		} else {
			// Create the Reddit API
			RedditAPI api = new RedditAPI(RedditAPI.DEFAULT_CLIENT_ID, 
					RedditAPI.DEFAULT_REDIRECT_URI
					.replace("PORT", Integer.toString(Config.PORT)));
			
			// Retrieve or refresh token
			api.auth();
			
			// Fetch data and build library
			library = new Library(api.fetchData(Config.FETCH_AMOUNT));
			
			// Save library has JSON
			JSONHandler.save(library.toJSON(), Config.FILE_LIBRARY);
			newLibrary = new Boolean(true);
		}
		library.computeScores();
		return new Object[] {library, newLibrary};
	}
	
}
