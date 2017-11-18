package web;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tools.Regex;

public class Router {
	
	private Map<String, View> map;
	
	public Router() {
		map = new HashMap<String, View>();
	}
	
	public void addView(String pattern, View view) {
		map.put(pattern, view);
	}
	
	public View findView(String request) throws IOException {
		String route = Regex.parse(Regex.PATTERN_ROUTE, request);
		String[] split = route.split("\\?");
		
		String url = split[0];
		String[] query = null;
		if (split.length >= 2) query = split[1].split("&");
		
		// Handling static file
		int index = url.lastIndexOf('.');
		if (index != -1) {
			String extension = url.substring(index);
			if (extension.matches(Server.STATIC_FILES)) {
				if (new File(Server.STATIC_DIR + url).exists()){
					return new View(Server.STATIC_DIR + url, null, new StaticEngine());
				} else {
					return null;
				}
			}
		}
		
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
