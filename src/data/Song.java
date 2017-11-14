package data;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

public class Song {
	
	public static final String PATTERN_TITLE_FULL    = "(.*) --? (.*) \\[(.*)\\] ?\\((.*)\\)";
	public static final String PATTERN_TITLE_NO_YEAR = "(.*) --? (.*) \\[(.*)\\]";
	
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
	public URL url;
	public URL thumbnail;
		
	public Song(JSONObject json) throws JSONException {
		id              = json.getString("id");
		ups             = json.getInt("ups");
		downs           = json.getInt("downs");
		nRedditComments = json.getInt("num_comments");
		domain          = json.getString("domain");
		try {
			url         = new URL(json.getString("url"));
			thumbnail   = new URL(json.getString("thumbnail"));
		} catch (MalformedURLException e) {
			// e.printStackTrace();
		}
		parseTitle(json.getString("title"));
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
	
	public String toString() {
		return artist + " - " + title + " [" + genra + "]";
	}
	
}
