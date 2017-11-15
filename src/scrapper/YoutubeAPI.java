package scrapper;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import data.Song;

public class YoutubeAPI {
	
	/*
	 * DOCS
	 * https://developers.google.com/youtube/v3/docs/#Retrieve_video_entry
	 */
	
	private static final String PATTERN_COM_VIDEO_ID = "watch\\?v=(.{11})";
	private static final String PATTERN_BE_VIDEO_ID = "youtu.be/(.{11})";
	private static final String API_KEY = "AIzaSyBiqiUxUCFWuMMVtAvfgWsgv5ezEv8WHIg";
	private static final String API_URL = "https://www.googleapis.com/youtube/v3/videos?part=statistics&id=VIDEO_ID&key=" + API_KEY;
	
	private static String tryPattern(String patternString, String target) {
		Pattern pattern = Pattern.compile(patternString);
		Matcher matcher = pattern.matcher(target);
		if (matcher.find()) return matcher.group(1);
		return null;			
	}
	
	public static JSONObject getStatistics(Song song) throws JSONException, IOException {			
		String videoId = null;
		if(song.domain.equals("youtube.com")) videoId = tryPattern(PATTERN_COM_VIDEO_ID, song.url.toString());
		if(song.domain.equals("youtu.be")) videoId = tryPattern(PATTERN_BE_VIDEO_ID, song.url.toString());
		if (videoId == null) return null;
		return new JSONObject(
				new Connection(API_URL.replace("VIDEO_ID", videoId))
				.getResponse())
				.getJSONArray("items").getJSONObject(0).getJSONObject("statistics");
	}
}
