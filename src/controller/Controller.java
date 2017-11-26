package controller;

import tools.Logger;

public class Controller {
	
	private ServerThread  serverThread;
	private LibraryThread libraryThread;

	public Controller() {};
	
	public LibraryThread getLibraryThread() {
		return libraryThread;
	}
	
	public ServerThread getServerThread() {
		return serverThread;
	}
	
	public void init() throws Exception {
		Logger.wrD("CONTROLLER", "Controller initialization started.");
		serverThread  = new ServerThread();
		libraryThread = new LibraryThread(serverThread);
		Logger.wrD("CONTROLLER", "Controller initialization is over");
	}
	
	public void start() {
		Logger.wrD("CONTROLLER", "Controller starting");
		serverThread.start();
		libraryThread.start();
	}
	
}
