package scrapper;

import java.io.IOException;

import org.json.JSONObject;

import data.Song;
import data.SongException;
import tools.Regex;

/**
 * 
 * Implements tools to retrieve info from Soundcloud.
 * Actually not a API from Soundcloud itself.
 * 
 * @author Yohan Chalier
 *
 */
public class SoundcloudAPI {
	
	public static JSONObject getStatistics(Song song) throws SongException{
		
		String html;
		try {
			html = new Connection(song.url).getResponse();
		} catch (IOException e) {
			throw new SongException("unable to establish connection to soundcloud: " + song.url);
		}
		
		JSONObject json = new JSONObject();
		
		try {
			json.put("viewCount", Integer.parseInt(
					Regex.parse(
							Regex.PATTERN_SOUNDCLOUD.replace("PARAM", "play"), 
							html)));
			json.put("commentCount", Integer.parseInt(
					Regex.parse(
							Regex.PATTERN_SOUNDCLOUD.replace("PARAM", "comments"), 
							html)));
			json.put("likeCount",
					Integer.parseInt(
							Regex.parse(
									Regex.PATTERN_SOUNDCLOUD.replace("PARAM", "like"),
									html))
					+ 
					Integer.parseInt(
							Regex.parse(
									Regex.PATTERN_SOUNDCLOUD.replace("PARAM", "download"), 
									html))
					);
			// No need for dislikeCount, as it is initialized with 0
		} catch (Exception e) {
			throw new SongException("invalid Soundcloud json: " + song.url);
		}
		
		return json;
	}
	
}
