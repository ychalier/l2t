

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;

import data.*;
import tools.*;
import web.*;
import scrapper.*;

@SuppressWarnings("unused")
public class Main {

	public static void main(String[] args) throws Exception {
				
		Library library = Library.loadLibrary();
		Model   model   = new Model(library);
		Router  router  = new Router(model);
		
		// Landing page
		router.addView("^$", new View("web/base.html", new StaticEngine()));
		
		// Player page
		router.addView("^search/([a-zA-Z0-9-]+)$", 
				new View("web/playlist.html",
						new TemplateEngine() {

							@Override
							public String process(View view) {
								String query = view.getHierarchy().get(1);
								StringBuilder builder = new StringBuilder();
								
								// Format: [["YouTube ID", "Artist", "Title"], ...];								
								for(Song song:view.getModel().getSearchEngine().search(query))
									builder.append("["
											+ "\"" + YouTubeAPI.getVideoId(song) + "\", "
											+ "\"" + song.artist + "\", "
											+ "\"" + song.title + "\""
										    + "],");
								
								// Removing last comma
								builder.setCharAt(builder.length()-1, '\n');
								
								return view.getTemplate().replace("PLAYLIST_DATA", builder.toString());
							}
			
						}
					)
				);
		
		Server server = new Server(router);
		server.run(true, false);
		
	}
}
