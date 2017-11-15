package scrapper;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.Scanner;

import org.json.JSONException;
import org.json.JSONObject;

import data.JSONHandler;

public class Authentifier {
	
	private static final String URL_CODE_RETRIEVAL        = "https://www.reddit.com/api/v1/authorize?client_id=CLIENT_ID&response_type=code&state=SEED&redirect_uri=REDIRECT_URI&duration=permanent&scope=read";
	private static final String URL_TOKEN_RETRIEVAL       = "https://www.reddit.com/api/v1/access_token";
	
	private static final String POST_PARAMETERS_RETRIEVAL = "grant_type=authorization_code&code=CODE&redirect_uri=REDIRECT_URI";
	private static final String POST_PARAMETERS_REFRESH   = "grant_type=refresh_token&refresh_token=TOKEN";
	
	private static final String FILE_TOKEN  = "token.json";
	
	private RedditAPI  api;
	
	private String     code;
	private JSONObject masterToken;
	private JSONObject secondToken;
	
	public Authentifier(RedditAPI api) throws IOException, JSONException {
		this.api = api;
		if (!new File(FILE_TOKEN).exists())
			retrieveToken();
		else
			loadToken();
		refreshToken();
	}
		
	private String retrieveCode() throws MalformedURLException {
		String seed = "a";
		URL url = new URL(URL_CODE_RETRIEVAL
				.replace("CLIENT_ID", api.getClientId())
				.replace("REDIRECT_URI", api.getRedirectUri())
				.replace("SEED", seed));
		System.out.println(url);
		System.out.print("Enter the code (27 chars)> ");
		Scanner scanner = new Scanner(System.in);
		code = scanner.nextLine();
		scanner.close();
		return code;
	}
	
	private String getAuthHeader() {
		String auth = api.getClientId() + ":";
		byte[] encodedAuth = Base64.getEncoder().encode(
				auth.getBytes(Charset.forName("US-ASCII"))
				);
		return "Basic " + new String(encodedAuth);
	}
	
	private String getAuthParametersRetrieval() throws MalformedURLException {
		return POST_PARAMETERS_RETRIEVAL
				.replace("CODE", retrieveCode())
				.replace("REDIRECT_URI", api.getRedirectUri());
	}
	
	private String getAuthParametersRefresh() 
			throws MalformedURLException, JSONException {
		return POST_PARAMETERS_REFRESH
				.replace("TOKEN", masterToken.getString("refresh_token"));
	}
	
	private void saveToken() throws IOException, JSONException {
		if (masterToken != null) JSONHandler.save(masterToken, FILE_TOKEN);
	}
	
	private JSONObject getTokenRetrievalResponse(String parameters)
			throws IOException, JSONException {
		Connection c = new Connection(URL_TOKEN_RETRIEVAL);
		c.initBasicAuthorization(getAuthHeader());
		c.initPOSTRequest(parameters);
		String response = c.getResponse();
		return new JSONObject(response);
	}
	
	private void retrieveToken() throws IOException, JSONException {
		masterToken = getTokenRetrievalResponse(getAuthParametersRetrieval());
		saveToken();
	}
	
	private void refreshToken() throws IOException, JSONException {
		System.out.print("Refreshing token... ");
		secondToken = getTokenRetrievalResponse(getAuthParametersRefresh());
		System.out.println("Done.");
	}
	
	private void loadToken() throws IOException, JSONException {
		masterToken = JSONHandler.load(FILE_TOKEN);
	}
	
	public String getToken() throws JSONException {
		return secondToken.getString("access_token");
	}

}
