package controller;

import tools.Logger;

/**
 * The main controller, that creates the two child threads
 * and starts them.
 * 
 * @author Yohan Chalier
 *
 */
public class Controller {
	
	/**
	 * A thread to host the local HTTP server
	 */
	private ServerThread  serverThread;
	
	/**
	 * A thread to handle the library creation
	 */
	private LibraryThread libraryThread;
	
	/**
	 * Creates the threads
	 */
	public void init() {
		Logger.wrD("CONTROLLER", "Controller initialization started.");
		serverThread  = new ServerThread();
		libraryThread = new LibraryThread(serverThread);
		Logger.wrD("CONTROLLER", "Controller initialization is over");
	}
	
	/**
	 * Starts the threads
	 */
	public void start() {
		Logger.wrD("CONTROLLER", "Controller starting");
		serverThread.start();
		libraryThread.start();
	}
	
}
