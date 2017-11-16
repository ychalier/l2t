package score;

import java.util.ArrayList;
import java.util.function.Function;

import data.Song;

public class ScoreMatrix {
	
	private double[][] matrix;
	private double[] score;
	private int n;
	private int m;
	
	public ScoreMatrix(ArrayList<Song> songs, Function<Song, int[]> func) {
		n = func.apply(songs.get(0)).length;
		m = songs.size();
		matrix = new double[n][m];
		for(int j=0; j<m; j++) {
			int[] attrs = func.apply(songs.get(j));
			for(int i=0; i<n; i++) matrix[i][j] = (double) attrs[i];
		}
		normalize();
		computeScore();
	}
	
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
	
	private void computeScore() {
		score = new double[m];
		for(int j=0; j<m; j++) {
			for(int i=0; i<n; i++) score[j] += matrix[i][j];
			score[j] /= (double) n;
		}
	}
	
	public double[] getScore() {
		return score;
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
