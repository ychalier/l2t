package web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Server  {
		
	private Map<String, View> router;
	private ServerSocket server;
	
	public Server(int port) throws IOException {
		this.server = new ServerSocket(port);
	}
	
	public void setRouter(Map<String, View> router) {
		this.router = router;
	}

	public void run() throws Exception {
	    System.out.println("Listening for connection on port 8080 ....");
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
	
	private String applyPattern(String patternString, String target) {
		Pattern pattern = Pattern.compile(patternString);
		Matcher matcher = pattern.matcher(target);
		if(matcher.find()) return matcher.group(1);
		return null;
	}
	
	private String getResponse(String request) throws IOException {
		
		String route = applyPattern("GET ([a-zA-Z0-9-\\/]+)", request);
		
		if (router.containsKey(route))
			return router.get(route).getResponse(request);
		return "404 Page not found";
	}

}
