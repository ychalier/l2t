package scrapper;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import data.Song;

public class YoutubeAPI {
	
	/*
	 * DOCS
	 * https://developers.google.com/youtube/v3/docs/#Retrieve_video_entry
	 */
	
	private static final String PATTERN_VIDEO_ID_COM = "watch\\?v=(.{11})";
	private static final String PATTERN_VIDEO_ID_BE  = "youtu.be/(.{11})";
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
		if(song.domain.equals("youtube.com")) videoId = tryPattern(PATTERN_VIDEO_ID_COM, song.url);
		if(song.domain.equals("youtu.be"))    videoId = tryPattern(PATTERN_VIDEO_ID_BE,  song.url);
		if (videoId == null) return null;
		String response = new Connection(API_URL.replace("VIDEO_ID", videoId)).getResponse();
		if (response == null) return null;
		JSONObject json = new JSONObject(response);
		if (json.has("items")) {
			JSONArray array = json.getJSONArray("items");
			if (array.length() > 0 && array.getJSONObject(0).has("statistics")) {
				return array.getJSONObject(0).getJSONObject("statistics");
			}
		}
		return null;
	}
}
