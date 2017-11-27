package web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import org.json.JSONException;

import tools.Config;
import tools.Logger;

/**
 * 
 * Implements a basic HTTP server with a small MVC.
 * 
 * @author Yohan Chalier
 *
 */
public class Server  {
	
	// The folder containing the templates for the views
	public static final String TEMPLATES_DIR = "/templates/";
	
	// The folder containing the static files
	public static final String STATIC_DIR    = "static/";
	// Static files types supported
	public static final String STATIC_FILES  = "(.css|.js)";
	// Static files should be referred to from the root ("/")
	// in a template file.
	
	private Router       router;
	private ServerSocket server;
	
	private boolean      timeout = false;
	
	public Server(Router router) throws IOException {
		this.router = router;
		this.server = new ServerSocket(Config.PORT);
	}
	
	
	public Router getRouter() {
		return router;
	}
	
	
	public Model getModel() {
		return router.getModel();
	}

	/**
	 * Starts the server.
	 * @throws IOException 
	 * @throws UnsupportedEncodingException 
	 * @throws JSONException 
	 * 
	 * @throws Exception
	 */
	public void run() {
		
	    Logger.wrI("SERVER", "Listening for connection on port " + Config.PORT + " ...");
	    
	    try {
			server.setSoTimeout(Config.SOCKET_TIMEOUT);
		} catch (SocketException e) {
			Logger.wrE("SERVER", "Error setting timeout: " + e.toString());
		}
	    
	    while (true){
	    	// Reading incoming requests
	    	try {
	    		Socket clientSocket;
				
				clientSocket = server.accept();
				BufferedReader reader = new BufferedReader(
		    			new InputStreamReader(clientSocket.getInputStream())
		    			);
				String request = reader.readLine();
				Logger.wrI("SERVER", request);
				
				// Preparing response
				String httpResponse = "HTTP/1.1 200 OK\r\n\r\n" 
									+ getResponse(request);
				
				// Sending response
				clientSocket.getOutputStream()
							.write(httpResponse.getBytes("UTF-8"));
				clientSocket.close();
				
	    	} catch (SocketTimeoutException e) {
	    		if (timeout) {
	    			Logger.wrI("SERVER", "Socket timeout, closing");
	    			break;
	    		}
	    	} catch (IOException e) {
	    		Logger.wrE("SERVER", "Error reading or writing request: " + e.toString());
	    	}
	    }
	    // Saving library for likes
	    getModel().getLibrary().save();
	    try {
			server.close();
		} catch (IOException e) {
			Logger.wrE("SERVER", "Error closing server: " + e.toString());
		}
	}
	
	public void setTimeout(boolean timeout) {
		this.timeout = timeout;
	}
	
	/**
	 * Uses the router to find a corresponding view.
	 * 
	 * @param request The GET request to handle
	 * @return The HTTP response
	 * @throws IOException
	 */
	private String getResponse(String request) throws IOException {
		
		View view = router.findView(request);
		if (view != null)
			return view.getResponse();
		
		return get404Response();
	}
	
	private String get404Response() {
		return "<!DOCTYPE html><html>Error 404: page not found.</html>";
	}

}
