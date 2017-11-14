package scrapper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import data.Song;

public class YoutubeAPI {
	
	public static final String URL_YOUTUBE_JSON = "https://gdata.youtube.com/feeds/api/videos/VIDEO_ID?v=2&alt=json";
	public static final String PATTERN_YOUTUBE_VIDEO_ID = "watch\\\\?v=(.{11})";
	
	private Song song;
	
	private String videoId;
	
	public YoutubeAPI(Song song) {
		this.song = song;
		Pattern pattern = Pattern.compile(PATTERN_YOUTUBE_VIDEO_ID);
		Matcher matcher = pattern.matcher(this.song.url.toString());
		if (matcher.find())
			videoId = matcher.group(1);
	}

}
