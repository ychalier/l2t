package web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

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
	 * 
	 * @param openBrowser Should the browser be open with a local URL at start
	 * @param oneTimeServer Should the server close after receiving a request
	 * @throws Exception
	 */
	public void run() 
			throws Exception {
		
	    Logger.wrI("SERVER", "Listening for connection on port " + Config.PORT + " ...");
	    
	    server.setSoTimeout(Config.SOCKET_TIMEOUT);
	    
	    while (true){
	    	// Reading incoming requests
	    	try {
	    		Socket clientSocket = server.accept();
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
	    	}
	    }
	    this.server.close();
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
