package data;

import java.util.ArrayList;
import java.util.Comparator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tools.Config;
import tools.Tools;

/**
 * 
 * Represents a song with all its relative info.
 * By default, it is created using a JSON file.
 * 
 * @author Yohan Chalier
 *
 */
public class Song {
	
	// Remove domains such as Soundcloud or Spotify (for now)
	public  static final String DOMAIN_FILTER = "youtube.com;youtu.be;soundcloud.com";
	
	// Id
	public String id;
	
	// Song tags
	public String artist;
	public String title;
	public String genre;
	
	public ArrayList<Genre> genres;
	
	// Reddit ranking
	public int ups;
	public int downs;
	public int nRedditComments;
	
	// External ranking
	public int views;
	public int nExtComments;
	public int likes;
	public int dislikes;
	
	// Media
	public String domain;
	public String url;
	public String thumbnail;
	
	// Score
	public double fame;
	public double quality;
	
	
	public Song() {} // Needed for subclasses
	
	/**
	 * Builds a song given a JSON object, previously
	 * written using the toJSON method of this very
	 * class.
	 * 
	 * @param json The JSONObject containing song info
	 * @throws JSONException
	 */
	public Song(JSONObject json) throws JSONException {
		id              = json.getString("id");
		artist          = json.getString("artist");
		title           = json.getString("title");
		genre           = json.getString("genre");
		ups             = json.getInt("ups");
		downs           = json.getInt("downs");
		nRedditComments = json.getInt("nRedditComments");
		views           = json.getInt("views");
		nExtComments    = json.getInt("nExtComments");
		likes           = json.getInt("likes");
		dislikes        = json.getInt("dislikes");
		domain          = json.getString("domain");
		url             = json.getString("url");
		thumbnail       = json.getString("thumbnail");
		
		JSONArray arr = json.getJSONArray("genres");
		genres = new ArrayList<Genre>();
		for (int i = 0; i < arr.length(); i++) {
			genres.add(new Genre(arr.getJSONObject(i)));
		}
	}
		
	/**
	 * Creates a JSON containing all song data
	 * 
	 * @return The JSON object containing the song data
	 * @throws JSONException
	 */
	public JSONObject toJSON() throws JSONException {
		JSONObject json = new JSONObject();
		json.put("id", id);
		json.put("artist", artist);
		json.put("title", title);
		json.put("genre", genre);
		json.put("ups", ups);
		json.put("downs", downs);
		json.put("nRedditComments", nRedditComments);
		json.put("views", views);
		json.put("nExtComments", nExtComments);
		json.put("likes", likes);
		json.put("dislikes", dislikes);
		json.put("domain", domain);
		json.put("url", url);
		json.put("thumbnail", thumbnail);
		JSONArray arr = new JSONArray();
		for (Genre genre : genres)
			arr.put(genre.toJSON());
		json.put("genres", arr);
		return json;
	}
	
	public String toStringGenres() {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < genres.size(); i++) {
			builder.append(genres.get(i).toString());
			if (i < genres.size() - 1)
				builder.append(" / ");
		}
		return builder.toString();
	}
	
	/**
	 * Format:
	 * Artist - Title [genre] (fame score/quality score)
	 */
	@Override
	public String toString() {
		return artist + " - " + title + " " + toStringGenres() + " "
				+ "(" + Tools.doubleToStr(fame)
				+ "/" + Tools.doubleToStr(quality) + ")";
	}
	
	/**
	 * Computes the average between the fame score
	 * and the quality score.
	 * 
	 * @return the score of the song
	 */
	public double meanScore() {
		return ( Config.WEIGHT_FAME * fame 
			   + Config.WEIGHT_QUALITY * quality
			   ) / 2;
	}
	
	/**
	 * @return A comparator for Song, based on their score
	 * 		   in decreasing order.
	 */
	public static Comparator<Song> comparator() {
		return new Comparator<Song>() {

			@Override
			public int compare(Song arg0, Song arg1) {
				if (arg0.meanScore() > arg1.meanScore())
					return -1;
				if (arg0.meanScore() < arg1.meanScore())
					return  1;
				return 0;
			}
			
		};
	}
		
}
