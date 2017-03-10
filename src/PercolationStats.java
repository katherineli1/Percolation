import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.swing.JOptionPane;

/**
 * Compute statistics on Percolation after performing T independent experiments on an N-by-N grid.
 * Compute 95% confidence interval for the percolation threshold, and  mean and std. deviation
 * Compute and print timings
 * 
 * @author Kevin Wayne
 * @author Jeff Forbes
 * @author Josh Hug
 */

public class PercolationStats {
	public static int RANDOM_SEED = 1234;
	public static Random ourRandom = new Random(RANDOM_SEED);
	
	private int N, T;
	private double[] fractions;
	
	/** 
	 * Initialize and gather data from T experiments on an N-by-N grid necessary to calculate mean, 
	 * standard deviation, and 95% confidence intervals on percolation thresholds
	 * 
	 * @param N
	 * 				size of Percolation grid
	 * @param T
	 * 				number of times experiment will be run (number of percolation threshold data points)
	 * @param times
	 * 				long array of run times for percolation
	 * @param fractions
	 * 				double array of percolation thresholds
	 */
	public PercolationStats(int N_loc, int T_loc) {
		if (N_loc <= 0 || T_loc <= 0)
			throw new IllegalArgumentException();
		
		N = N_loc;
		T = T_loc;
		fractions = new double[T];
		
		// perform T experiments for an N-by-N grid
		for (int i = 0; i < T; i++) {
			PercolationUF perc = new PercolationUF(N);
			//PercolationUF perc = new PercolationUF(N, new QuickFind(N)); // initialize new Percolation object
			int myOpenedSites = 0; // initialize a variable to keep track of opened sites
						
			while (!perc.percolates()) {
				int x = ourRandom.nextInt(N);
				int y = ourRandom.nextInt(N);
				if (!perc.isOpen(x, y)) {
					perc.open(x, y);
					myOpenedSites++;
				}
			}
						
			fractions[i] = (double) myOpenedSites/(N*N); // store percolation threshold in 'fractions' double array
		}
	}
	
	// calculate sample mean for generated percolation thresholds
	public double mean() {
		double sum = 0;
		for (double n : fractions)
			sum += n;
		return sum/ T;
	}
	
	// calculate sample standard deviation for generated percolation thresholds
	public double stddev() {
		double sum = 0;
		if (T == 1) return Double.NaN;
		for (double n : fractions)
			sum += Math.pow(n - mean(), 2);
		return Math.sqrt(sum/(T - 1));
	}
	
	// calculate low endpoint of 95% confidence interval for generated percolation thresholds
	public double confidenceLow() {
		return mean() - ((1.96 * stddev()) / Math.sqrt(T));
	}
	
	// calculate high endpoint of 95% confidence interval for generated percolation thresholds
	public double confidenceHigh() {
		return mean() + ((1.96 * stddev()) / Math.sqrt(T));
	}
	
	// print out statistics values for testing and analysis
	public static void main(String[] args) {
		long start = System.currentTimeMillis(); // start time for percolation trial
		PercolationStats test = new PercolationStats(20, 10);
		long end = System.currentTimeMillis(); // end time for percolation trial (after percolating);
		
		System.out.println("Run Time: " + (end - start));
		System.out.println("Mean: " + test.mean());
		System.out.println("Standard Dev.: " + test.stddev());
		System.out.println("95% CI Lower Bound: " + test.confidenceLow());
		System.out.println("95% CI Upper Bound: " + test.confidenceHigh());
	}
	
}
