package web;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;

public class Server  {
	
	public static final String STATIC_DIR   = "web/";
	public static final String STATIC_FILES = "(.css|.js|.ico)";
	
	private int port;
	private Router router;
	private ServerSocket server;
	
	
	public Server(int port, Router router) throws IOException {
		this.port   = port;
		this.server = new ServerSocket(port);
		this.router = router;
	}
	

	public void run() throws Exception {
		
		try {
			if (Desktop.isDesktopSupported())
				Desktop.getDesktop().browse(new URI("http://localhost:" + port + "/"));
		} catch (java.lang.UnsupportedOperationException e) {
			// e.printStackTrace();
		}
		
	    System.out.println("Listening for connection on port " + port + " ...");
	    while (true){
	    	Socket clientSocket = server.accept();
	    	BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			String request = reader.readLine();			
			System.out.println(request);
			String httpResponse = "HTTP/1.1 200 OK\r\n\r\n" + getResponse(request);
			clientSocket.getOutputStream().write(httpResponse.getBytes("UTF-8"));
			clientSocket.close();
	    }
	}
	
	
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
