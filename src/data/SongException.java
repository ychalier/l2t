package data;

/**
 * 
 * Used to log errors occurring during the 
 * information extraction of a song.
 * 
 * @author Yohan Chalier
 *
 */
@SuppressWarnings("serial")
public class SongException extends Exception {
	
	public SongException() {}
	
	public SongException(String message) {
		super(message);
	}

}
