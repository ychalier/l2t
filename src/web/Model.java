package web;

import data.Library;
import data.SearchEngine;
import scrapper.RedditAPI;

/**
 * 
 * A wrapper to be able to access the database
 * and operate with it from anywhere/
 * 
 * @author Yohan Chalier
 *
 */
public class Model {

	private RedditAPI    api;
	private Library      library;
	private SearchEngine searchEngine;
	
	public Model() {}
	
	/**
	 * The constructor requires a library.
	 * Pass null to create a dummy model,
	 * and then only set the API.
	 * 
	 * @param library
	 */
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
	
	public void setLibrary(Library library) {
		this.library = library;
		this.searchEngine = new SearchEngine(library);
	}
	
}
