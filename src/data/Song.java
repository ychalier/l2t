package data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import scrapper.YoutubeAPI;

public class Song {
	
	public static final String DOMAIN_FILTER = "youtube.com;youtu.be";
	
	private static final String PATTERN_TITLE_FULL    = "(.*) --? (.*) \\[(.*)\\] ?\\((.*)\\)";
	private static final String PATTERN_TITLE_NO_YEAR = "(.*) --? (.*) \\[(.*)\\]";
	
	private static final int WEIGHT_FAME = 1;
	private static final int WEIGHT_QUALITY = 10;
	
	// Id
	public String id;
	
	// Song tags
	public String artist;
	public String title;
	public String genra;
	public String year;
	
	public ArrayList<Genra> genras;
	
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
		
	public Song(JSONObject json) throws JSONException, IOException {
		id              = json.getString("id");
		ups             = json.getInt("ups");
		downs           = json.getInt("downs");
		nRedditComments = json.getInt("num_comments");
		domain          = json.getString("domain");
		url             = json.getString("url");
		thumbnail       = json.getString("thumbnail");
		
		parseTitle(json.getString("title"));
		parseGenra(genra);
		
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
	
	private void parseGenra(String genraString) {
		String[] split = genraString.toLowerCase().replace("?", "").split("/|,");
		genras = new ArrayList<Genra>();
		for(int i=0; i<split.length; i++) genras.add(new Genra(split[i]));
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
		
		parseGenra(genra);
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
	
	public String doubleStr(double d) {
		if(d < 0.01 && d > 0) return "0.0-";
		return Double.toString(d).substring(0, Math.min(4, Double.toString(d).length()));
	}
	
	public String toString() {
		return artist + " - " + title + " " + genras + " "
				+ "(" + doubleStr(fame) + "/" + doubleStr(quality) + ")";
	}
	
	public double meanScore() {
		return (WEIGHT_FAME*fame + WEIGHT_QUALITY*quality) / 2;
	}
	
	public static Comparator<Song> fameComparator() {
		return new Comparator<Song>() {

			@Override
			public int compare(Song arg0, Song arg1) {
				if (arg0.fame > arg1.fame) return -1;
				if (arg0.fame < arg1.fame) return  1;
				return 0;
			}
			
		};
	}
	
	public static Comparator<Song> qualityComparator() {
		return new Comparator<Song>() {

			@Override
			public int compare(Song arg0, Song arg1) {
				if (arg0.quality > arg1.quality) return -1;
				if (arg0.quality < arg1.quality) return  1;
				return 0;
			}
			
		};
	}
	
	public static Comparator<Song> meanComparator() {
		return new Comparator<Song>() {

			@Override
			public int compare(Song arg0, Song arg1) {
				if (arg0.meanScore() > arg1.meanScore()) return -1;
				if (arg0.meanScore() < arg1.meanScore()) return  1;
				return 0;
			}
			
		};
	}
		
}
