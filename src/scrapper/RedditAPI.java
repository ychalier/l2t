package scrapper;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 
 * Implements the tools required to work with Reddit.
 * Doc at {@link https://www.reddit.com/dev/api}
 * 
 * @author Yohan Chalier
 *
 */
public class RedditAPI {
	
	// Default Reddit application parameters
	public  static final String DEFAULT_CLIENT_ID    = "O4_S_-j1vdVw8Q";
	public  static final String DEFAULT_REDIRECT_URI = "http://localhost:PORT/authorize";
	
	// Default amount of posts fetched for a new library
	public  static final int    DEFAULT_FETCH_AMOUNT = 900;

	private static final String URL_JSON  = "https://oauth.reddit.com/r/SUBREDDIT/new.json?limit=LIMIT&after=AFTER";
	private static final int    MAX_LIMIT = 100;
	
	private String       client_id;
	private String       redirect_uri;
	private Authentifier authentifier;
	
	/**
	 * @param client_id The client id of the Reddit app
	 * @param redirect_uri The redirect URI set for the Reddit app
	 * @throws IOException
	 * @throws JSONException
	 */
	public RedditAPI(String client_id, String redirect_uri) 
			throws IOException, JSONException {
		this.client_id = client_id;
		this.redirect_uri = redirect_uri;
		authentifier = new Authentifier(this);
	}
	
	public String getClientId() {
		return client_id;
	}
	
	public String getRedirectUri() {
		return redirect_uri;
	}
	
	public Authentifier getAuthentifier() {
		return authentifier;
	}
	
	/**
	 * Start the authentication process
	 * @throws Exception
	 * @see Authentifier
	 */
	public void auth() throws Exception {
		this.authentifier.auth();
	}
	
	/**
	 * Concatenate multiple JSONArrays into one.
	 * @param arrs Arrays to be concatenated
	 * @return The array of the union
	 * @throws JSONException
	 */
	private JSONArray concatArray(JSONArray... arrs) 
			throws JSONException {
	    JSONArray result = new JSONArray();
	    
	    //Adding objects one by one
	    for (JSONArray arr : arrs)
	    	if (arr != null)
	    		for (int i = 0; i < arr.length(); i++)
		            result.put(arr.get(i));
	    
	    return result;
	}
	
	/**
	 * Establish a connection to a certain subreddit
	 * using the Reddit API, and returns the response
	 * given by the API.
	 * 
	 * @param sub The subreddit to connect to
	 * @param limit The maximum posts to show (see Reddit API doc)
	 * @param after The id of the post used as pivot
	 * @return The JSON returned by the Reddit API
	 * @throws IOException
	 * @throws JSONException
	 */
	private JSONObject fetchJSON(String sub, int limit, String after) 
			throws IOException, JSONException {
		
		// The first fetch has after=null
		if (after == null)
			after = "null";
		
		// Creating connection
		Connection c = new Connection(URL_JSON
				.replace("SUBREDDIT", sub)
				.replace("LIMIT", Integer.toString(limit))
				.replace("AFTER", after));
		
		// Adding token to be authorized by the API
		c.initBasicAuthorization("bearer " + authentifier.getToken());
		
		return new JSONObject(c.getResponse());
	}
	
	/**
	 * Compute the number of subreddit pages to fetch,
	 * use fetchJSON to collect them,
	 * and merge all results. 
	 * 
	 * @param amount Number of posts to fetch
	 * @return A JSONArray where each object represents a post
	 * @throws IOException
	 * @throws JSONException
	 */
	public JSONArray fetchData(int amount) 
			throws IOException, JSONException {
		
		// See Reddit API doc: at first after is null
		String after = null;
		
		// Number of pages required to fetch [amount] posts
		int nIterations = amount / MAX_LIMIT + 1;
		
		// Each page posts will be stored in an array
		// All those array will then be concatenate
		JSONArray[] posts = new JSONArray[nIterations];
		
		System.out.print("Fetching Reddit posts...");
		
		for (int i = 0; i < nIterations; i++) {
			// Wether we want all posts from the page or not
			int limit = MAX_LIMIT;
			if (amount - i*MAX_LIMIT < MAX_LIMIT)
				limit = amount - i*MAX_LIMIT;
			
			// Getting response from API
			JSONObject container = fetchJSON("listentothis", limit, after)
					.getJSONObject("data");
			posts[i] = container.getJSONArray("children");
			
			// After can be null or not exists
			// if there's no more post to fetch
			try {
				after = container.getString("after");
			} catch (org.json.JSONException e) {
				break;
			}
		}
		System.out.println(" Done.");
		
		// Finally concatenate all sub-arrays
		return concatArray(posts);
	}
	
}
