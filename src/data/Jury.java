package data;

import java.util.ArrayList;
import java.util.function.Function;

/**
 * 
 * A class handling the score computing
 * of a library.
 * 
 * @author Yohan Chalier
 *
 */
public class Jury {
	
	private Library library;
		
	public Jury(Library library) {
		this.library = library;
	}
	
	/**
	 * Computes the scores of the songs of the library.
	 * 
	 * Creates a score matrix for fame and quality,
	 * computes the scores and set them to the songs.
	 * 
	 * @see ScoreMatrix
	 */
	public void computeScores() {
		
		ArrayList<Song> songs = new ArrayList<Song>();
		for (String key: library.getSongs().keySet())
			songs.add(library.getSongs().get(key));
		
		// Fame score matrix
		ScoreMatrix fameMatrix = new ScoreMatrix(
				songs,
				(Function<Song, int[]>) this::getFameAttrs);
		
		// Quality score matrix
		ScoreMatrix qualityMatrix = new ScoreMatrix(
				songs,
				(Function<Song, int[]>) this::getQualityAttrs);
		
		// Computing scores
		double[] fameScore = fameMatrix.getScore();
		double[] qualityScore = qualityMatrix.getScore();
		
		// Setting values to songs objects
		for(int i = 0; i < songs.size(); i++) {
			songs.get(i).fame = fameScore[i];
			songs.get(i).quality = qualityScore[i];
		}
	}
	
	/**
	 * Returns the parameters used to compute fame score.
	 * 
	 * @param song The song to retrieve attributes from.
	 * @return An array of those attributes values
	 */
	private int[] getFameAttrs(Song song) {
		return new int[] {
			song.ups + song.downs,
			song.nRedditComments,
			song.views,
			song.likes + song.dislikes,
			song.nExtComments
		};
	}
	
	/**
	 * Returns the parameters used to compute quality score.
	 * 
	 * @param song The song to retrieve attributes from.
	 * @return An array of those attributes values
	 */
	private int[] getQualityAttrs(Song song) {
		
		int redditScore = 0;
		int extScore = 0;
		
		// Little chemistry to get meaningful values
		// Basically (ups-downs)/(ups+downs)
		if (song.ups + song.downs != 0)
			redditScore = (1000 * (song.ups - song.downs)) / (song.ups + song.downs);
		if (song.likes + song.dislikes != 0)
			extScore = (1000 * (song.likes - song.dislikes)) / (song.likes + song.dislikes);
		
		return new int[] {
			redditScore,
			extScore
		};
	}
	
}
