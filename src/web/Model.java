package web;

import data.Library;
import data.SearchEngine;
import scrapper.RedditAPI;

public class Model {
	
	private final RedditAPI    api;
	private final Library      library;
	private final SearchEngine searchEngine;
	
	
	public Model(RedditAPI api, Library library) {
		this.api = api;
		this.library = library;
		this.searchEngine = new SearchEngine(library);
	}
	

	public SearchEngine getSearchEngine() {
		return searchEngine;
	}
	

	public RedditAPI getApi() {
		return api;
	}
	

	public Library getLibrary() {
		return library;
	}

}
