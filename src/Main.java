import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import data.Library;
import data.Song;
import scrapper.YouTubeAPI;
import tools.Config;
import tools.Logger;
import web.Model;
import web.Router;
import web.Server;
import web.TemplateEngine;
import web.View;

/**
 * 
 * arguments
 * -l  --log              Activate the logger (into file .log)
 * -c  --config  [PATH]   Loads a config file
 * 
 * @author Yohan Chalier
 *
 */
public class Main {

	public static void main(String[] args) throws Exception {
		
		// Reading arguments
		boolean log = false;
		String configPath = null;
		int i = 0;
		while (i < args.length) {
			if (args[i].equals("-l") || args[i].equals("--log"))
				log = true;
			else if ((args[i].equals("-c") || args[i].equals("--config")) && i < args.length-1) {
				configPath = args[i+1];
				i++;
			}
			else if ((args[i].equals("-h") || args[i].equals("--help"))) {
				System.out.println("usage: java -jar [jarfile] [options]\n"
						+ "Options and arguments:\n"
						+ "-c --config [FILENAME] : load a config file\n"
						+ "-l --log               : activate the logger (logfile set in config)\n"
						+ "-h --help              : show this message");
				return ;
			}
			else {
				System.out.println("invalid parameter: " + args[i] + "\nTry with -h for help.");
				return;
			}
			i++;
		}
		
		// Load configuration
		if (configPath != null) {
			Logger.wr("Loading config file: " + configPath);
			System.out.println("Loading config file: " + configPath);
			Config.load(new File(configPath));
		} else {
			Logger.wr("No config file specified. Using default values.");
			System.out.println("No config file specified. Using default values.");
		}
		
		// Initialize logger
		new Logger(log);
			
		Object[] objs      = Library.loadLibrary();
		Library library    = (Library) objs[0];
		Boolean newLibrary = (Boolean) objs[1];
		Thread  serverTh   = (Thread)  objs[2];
		Model   model      = new Model(library);
		Router  router     = new Router(model);
				
		// Landing page
		router.addView("^$",
				new View(Server.TEMPLATES_DIR + "base.html",
						new TemplateEngine() {

							@Override
							public String process(View view) {
								
								Map<String, Integer> map = view
										.getModel()
										.getLibrary()
										.getGenres();
								
								ArrayList<String> genres = new ArrayList<String>();
								for (String genre: map.keySet())
									genres.add(genre);
								genres.sort(new Comparator<String>() {
									@Override
									public int compare(String o1, String o2) {
										if (map.get(o1) > map.get(o2)) 
											return -1;
										if (map.get(o1) < map.get(o2)) 
											return  1;
										return 0;
									}
								});
								
								StringBuilder builder = new StringBuilder();
								for (String genre: genres)
									builder.append("\"" + genre + "\",");
								builder.setCharAt(builder.length()-1, ' ');
								
								return view.getTemplate().replace(
										"GENRES", 
										builder.toString());
							}
					
				}));
		
		// Player page
		router.addView("^search/([a-zA-Z0-9-]+)$", 
				new View(Server.TEMPLATES_DIR + "playlist.html",
						new TemplateEngine() {

							@Override
							public String process(View view) {
								
								String query = view.getHierarchy().get(1);
								
								List<Song> songs;
								if (view.getQuery() != null && view.getQuery().length > 0) {
									songs = view
											.getModel()
											.getSearchEngine()
											.searchRandom(query);
								} else {
									songs = view
											.getModel()
											.getSearchEngine()
											.search(query);
								}
								
								StringBuilder builder = new StringBuilder();

								// Format: [["YouTube ID", "Artist", "Title"], ...];
								// Replacing char " (ASCII code 34) (causes troubles in JS)
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
		
		// Library page
		router.addView("^library\\/?$",
				new View(Server.TEMPLATES_DIR + "library.html",
					new TemplateEngine() {
						@Override
						public String process(View view) {
							StringBuilder builder = new StringBuilder();
							
							for (Song song: view.getModel().getLibrary().getSongs()) {
								builder.append("<tr>"
										+ "<td>" + song.id + "</td>"
										+ "<td>" + song.artist + "</td>"
										+ "<td>" + song.title + "</td>"
										+ "<td>" + song.toStringGenres() + "</td>"
										+ "<td>" + song.domain + "</td>"
										+ "<td><a href=\"" + song.url + "\"> " + song.url + "</a></td>"
										+ "<td>" + song.fame + "</td>"
										+ "<td>" + song.quality + "</td>"
										+ "</tr>");
							}	
							
							String response = view.getTemplate()
									.replace("PLACEHOLDER", builder.toString());
							return response;
						}
			
					}
				));
		
		// Not a real view, used to answer when
		// the wait page asks for an answer.
		router.addView("^ask$", new View(Server.TEMPLATES_DIR + "wait.html",
				new TemplateEngine() {

					@Override
					public String process(View view) {
						return "ACK";
					}
			
				}
			));
		
		if (serverTh != null)
			serverTh.interrupt();
		
		Server server = new Server(router);
		
		// If the library is new, the wait page
		// has been loaded and will update automatically
		// so there is no need to start a new one.
		if (newLibrary.booleanValue())
			server.run(false, false, true);
		else
			server.run(true, false, true);
				
	}
}
