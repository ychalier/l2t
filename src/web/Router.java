package web;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tools.Logger;
import tools.Regex;

/**
 * 
 * Implements a engine to associate routes and views.
 * 
 * @author Yohan Chalier
 *
 */
public class Router {
	
	private Map<String, View> map;
	private Model  model;	
	
	/**
	 * The association is made using a hash map.
	 * 
	 * @param model The model from the MVC scheme to link
	 * 				the views with the data.
	 */
	public Router(Model model) {
		this.model = model;
		map = new HashMap<String, View>();
		
		Logger.wrD("ROUTER", "Creating view: static");
		try {
			addView("^" + Server.STATIC_DIR + ".+", new View(new ViewEngine() {

				@Override
				public String process(View view) {
					List<String> hierarchy = view.getHierarchy();
					StringBuilder url = new StringBuilder();
					for (String part: hierarchy)
						url.append(part + "/");
					String filename = url.subSequence(0, url.length()-1).toString();
					try {
						return new TemplateView("/" + filename, new StaticEngine()).getTemplate();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return "";
				}
				
			}));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public Model getModel() {
		return model;
	}
	
	/**
	 * Adds a view to the map and sets its model to it
	 * 
	 * @param pattern The regular expression of the route
	 * @param view The corresponding view
	 * @see TemplateView
	 */
	public void addView(String pattern, View view) {
		view.setModel(model);
		map.put(pattern, view);
	}
	
	/**
	 * Maps a request to a view, by matching
	 * the regular expressions.
	 * 
	 * @param request The GET request
	 * @return The view corresponding, null if not found (404)
	 * @throws IOException
	 */
	public View findView(String request)  {
		// Removes the GET and the HTTP/1.1
		String route = Regex.parse(Regex.PATTERN_ROUTE, request);
		
		// Splits the hierarchical part from the query part
		String[] split = route.split("\\?");
		String url = split[0];
		String[] query = null;
		if (split.length >= 2) query = split[1].split("&");
		
		// Checking all views to find one corresponding.
		List<String> hierarchy;
		View view;
		for(String pattern : map.keySet()) {
			if ((hierarchy = Regex.parseAll(pattern, url)) != null) {
				view = map.get(pattern);
				view.setRequest(hierarchy, query);
				return view;
			}
		}
		return null;
	}

}
