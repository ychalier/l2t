package scrapper;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RedditAPI {
	
	public static final String URL_JSON = "https://oauth.reddit.com/r/SUBREDDIT/new.json?limit=LIMIT&after=AFTER";
	public static final int MAX_LIMIT = 100;
	
	private String client_id;
	private String client_secret;
	private String redirect_uri;
	
	private Authentifier authentifier;
	
	public RedditAPI(String client_id, String client_secret, String redirect_uri) 
			throws IOException, JSONException {
		this.client_id = client_id;
		this.client_secret = client_secret;
		this.redirect_uri = redirect_uri;
		authentifier = new Authentifier(this);
	}
	
	public String getClientId() {
		return client_id;
	}
	
	public String getClientSecret() {
		return client_secret;
	}
	
	public String getRedirectUri() {
		return redirect_uri;
	}
	
	public Authentifier getAuthentifier() {
		return authentifier;
	}
	
	private JSONArray concatArray(JSONArray... arrs) throws JSONException {
	    JSONArray result = new JSONArray();
	    for (JSONArray arr : arrs) {
	        for (int i = 0; i < arr.length(); i++) {
	            result.put(arr.get(i));
	        }
	    }
	    return result;
	}
	
	private JSONObject fetchJSON(String subreddit, int limit, String after) 
			throws IOException, JSONException {
		if (after == null)
			after = "null";
		Connection c = new Connection(URL_JSON
				.replace("SUBREDDIT", subreddit)
				.replace("LIMIT", Integer.toString(limit))
				.replace("AFTER", after));
		c.initBasicAuthorization("bearer " + authentifier.getToken());
		return new JSONObject(c.getResponse());
	}
	
	public JSONArray fetchData(int amount) throws IOException, JSONException {
				
		String after = null;		
		int nIterations = amount / MAX_LIMIT + 1;
		JSONArray[] posts = new JSONArray[nIterations];
		
		for(int i=0; i<nIterations; i++) {
			int limit = MAX_LIMIT;
			if (amount - i*MAX_LIMIT < MAX_LIMIT)
				limit = amount - i*MAX_LIMIT;
			JSONObject container = fetchJSON("listentothis", limit, after)
					.getJSONObject("data");
			after = container.getString("after");
			posts[i] = container.getJSONArray("children");
		}
		
		return concatArray(posts);
	}
	
}