package data;

import java.io.IOException;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import scrapper.YouTubeAPI;
import tools.Regex;

/**
 * 
 * Represents a song but from a Reddit post JSON.
 * More processing involved (including YouTube API).
 * 
 * @author Yohan Chalier
 *
 */
public class PostSong extends Song {

	/**
	 * Builds a song given a JSON object representing
	 * the song in a Reddit post.
	 * 
	 * @param json The direct element from Reddit post.
	 * 			   Corresponds to the "data" field of an element
	 * 			   from the JSONArray.
	 * @throws JSONException
	 * @throws IOException
	 */
	public PostSong(JSONObject json) throws JSONException, IOException {
		super();
		
		id              = json.getString("id");
		ups             = json.getInt("ups");
		downs           = json.getInt("downs");
		nRedditComments = json.getInt("num_comments");
		domain          = json.getString("domain");
		url             = json.getString("url");
		thumbnail       = json.getString("thumbnail");
		
		// Extract artist, title, genre and year
		parseTitle(json.getString("title"));
		
		// Interpret the genre string found earlier
		if (genre != null)
			parseGenre(genre);
		
		// Fetching YouTube statistics
		if (domain.equals("youtube.com") || domain.equals("youtu.be")) {
			JSONObject statistics = YouTubeAPI.getStatistics(this);
			if(statistics != null) {
				try {
					views = statistics.getInt("viewCount");
					nExtComments = statistics.getInt("commentCount");
					likes = statistics.getInt("likeCount");
					dislikes = statistics.getInt("dislikeCount");
				} catch (org.json.JSONException e) {
					// e.printStackTrace();
				}
			}
		}
		
		// For now, only youtube.com and youtu.be are supported
		if (!DOMAIN_FILTER.contains(domain)) artist = null;
		
	}
	
	/**
	 * Interpret post title to extract the artist,
	 * the title, the genre(s) and the year.
	 * Year is optional (null if not found).
	 * 
	 * @param title Reddit post title (given by OP)
	 */
	private void parseTitle(String title) {
		
		List<String> matchs;
		
		// Parsing through a regular expression
		// Firstly trying with year, and then without if it fails
		if ((matchs = Regex.parseAll(Regex.PATTERN_TITLE_FULL, title)) == null)
			 matchs = Regex.parseAll(Regex.PATTERN_TITLE_NO_YEAR, title);
		if (matchs == null) return;
		
		this.artist = matchs.get(1);
		this.title  = matchs.get(2);
		this.genre  = matchs.get(3);
		
		if (matchs.size() > 4) this.year = matchs.get(4);
		
	}
	
	

}
