package web;

import data.Library;
import data.SearchEngine;
import scrapper.RedditAPI;

public class Model {
	
	private final Library      library;
	private final SearchEngine searchEngine;
	
	private RedditAPI api;
	
	public Model(Library library) {
		this.library = library;
		this.searchEngine = new SearchEngine(library);
	}
	

	public SearchEngine getSearchEngine() {
		return searchEngine;
	}

	
	public Library getLibrary() {
		return library;
	}
	
	
	public RedditAPI getApi() {
		return api;
	}
	
	
	public void setApi(RedditAPI api) {
		this.api = api;
	}
	
}
