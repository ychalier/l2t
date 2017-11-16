package score;

import java.util.function.Function;

import data.Library;
import data.Song;

public class Jury {
	
	private Library library;
		
	public Jury(Library library) {
		this.library = library;
	}
	
	public void computeScores() {
		ScoreMatrix fameMatrix = new ScoreMatrix(
				library.getSongs(),
				(Function<Song, int[]>) this::getFameAttrs);
		
		ScoreMatrix qualityMatrix = new ScoreMatrix(
				library.getSongs(),
				(Function<Song, int[]>) this::getQualityAttrs);
		
		double[] fameScore = fameMatrix.getScore();
		double[] qualityScore = qualityMatrix.getScore();
		
		for(int i=0; i<library.getSongs().size(); i++) {
			library.getSongs().get(i).fame = fameScore[i];
			library.getSongs().get(i).quality = qualityScore[i];
		}
	}
	
	private int[] getFameAttrs(Song song) {
		return new int[] {
			song.ups + song.downs,
			song.nRedditComments,
			song.views,
			song.likes + song.dislikes,
			song.nExtComments
		};
	}
	
	private int[] getQualityAttrs(Song song) {
		
		int redditScore = 0;
		int extScore = 0;
		
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
