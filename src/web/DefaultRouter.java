package web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import data.Song;
import scrapper.YouTubeAPI;
import tools.Logger;

public class DefaultRouter extends Router {

	public DefaultRouter(Model model) throws IOException {
		super(model);
		
		// Landing page
		Logger.wrD("ROUTER", "Creating view: landing page");
		addView("^$",
				new TemplateView(Server.TEMPLATES_DIR + "base.html",
				new ViewEngine() {

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
						
						return ((TemplateView) view).getTemplate().replace(
								"GENRES", 
								builder.toString());
					}
				}
			)
		);
		
		// Player page
		Logger.wrD("ROUTER", "Creating view: playlist page");
		addView("^search/([a-zA-Z0-9-]+)$", 
				new TemplateView(Server.TEMPLATES_DIR + "playlist.html",
				new ViewEngine() {

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
									+ "\"" + song.title.replace((char) 34, '\'') + "\","
									+ "\"" + song.thumbnail + "\""
								    + "],");
						}
						
						// Removing last comma
						if (songs.size() > 0)
							builder.setCharAt(builder.length()-1, ' ');
						
						// Building response and replacing LF (ASCII code 10) with '\n',
						// so the output file looks all right.
						String response = ((TemplateView) view).getTemplate()
								.replace("PLAYLIST_DATA", builder.toString())
								.replace((char) 10, '\n');
						
						// System.out.println(response);
						
						return response;
					}
				}
			)
		);
		
		// Library page
		Logger.wrD("ROUTER", "Creating view: library page");
		addView("^library\\/?$",
				new TemplateView(Server.TEMPLATES_DIR + "library.html",
				new ViewEngine() {
					
					@Override
					public String process(View view) {
						StringBuilder builder = new StringBuilder();
						
						for (String key: view.getModel().getLibrary().getSongs().keySet()) {
							Song song = view.getModel().getLibrary().getSongs().get(key);
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
						String response = ((TemplateView) view).getTemplate()
								.replace("PLACEHOLDER", builder.toString());
						return response;
					}
				}
			)
		);
		
		// Not a real view, used to answer when
		// the wait page asks for an answer.
		addView("^ask$", 
				new View(new ViewEngine() {
					@Override
					public String process(View view) {
						if (view.getModel().getLibrary() != null)
							return "ACK";
						return "NO";
					}
					
				}
			)
		);
		
		// Authorizing view
		Logger.wrD("ROUTER", "Creating view: authorize page");
		addView("^authorize?$",
				new TemplateView(Server.TEMPLATES_DIR + "wait.html",
				new ViewEngine() {

					@Override
					public String process(View view) {
						
						String[] query = view.getQuery();
						String code = null;
						String seed = null;
						
						// Getting code & state (seed)
						for(int i=0; i<query.length; i++) {
							String[] param = query[i].split("=");
							if (param.length == 2 
									&& param[0].equals("code"))
								code = param[1];
							if (param.length == 2 
									&& param[0].equals("state"))
								seed = param[1];
						}
						
						if (code != null && seed != null)
							try {
								view
								.getModel()
								.getApi()
								.getAuthentifier()
								.retrieveCodeServerIn(code, seed);
							} catch (Exception e) {
								e.printStackTrace();
							}
						
						return ((TemplateView) view).getTemplate();
					}	
				}
			)
		);
		
		// Waiting page
		Logger.wrD("ROUTER", "Creating view: waiting page");
		addView("^wait$", new TemplateView(
				Server.TEMPLATES_DIR + "wait.html",
				new StaticEngine()));
	}
	
	
}
