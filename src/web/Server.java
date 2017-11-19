package web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server  {
	
	public static final String STATIC_DIR   = "web/";
	public static final String STATIC_FILES = "(.css|.js|.ico)";
	
	public static final int PORT = 8080;
	
	private Router router;
	private ServerSocket server;
	
	public Server(Router router) throws IOException {
		this.router = router;
		this.server = new ServerSocket(PORT);
	}

	public void run(boolean openBrowser, boolean closeOnRequest) throws Exception {
		
		if (openBrowser)
			Runtime.getRuntime().exec("xdg-open http://localhost:" + PORT + "/");
				
	    System.out.println("Listening for connection on port " + PORT + " ...");
	    while (true){
	    	Socket clientSocket = server.accept();
	    	BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			String request = reader.readLine();			
			System.out.println(request);
			String httpResponse = "HTTP/1.1 200 OK\r\n\r\n" + getResponse(request);
			clientSocket.getOutputStream().write(httpResponse.getBytes("UTF-8"));
			clientSocket.close();
			if (closeOnRequest)
				break;
	    }
	    this.server.close();
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
