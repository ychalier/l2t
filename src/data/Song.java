package data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

import scrapper.YouTubeAPI;
import tools.Regex;

public class Song {
	
	public static final String DOMAIN_FILTER = "youtube.com;youtu.be";
	
	private static final int WEIGHT_FAME    = 2;
	private static final int WEIGHT_QUALITY = 1;
	
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
		
		if (!DOMAIN_FILTER.contains(domain)) artist = null;
	}
		
	private void parseTitle(String title) {
		
		List<String> matchs;
		
		if ((matchs = Regex.parseAll(Regex.PATTERN_TITLE_FULL, title)) == null)
			matchs  = Regex.parseAll(Regex.PATTERN_TITLE_NO_YEAR, title);
		if (matchs == null) return;
		this.artist = matchs.get(1);
		this.title  = matchs.get(2);
		this.genra  = matchs.get(3);
		if (matchs.size() > 4) this.year = matchs.get(4);
		
	}
	
	private void parseGenra(String genraString) {
		if (genraString == null) return;
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
		return (WEIGHT_FAME * fame + WEIGHT_QUALITY * quality) / 2;
	}
	
	public static Comparator<Song> comparator() {
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
