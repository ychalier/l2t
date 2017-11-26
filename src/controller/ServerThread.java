package controller;

import java.io.IOException;

import data.Library;
import scrapper.RedditAPI;
import tools.Logger;
import web.DefaultRouter;
import web.Model;
import web.Router;
import web.Server;

public final class ServerThread extends Thread {
	
	private Server server;
	
	public ServerThread() {
		super();
	}
	
	@Override
	public void run() {
		
		Model model = new Model();
		Router router = null;
		try {
			router = new DefaultRouter(model);
		} catch (IOException e) {
			Logger.wrE("SERVER THREAD", "Error while parsing templates: " + e.toString());
		}
		
		try {
			server = new Server(router);
			server.run();
		} catch (Exception e) {
			Logger.wrE("SERVER THREAD", "Error: " + e.toString());
		}
		
	}
	
	public void setLibrary(Library library) {
		server.getModel().setLibrary(library);
	}
	
	public void setAPI(RedditAPI api) {
		server.getModel().setApi(api);
	}
	
	public void setTimeout() {
		server.setTimeout(true);
	}

}
