package scrapper;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import tools.Config;

/***
 * 
 * Implements tools to send and received HTTP requests.
 * 
 * @author Yohan Chalier
 *
 */
public class Connection {
		
	private URL url;
	private HttpsURLConnection con;
	
	/**
	 * Creates a new connection to an URL.
	 * 
	 * @param urlString The URL to connect to.
	 * @throws IOException
	 */
	public Connection(String urlString) throws IOException {
		url = new URL(urlString.replace("http://", "https://"));
		con = (HttpsURLConnection) url.openConnection();
		con.setRequestProperty("User-Agent", Config.USER_AGENT);
	}
	
	/**
	 * Initializes a POST request
	 * 
	 * @param parameters POST parameters to add to the request
	 * @throws IOException
	 */
	public void initPOSTRequest(String parameters) 
			throws IOException {
		con.setRequestMethod("POST");
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(
				con.getOutputStream());
		wr.writeBytes(parameters);
		wr.flush();
		wr.close();
	}
	
	/**
	 * Adds a basic authentication to the HTTP request
	 * 
	 * @param value The credentials to use for authentication
	 */
	public void initBasicAuthorization(String value) {
		con.setRequestProperty("Authorization", value);
	}
	
	/**
	 * Sends the requests and returns the response.
	 * 
	 * @return The response from the HTTP request
	 * @throws IOException
	 */
	public String getResponse() throws IOException {
		// Error 401 might occur because of a credentials error
		// This methods also sends the request
		if (con.getResponseCode() == 401)
			return null;
		
		// Receives the response
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		
		return response.toString();
	}
	
}
