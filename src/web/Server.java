package web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import tools.Logger;
import tools.Tools;

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
	public static final String STATIC_DIR    = "/static/";
	// Static files types supported
	public static final String STATIC_FILES  = "(.css|.js|.ico)";
	// Static files should be referred to from the root ("/")
	// in a template file.
	
	// Default server port
	public static final int    PORT          = 8080;
	
	private Router router;
	private ServerSocket server;
	
	public Server(Router router) throws IOException {
		this.router = router;
		this.server = new ServerSocket(PORT);
	}

	/**
	 * Starts the server.
	 * 
	 * @param openBrowser Should the browser be open with a local URL at start
	 * @param closeOnRequest Should the server close after receiving a request
	 * @throws Exception
	 */
	public void run(boolean openBrowser, boolean closeOnRequest) throws Exception {
		
		if (openBrowser)
			Tools.openBrowser("http://localhost:" + PORT + "/");
				
	    System.out.println("Listening for connection on port " + PORT + " ...");
	    Logger.wr("Listening for connection on port " + PORT + " ...");
	    while (true){
	    	// Reading incoming requests
	    	Socket clientSocket = server.accept();
	    	BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			String request = reader.readLine();			
			System.out.println(request);
			Logger.wr(request);
			
			// Preparing response
			String httpResponse = "HTTP/1.1 200 OK\r\n\r\n" + getResponse(request);
			
			// Sending response
			clientSocket.getOutputStream().write(httpResponse.getBytes("UTF-8"));
			clientSocket.close();
			
			if (closeOnRequest)
				break;
	    }
	    this.server.close();
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
