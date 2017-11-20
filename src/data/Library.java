package data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import scrapper.RedditAPI;
import tools.JSONHandler;
import web.Server;

/**
 * 
 * Represents the library of songs retrieved from Reddit.
 * 
 * @author Yohan Chalier
 *
 */
public class Library {
		
	private static final String FILE_LIBRARY = "library.json";
	
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
		System.out.println("Building library...");
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
				//TODO: log those SongExceptions
				System.out.println(e);
			}
				
		}
		
		jury = new Jury(this);
		System.out.println("Library built.");
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
		System.out.print("Computing scores... ");
		jury.computeScores();
		System.out.println("Done.");
	}
	
	/**
	 * If a library already exists in FILE_LIBRARY, loads it.
	 * Else, create a Reddit API to fetch a new library.
	 * 
	 * @return A new library.
	 * @throws Exception
	 */
	public static Library loadLibrary() throws Exception {
		Library library;
		if (new File(FILE_LIBRARY).exists()) {
			library = new Library(new File(FILE_LIBRARY));
		} else {
			// Create the Reddit API
			RedditAPI api = new RedditAPI(RedditAPI.DEFAULT_CLIENT_ID, 
					RedditAPI.DEFAULT_REDIRECT_URI
					.replace("PORT", Integer.toString(Server.PORT)));
			
			// Retrieve or refresh token
			api.auth();
			
			// Fetch data and build library
			library = new Library(api.fetchData(RedditAPI.DEFAULT_FETCH_AMOUNT));
			
			// Save library has JSON
			JSONHandler.save(library.toJSON(), FILE_LIBRARY);
		}
		library.computeScores();
		return library;
	}
	
}
