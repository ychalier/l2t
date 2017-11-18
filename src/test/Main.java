package test;

import java.util.HashMap;
import java.util.Map;

import score.*;
import data.*;
import tools.*;
import web.*;
import scrapper.*;

@SuppressWarnings("unused")
public class Main {

	public static void main(String[] args) throws Exception {
		
		
		RedditAPI api = new RedditAPI("O4_S_-j1vdVw8Q", 
				"http://start.byethost31.com/leethan2theece/");
		
		
		/*
		Library library = new Library(api.fetchData(500));
		JSONHandler.save(library.toJSON(), "library.json");
		*/
		
		Library library = new Library(JSONHandler.load("library.json"));
		library.computeScores();
		
		Model model = new Model(api, library);
		
		Router router = new Router();
		
		router.addView("^$", new View("web/base.html", model, new StaticEngine()));
		
		/*
		router.addView("^library\\/?$", 
				new View("web/index.html",
						model,
						new TemplateEngine() {

							@Override
							public String process(View view) {
								StringBuilder builder = new StringBuilder();
								for(Song song:view.getModel().getLibrary().getSongs())
									builder.append("<tr>"
											+ "<td>" + song.id + "</td>"
											+ "<td>" + song.artist + "</td>"
											+ "<td>" + song.title + "</td>"
											+ "<td>" + song.genra + "</td>"
											+ "<td>" + song.meanScore() + "</td>"
											+ "<td><a href='" + song.url + "'>" + song.domain + "</a></td>"
											+ "</tr>");
								
								return view.getTemplate().replace("PLACEHOLDER", builder.toString());
							}
			
						}
					)
				);
		*/
		
		router.addView("^search/([a-zA-Z0-9-]+)$", 
				new View("web/index.html",
						model,
						new TemplateEngine() {

							@Override
							public String process(View view) {
								String query = view.getHierarchy().get(1);
								StringBuilder builder = new StringBuilder();
								for(Song song:view.getModel().getSearchEngine().search(query))
									builder.append("<tr>"
											+ "<td>" + song.id + "</td>"
											+ "<td>" + song.artist + "</td>"
											+ "<td>" + song.title + "</td>"
											+ "<td>" + song.genra + "</td>"
											+ "<td>" + song.meanScore() + "</td>"
											+ "<td><a href='" + song.url + "'>" + song.domain + "</a></td>"
											+ "</tr>");
								
								return view.getTemplate().replace("PLACEHOLDER", builder.toString());
							}
			
						}
					)
				);
		
		Server server = new Server(8080, router);
		server.run();
		
	}
}
