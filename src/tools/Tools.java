package tools;

import java.io.IOException;

/**
 * 
 * Implements misc tools to ease programming.
 * 
 * @author Yohan Chalier
 *
 */
public class Tools {
	
	/**
	 * Formats a double into a fancy string
	 * 
	 * @param d number to print
	 * @return fancy string
	 */
	public static String doubleToStr(double d) {
		if(d < 0.01 && d > 0) return "0.0-";
		return Double
				.toString(d)
				.substring(0, 
						Math.min(4, 
								Double.toString(d).length()));
	}
	
	/**
	 * Opens a URL in the default browser
	 * 
	 * @param url
	 */
	public static void openBrowser(String url) {
		try {
			Runtime.getRuntime().exec("xdg-open " + url);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
