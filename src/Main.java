import data.Library;
import data.Song;
import scrapper.YouTubeAPI;
import web.Model;
import web.Router;
import web.Server;
import web.StaticEngine;
import web.TemplateEngine;
import web.View;

public class Main {

	public static void main(String[] args) throws Exception {
				
		Library library = Library.loadLibrary();
		Model   model   = new Model(library);
		Router  router  = new Router(model);
		
		// Landing page
		router.addView("^$",
				new View(Server.TEMPLATES_DIR + "base.html",
						new StaticEngine()));
		
		// Player page
		router.addView("^search/([a-zA-Z0-9-]+)$", 
				new View(Server.TEMPLATES_DIR + "playlist.html",
						new TemplateEngine() {

							@Override
							public String process(View view) {
								String query = view.getHierarchy().get(1);
								StringBuilder builder = new StringBuilder();
								
								// Format: [["YouTube ID", "Artist", "Title"], ...];
								// Replacing char " (ASCII code 34) or it causes troubles in JS
								for(Song song:view.getModel().getSearchEngine().search(query))
									builder.append("["
											+ "\"" + YouTubeAPI.getVideoId(song) + "\", "
											+ "\"" + song.artist.replace((char) 34, '\'') + "\", "
											+ "\"" + song.title.replace((char) 34, '\'') + "\""
										    + "],");
								
								// Removing last comma
								builder.setCharAt(builder.length()-1, ' ');
								
								// Building response and replacing LF (ASCII code 10) with '\n',
								// so the output file looks all right.
								String response = view.getTemplate()
										.replace("PLAYLIST_DATA", builder.toString())
										.replace((char) 10, '\n');
								
								return response;
							}
			
						}
					)
				);
		
		Server server = new Server(router);
		server.run(true, false);
		
	}
}
