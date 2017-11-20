package scrapper;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import data.Song;
import data.SongException;
import tools.Regex;

/**
 * 
 * Provides tools to use YouTube data API.
 * As we're not accessing any sensible data,
 * or asking for very specific and complete data,
 * there is no need for using OAuth.
 * An API key is enough.
 * 
 * See official doc at
 * {@link https://developers.google.com/youtube/v3/docs/#Retrieve_video_entry}
 * 
 * @author Yohan Chalier
 *
 */
public class YouTubeAPI {
	
	private static final String API_KEY = "AIzaSyBiqiUxUCFWuMMVtAvfgWsgv5ezEv8WHIg";
	private static final String API_URL = "https://www.googleapis.com/youtube/v3/videos?part=statistics&id=VIDEO_ID&key=" + API_KEY;
	
	/**
	 * Returns the YouTube statistics of a song
	 * whose media is a YouTube video.
	 * @param song The song to retrieve info for
	 * @return The statistics of the song, with keys
	 * 		   viewCount
	 * 		   commentCount
	 * 		   likeCount
	 * 		   dislikeCount
	 * @throws SongException
	 */
	public static JSONObject getStatistics(Song song) throws SongException {
		
		// Parsing video id
		String videoId = getVideoId(song);
		if (videoId == null)
			throw new SongException("invalid video id: " + song.url);
		
		// Getting response from YouTube
		String response;
		try {
			response = new Connection(API_URL.replace("VIDEO_ID", videoId)).getResponse();
		} catch (IOException e1) {
			throw new SongException("unable to establish connection to youtube: " + song.url);
		}
		
		// Data trimming
		JSONObject json;
		try {
			json = new JSONObject(response);
		} catch (JSONException e) {
			throw new SongException("invalid response from YouTube api: " + song.url);
		}
		try {
			return json.getJSONArray("items").getJSONObject(0).getJSONObject("statistics");
		} catch (JSONException e) {
			throw new SongException("invalid YouTube json: " + song.url);
		}
	}
	
	/**
	 * Returns the id (11 chars) of a YouTube video
	 * @param song The song to retrieve info for
	 * @return The string of the id, or null if none found.
	 */
	public static String getVideoId(Song song) {
		return Regex.parse(Regex.PATTERN_VIDEO_ID, song.url, 2);
	}
}
