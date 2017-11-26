package controller;

import java.io.File;

import data.Library;
import scrapper.RedditAPI;
import tools.Config;
import tools.JSONHandler;
import tools.Logger;
import tools.Tools;

public final class LibraryThread extends Thread {
	
	private final ServerThread serverThread;
	
	private RedditAPI api;
	private Library   library;
	
	public LibraryThread(ServerThread serverThread) {
		this.serverThread = serverThread;
	}
	
	@Override
	public void run(){
		
		boolean browserOpened = false;
		
		try {
			api = new RedditAPI();
			
			// Retrieving token
			browserOpened = api.auth();
						
			// Waiting for the server answer
			
			if (browserOpened) {
				Logger.wrD("LIBRARY THREAD", "Waiting for token refreshing");
				serverThread.setAPI(api);
				synchronized (api.getAuthentifier().getLock()) {
					api.getAuthentifier().getLock().wait();
				}
				Logger.wrD("LIBRARY THREAD", "Successfully synchronized token");
			}
			
			// If the browser has not been opened, opens it
			if (!browserOpened)
				Tools.openBrowser("http://localhost:8080/wait");
			
			// Building library
			Logger.wrD("LIBRARY THREAD", "Building library");
			if (!(new File(Config.FILE_LIBRARY)).exists())
				library = new Library(api.fetchData(Config.FETCH_AMOUNT));
			else {
				library = new Library(new File(Config.FILE_LIBRARY));
				library.append(api.fetchData(Config.REFRESH_AMOUNT));
			}
			library.computeScores();
			JSONHandler.save(library.toJSON(), Config.FILE_LIBRARY);
			
			// Notify server thread
			serverThread.setLibrary(library);
			serverThread.setTimeout();
			
		} catch (Exception e) {
			Logger.wrE("LIBRARY THREAD", "Error: " + e.toString());
		}
	}

}
