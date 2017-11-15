package data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;

public class Library {
	
	private ArrayList<Song> songs;
	
	public Library(JSONArray posts) throws JSONException, IOException {
		songs = new ArrayList<Song>();
		System.out.print("Building library...");
		for(int i=0; i<posts.length(); i++) {
			Song song = new Song(posts.getJSONObject(i).getJSONObject("data"));
			if (song.artist != null)
				songs.add(song);
		}
		System.out.println(" Done.");
	}
	
	public ArrayList<Song> getSongs(){
		return songs;
	}
	
	public Map<String, Integer> getDomainRepartition() {
		Map<String, Integer> repartition = new HashMap<String, Integer>();
		for(Song song : songs) {
			if (!repartition.containsKey(song.domain))
				repartition.put(song.domain, 0);
			repartition.put(song.domain, repartition.get(song.domain) + 1);
		}
		return repartition;
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
