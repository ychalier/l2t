package data;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import scrapper.YoutubeAPI;

public class Song {
	
	public static final String DOMAIN_FILTER = "youtube.com;youtu.be";
	
	private static final String PATTERN_TITLE_FULL    = "(.*) --? (.*) \\[(.*)\\] ?\\((.*)\\)";
	private static final String PATTERN_TITLE_NO_YEAR = "(.*) --? (.*) \\[(.*)\\]";
	
	// Id
	public String id;
	
	// Song tags
	public String artist;
	public String title;
	public String genra;
	public String year;
	
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
		
	public Song(JSONObject json) throws JSONException, IOException {
		id              = json.getString("id");
		ups             = json.getInt("ups");
		downs           = json.getInt("downs");
		nRedditComments = json.getInt("num_comments");
		domain          = json.getString("domain");
		url             = json.getString("url");
		thumbnail       = json.getString("thumbnail");
		
		parseTitle(json.getString("title"));
		
		if (domain.equals("youtube.com") || domain.equals("youtu.be")) {
			JSONObject statistics = YoutubeAPI.getStatistics(this);
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
		
		if (!DOMAIN_FILTER.contains(domain)) artist = null;
	}
		
	private void parseTitle(String title) {
		Pattern pattern = Pattern.compile(PATTERN_TITLE_FULL);
		Matcher matcher = pattern.matcher(title);
		if (matcher.find()) {
			this.artist = matcher.group(1);
			this.title  = matcher.group(2);
			this.genra  = matcher.group(3);
			this.year   = matcher.group(4);
		} else {
			pattern = Pattern.compile(PATTERN_TITLE_NO_YEAR);
			matcher = pattern.matcher(title);
			if (matcher.find()) {
				this.artist = matcher.group(1);
				this.title  = matcher.group(2);
				this.genra  = matcher.group(3);
			}
		}
	}
	
	public Song(JSONObject json, int foo) throws JSONException {
		id              = json.getString("id");
		artist          = json.getString("artist");
		title           = json.getString("title");
		genra           = json.getString("genra");
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
		
		try {
			year        = json.getString("year");
		} catch (org.json.JSONException e) {
			year        = null;
		}
	}
	
	public JSONObject toJSON() throws JSONException {
		JSONObject json = new JSONObject();
		json.put("id", id);
		json.put("artist", artist);
		json.put("title", title);
		json.put("genra", genra);
		json.put("year", year);
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
		return json;
	}
	
	public String toString() {
		return artist + " - " + title + " [" + genra + "]";
	}
		
}
