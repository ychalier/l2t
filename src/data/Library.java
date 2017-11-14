package data;

import java.net.MalformedURLException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

public class Library {
	
	private ArrayList<Song> songs;
	
	public Library(JSONArray posts) throws MalformedURLException, JSONException {
		songs = new ArrayList<Song>();
		for(int i=0; i<posts.length(); i++) {
			Song song = new Song(posts.getJSONObject(i).getJSONObject("data"));
			if (song.artist != null)
				songs.add(song);
		}
	}
	
	public ArrayList<Song> getSongs(){
		return songs;
	}
	
	public String toString() {
		StringBuffer out = new StringBuffer();
		for(int i=0; i<songs.size(); i++) {
			int maxSize = Integer.toString(songs.size()).length();
			for(int k=0; k<maxSize-Integer.toString(i+1).length(); k++)
				out.append("0");
			out.append(Integer.toString(i+1) + ": " + songs.get(i).toString() + "\n");
		}
		return out.toString();
	}
	
}
