package data;

import java.util.ArrayList;
import java.util.function.Function;

/**
 * 
 * Represents a matrix used to compute score (fame or quality).
 * A row corresponds to one attribute relevant for the score.
 * A column corresponds to a song.
 * 
 * @author Yohan Chalier
 *
 */
public class ScoreMatrix {
	
	private double[][] matrix;
	private double[] score;
	private int n;
	private int m;
	
	/**
	 * All operations are performed in the constructor,
	 * to automate operations.
	 * 
	 * @param songs Songs for the columns of the matrix
	 * @param func A function to get the relevant attributes
	 * 			   from a song for the score computation
	 */
	public ScoreMatrix(ArrayList<Song> songs, Function<Song, int[]> func) {
		// Building the matrix
		n = func.apply(songs.get(0)).length; // Number of rows
		m = songs.size();					 // Number of columns
		matrix = new double[n][m];
		for(int j=0; j<m; j++) {
			int[] attrs = func.apply(songs.get(j));
			for(int i=0; i<n; i++) matrix[i][j] = (double) attrs[i];
		}
		
		// Centering and reducing
		normalize();
		
		// Computing average
		computeScore();
	}
	
	
	public double[] getScore() {
		return score;
	}
	
	/**
	 * Centers and reduces the matrix, by computing
	 * the average and the deviation.
	 */
	private void normalize() {
		for(int i=0; i<n; i++) {
			double sum = 0;
			for(int j=0; j<m; j++) sum += matrix[i][j];
			double mean = sum / (double) m;
			sum = 0;
			for(int j=0; j<m; j++) sum += (matrix[i][j] - mean) * (matrix[i][j] - mean);
			double sd = Math.sqrt(sum / (double) m);
			for(int j=0; j<m; j++) matrix[i][j] = (matrix[i][j] - mean) / sd;
		}
	}
	
	/**
	 * Attributes a value for each song (each column)
	 * corresponding to the average of the column.
	 */
	private void computeScore() {
		score = new double[m];
		for(int j=0; j<m; j++) {
			for(int i=0; i<n; i++) score[j] += matrix[i][j];
			score[j] /= (double) n;
		}
	}

	
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		for(int i=0; i<n; i++) {
			for(int j=0; j<m; j++) buffer.append(Double.toString(matrix[i][j]) + " ");
			buffer.append("\n");
		}
		return buffer.toString();
	}

}
