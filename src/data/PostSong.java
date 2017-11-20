package data;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import scrapper.SoundcloudAPI;
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
	 * @throws SongException 
	 */
	public PostSong(JSONObject json) throws SongException {
		
		super();
		
		String tmpTitle;
		try {
			id              = json.getString("id");
			ups             = json.getInt("ups");
			downs           = json.getInt("downs");
			nRedditComments = json.getInt("num_comments");
			domain          = json.getString("domain");
			url             = json.getString("url");
			thumbnail       = json.getString("thumbnail");
			tmpTitle        = json.getString("title");
		} catch (JSONException e) {
			throw new SongException("invalid reddit json");
		}
		
		// Domain filter
		if (!DOMAIN_FILTER.contains(domain))
			throw new SongException("domain unsupported: " + domain);
		
		// Extract artist, title, genre and year
		parseTitle(tmpTitle);
		
		// Interpret the genre string found earlier
		parseGenre(genre);
		
		// Fetching statistics
		JSONObject statistics = null;
		if (domain.equals("youtube.com") || domain.equals("youtu.be")) {
			statistics = YouTubeAPI.getStatistics(this);
		} else if (domain.equals("soundcloud.com")){
			statistics = SoundcloudAPI.getStatistics(this);
		}
		
		// Setting statistics values
		if(statistics != null) {
			try {
				// Some data might not be available.
				// But we do not want to toss the song either,
				// thus by default, values are at 0.
				views        = 0;
				nExtComments = 0;
				likes        = 0;
				dislikes     = 0;
				if (statistics.has("viewCount"))
					views = statistics.getInt("viewCount");
				if (statistics.has("commentCount"))
					nExtComments = statistics.getInt("commentCount");
				if (statistics.has("likeCount"))
					likes = statistics.getInt("likeCount");
				if (statistics.has("dislikeCount"))
					dislikes = statistics.getInt("dislikeCount");
			} catch (JSONException e) {
				e.printStackTrace();
				throw new SongException("invalid statistics json: " + url);
			}
		} else {
			throw new SongException("no statistics found: " + url);
		}
		
	}
	
	/**
	 * Interpret post title to extract the artist,
	 * the title, the genre(s) and the year.
	 * Year is optional (null if not found).
	 * 
	 * @param title Reddit post title (given by OP)
	 * @throws SongException 
	 */
	private void parseTitle(String title) throws SongException {
		
		if (title == null)
			throw new SongException("no title found");
		
		List<String> matchs;
		
		// Parsing through a regular expression
		matchs = Regex.parseAll(Regex.PATTERN_TITLE, title);
		if (matchs == null)
			throw new SongException("invalid title: " + title);
		
		this.artist = matchs.get(1);
		this.title  = matchs.get(3);
		this.genre  = matchs.get(4);
		
	}
	
	/**
	 * Split all genres provided by the OP, and then
	 * process them through a basic IE engine.
	 * 
	 * @param genreString Song genre with the following format:
	 * 					"genre 1/genre 2" or "genre 1, genre 2"
	 * @throws SongException 
	 * @see Genre
	 */
	private void parseGenre(String genreString) throws SongException {
		
		if (genreString == null)
			throw new SongException("genre string is null");
		
		String[] split = genreString.toLowerCase()
				.replace("?", "")
				.split("/|,");
		genres = new ArrayList<Genre>();
		for(int i=0; i<split.length; i++)
			genres.add(new Genre(split[i]));
	}
	
	

}
