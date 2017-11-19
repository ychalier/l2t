package scrapper;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import tools.JSONHandler;
import web.Model;
import web.Router;
import web.Server;
import web.TemplateEngine;
import web.View;

public class Authentifier {
	
	private static final String URL_CODE_RETRIEVAL        = "https://www.reddit.com/api/v1/authorize?client_id=CLIENT_ID&response_type=code&state=SEED&redirect_uri=REDIRECT_URI&duration=permanent&scope=read";
	private static final String URL_TOKEN_RETRIEVAL       = "https://www.reddit.com/api/v1/access_token";
	
	private static final String POST_PARAMETERS_RETRIEVAL = "grant_type=authorization_code&code=CODE&redirect_uri=REDIRECT_URI";
	private static final String POST_PARAMETERS_REFRESH   = "grant_type=refresh_token&refresh_token=TOKEN";
	
	private static final String FILE_TOKEN  = "token.json";
	
	private RedditAPI  api;
	
	private String     code;
	private String     seed;
	private JSONObject masterToken;
	private JSONObject secondToken;
	private Server     server;
	
	
	public Authentifier(RedditAPI api) {
		this.api  = api;
		this.code = null;
	}
	
	
	public void auth() throws Exception {
		if (!new File(FILE_TOKEN).exists())
			if (code == null) {
				retrieveCodeServerOut();
				return;
			}
			else
				retrieveToken();
		else 
			loadToken();
		refreshToken();
	}
	
	private String generateString(Random rng, String characters, int length){
	    char[] text = new char[length];
	    for (int i = 0; i < length; i++){
	        text[i] = characters.charAt(rng.nextInt(characters.length()));
	    }
	    return new String(text);
	}
	
	private URL getCodeRetrievalURL() throws MalformedURLException {
		seed = generateString(new Random(),
				"abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789", 8);
		return new URL(URL_CODE_RETRIEVAL
				.replace("CLIENT_ID", api.getClientId())
				.replace("REDIRECT_URI", api.getRedirectUri())
				.replace("SEED", seed));
	}
	
	private void retrieveCodeServerOut() throws Exception {
		URL url = getCodeRetrievalURL();
		System.out.println(url);
		
		Runtime.getRuntime().exec("xdg-open " + url.toString());
		
		Model model = new Model(null);
		model.setApi(this.api);
		Router router = new Router(model);
		router.addView("^authorize?$", new View("web/base.html",
				new TemplateEngine() {

					@Override
					public String process(View view) {
						
						String[] query = view.getQuery();
						String code = null;
						String seed = null;
						
						for(int i=0; i<query.length; i++) {
							String[] param = query[i].split("=");
							if (param.length == 2 && param[0].equals("code"))
								code = param[1];
							if (param.length == 2 && param[0].equals("state"))
								seed = param[1];
						}
						
						if (code != null && seed != null)
							try {
								view.getModel().getApi().getAuthentifier().retrieveCodeServerIn(code, seed);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						return "";
					}
			
		}));
		server = new Server(router);
		server.run(false, true);
	}
	
	public void retrieveCodeServerIn(String code, String seed) throws Exception {
		if (this.seed.equals(seed)) {
			this.code = code;
			auth();
		}
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
				.replace("CODE", code)
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
