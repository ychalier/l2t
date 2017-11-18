package data;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import score.Jury;

public class Library {
	
	private ArrayList<Song> songs;
	private Jury jury;
	
	
	public Library(JSONArray posts) throws JSONException, IOException {
		songs = new ArrayList<Song>();
		System.out.print("Building library...");
		for(int i=0; i<posts.length(); i++) {
			Song song = new Song(posts.getJSONObject(i).getJSONObject("data"));
			if (song.artist != null)
				songs.add(song);
		}
		jury = new Jury(this);
		System.out.println(" Done.");
	}
	
	
	public Library(JSONObject json) throws JSONException, MalformedURLException {
		songs = new ArrayList<Song>();
		JSONArray array = json.getJSONArray("list");
		for(int i=0; i<array.length(); i++) songs.add(new Song(array.getJSONObject(i), 0));
		jury = new Jury(this);
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
	
	
	public Map<String, Integer> getGenraRepartition() {
		Map<String, Integer> repartition = new HashMap<String, Integer>();
		for(Song song : songs) {
			if (!repartition.containsKey(song.genra))
				repartition.put(song.genra, 0);
			repartition.put(song.genra, repartition.get(song.genra) + 1);
		}
		return repartition;
	}
	
	
	public void computeScores() {
		jury.computeScores();
	}
	
	
	public void sort(){
		songs.sort(Song.comparator());
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
	
	
	public JSONObject toJSON() throws JSONException {
		JSONArray array = new JSONArray();
		for(Song song : songs) array.put(song.toJSON());
		JSONObject json = new JSONObject();
		json.put("list", array);
		return json;
	}
	
	
	public ArrayList<Song> search(String query){
		Genra genra = new Genra(query);
		ArrayList<Song> results = new ArrayList<Song>();
		for(Song song: songs) {
			for(Genra g: song.genras) {
				if(g.main.equals(genra.main)) {
					results.add(song);
					break;
				}
			}
		}
		results.sort(Song.comparator());
		return results;
	}
	
}
