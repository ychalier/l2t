package scrapper;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class Connection {
	
	private static final String USER_AGENT = "Mozilla/5.0";
	
	private URL url;
	private HttpsURLConnection con;
	
	public Connection(String urlString) throws IOException {
		url = new URL(urlString);
		con = (HttpsURLConnection) url.openConnection();
		con.setRequestProperty("User-Agent", USER_AGENT);
	}
	
	public void initPOSTRequest(String parameters) throws IOException {
		con.setRequestMethod("POST");
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(parameters);
		wr.flush();
		wr.close();
	}
	
	public void initBasicAuthorization(String value) {
		con.setRequestProperty("Authorization", value);
	}
	
	public String getResponse() throws IOException {
		if (con.getResponseCode() == 401)
			return null;
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
