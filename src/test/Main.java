package test;

import java.util.HashMap;
import java.util.Map;

import web.*;

public class Main {

	public static void main(String[] args) throws Exception {
		
		/*
		RedditAPI api = new RedditAPI("O4_S_-j1vdVw8Q", 
				"http://start.byethost31.com/leethan2theece/");
		
		Library library = new Library(api.fetchData(500));
		JSONHandler.save(library.toJSON(), "library.json");
		*/
		
		/*
		Library library = new Library(JSONHandler.load("library.json"));
		library.computeScores();
		library.sortByFame();
		System.out.println(library);
		*/
		
		Server server = new Server(8080);
		Map<String, View> router = new HashMap<String, View>();
		router.put("/", new View("web/index.html"));
		router.put("/index", new View("web/index.html"));
		server.setRouter(router);
		server.run();
		
	}
}
