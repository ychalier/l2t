import java.util.List;

import data.Library;
import data.Song;
import scrapper.YouTubeAPI;
import tools.Logger;
import web.Model;
import web.Router;
import web.Server;
import web.StaticEngine;
import web.TemplateEngine;
import web.View;

/**
 * 
 * arguments
 * -l  --log  Activate the logger (into file .log)
 * 
 * @author Yohan Chalier
 *
 */
public class Main {

	public static void main(String[] args) throws Exception {
		
		// Reading arguments
		boolean log = false;
		for (int i = 0; i < args.length; i++)
			if (args[i].equals("-l") || args[i].equals("--log"))
				log = true;
		
		// Initialize logger
		new Logger(log);
				
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
								List<Song> songs = view
										.getModel()
										.getSearchEngine()
										.search(query);
								
								StringBuilder builder = new StringBuilder();

								// Format: [["YouTube ID", "Artist", "Title"], ...];
								// Replacing char " (ASCII code 34) or it causes troubles in JS
								for(Song song: songs){
									
									if (song.domain.equals("youtube.com") 
											|| song.domain.equals("youtu.be"))
										builder.append("[ \"yt\", "
												+ "\"" + YouTubeAPI.getVideoId(song) + "\", ");
									else if (song.domain.equals("soundcloud.com"))
										builder.append("[ \"sc\", "
												+ "\"" + song.url + "\", ");
								
									builder.append(
											  "\"" + song.artist.replace((char) 34, '\'') + "\", "
											+ "\"" + song.title.replace((char) 34, '\'') + "\""
										    + "],");
								}
								
								// Removing last comma
								if (songs.size() > 0)
									builder.setCharAt(builder.length()-1, ' ');
								
								// Building response and replacing LF (ASCII code 10) with '\n',
								// so the output file looks all right.
								String response = view.getTemplate()
										.replace("PLAYLIST_DATA", builder.toString())
										.replace((char) 10, '\n');
								
								// System.out.println(response);
								
								return response;
							}
			
						}
					)
				);
		
		Server server = new Server(router);
		server.run(true, false);
		
	}
}
