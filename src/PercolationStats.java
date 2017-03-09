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
		N = N_loc;
		T = T_loc;
		fractions = new double[T];
		if (N <= 0 || T <= 0)
			throw new IllegalArgumentException();
		
		long start = System.currentTimeMillis(); // start time for percolation trial
		
		// perform T experiments for an N-by-N grid
		for (int i = 0; i < T; i++) {
			PercolationUF perc = new PercolationUF(N);
			//PercolationUF perc = new PercolationUF(N, new QuickFind(N)); // initialize new Percolation object
			int myOpenedSites = 0; // initialize a variable to keep track of opened sites
						
//			while (!perc.percolates()) {
//				int x = ourRandom.nextInt(N);
//				int y = ourRandom.nextInt(N);
//				if (!perc.isOpen(x, y)) {
//					perc.open(x, y);
//					myOpenedSites++;
//				}
//			}
			
			List<Point> sites = getShuffledCells();	// get random list of sites
			for (Point cell: sites) {
				// repeatedly declare sites open until the system percolates
//				if (!perc.isOpen(cell.x, cell.y))
					perc.open(cell.x, cell.y);
					myOpenedSites++;
					if (perc.percolates())
						break;
			}
						
			fractions[i] = (double) myOpenedSites/(N*N); // store percolation threshold in 'fractions' double array
		}
		long end = System.currentTimeMillis(); // end time for percolation trial (after percolating);
		System.out.println("Run Time: " + (end - start));
		
		mean();
		stddev();
		confidenceLow();
		confidenceHigh();
	}
	
	// generate a random list of shuffled cell positions within the grid
	private List<Point> getShuffledCells() {
		ArrayList<Point> list = new ArrayList<Point>();
		for (int i = 0; i < N; i++)
			for (int j = 0; j < N; j++)
				list.add(new Point(i, j));
		Collections.shuffle(list, ourRandom);
		return list;
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
//		if (args.length == 2) {
//			N = Integer.parseInt(args[0]);
//			T = Integer.parseInt(args[1]);
//		} else {
//			String input = JOptionPane.showInputDialog("Enter N and T", "20, 100");
//			N = Integer.parseInt(input.split(", ")[0]);
//			T = Integer.parseInt(input.split(", ")[1]);
//		}
		
		PercolationStats test = new PercolationStats(80, 10);
		
		System.out.println(test.mean());
		System.out.println(test.stddev());
		System.out.println(test.confidenceLow());
		System.out.println(test.confidenceHigh());
	}
	
}
