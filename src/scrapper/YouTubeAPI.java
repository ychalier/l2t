package scrapper;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import data.Song;
import tools.Regex;

public class YouTubeAPI {
	
	/*
	 * DOCS
	 * https://developers.google.com/youtube/v3/docs/#Retrieve_video_entry
	 */
	
	private static final String API_KEY = "AIzaSyBiqiUxUCFWuMMVtAvfgWsgv5ezEv8WHIg";
	private static final String API_URL = "https://www.googleapis.com/youtube/v3/videos?part=statistics&id=VIDEO_ID&key=" + API_KEY;
	
	public static JSONObject getStatistics(Song song) throws JSONException, IOException {
		
		// Parsing video id
		String videoId = getVideoId(song);
		if (videoId == null) return null;
		
		// Getting response from YouTube
		String response = new Connection(API_URL.replace("VIDEO_ID", videoId)).getResponse();
		if (response == null) return null;
		
		// Data trimming
		JSONObject json = new JSONObject(response);
		if (json.has("items")) {
			JSONArray array = json.getJSONArray("items");
			if (array.length() > 0 && array.getJSONObject(0).has("statistics")) {
				return array.getJSONObject(0).getJSONObject("statistics");
			}
		}
		return null;
	}
	
	public static String getVideoId(Song song) {
		String videoId = null;
		if(song.domain.equals("youtube.com")) videoId = Regex.parse(Regex.PATTERN_VIDEO_ID_COM, song.url);
		if(song.domain.equals("youtu.be"))    videoId = Regex.parse(Regex.PATTERN_VIDEO_ID_BE,  song.url);
		return videoId;
	}
}
