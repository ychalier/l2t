package test;

import java.io.IOException;

import org.json.JSONException;

import scrapper.*;
import data.*;

public class Main {

	public static void main(String[] args) throws JSONException, IOException {
		
		RedditAPI api = new RedditAPI("O4_S_-j1vdVw8Q", "", 
				"http://start.byethost31.com/leethan2theece/");
		
		Library library = new Library(api.fetchData(100));

	}
}
