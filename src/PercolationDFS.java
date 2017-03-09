import java.util.Arrays;

/**
 * Simulate percolation thresholds for a grid-base system using depth-first-search,
 * aka 'flood-fill' techniques for determining if the top of a grid is connected
 * to the bottom of a grid.
 * <P>
 * Modified from the COS 226 Princeton code for use at Duke. The modifications
 * consist of supporting the <code>IPercolate</code> interface, renaming methods
 * and fields to be more consistent with Java/Duke standards and rewriting code
 * to reflect the DFS/flood-fill techniques used in discussion at Duke.
 * <P>
 * @author Kevin Wayne, wayne@cs.princeton.edu
 * @author Owen Astrachan, ola@cs.duke.edu
 * @author Jeff Forbes, forbes@cs.duke.edu
 */


public class PercolationDFS implements IPercolate {
	// instance variable for storing grid state
	public int[][] myGrid;
	private int myOpenSites;

	/**
	 * Initialize a grid so that all cells are blocked.
	 * 
	 * @param n 
	 * 				is the size of the simulated (square) grid
	 */
	
	public PercolationDFS(int n) {
		myOpenSites  = 0;
		myGrid = new int[n][n];
		for (int[] row : myGrid)
			Arrays.fill(row, BLOCKED);
//		alternative way to fill grid
//		for (int i = 0; i < myGrid.length; i++)
//			for (int j = 0; j < myGrid[i].length; j++)
//				myGrid[i][j] = BLOCKED;
	}

	public void open(int i, int j) {
		if (myGrid[i][j] != BLOCKED)
			return;
		
		myOpenSites++;
		myGrid[i][j] = OPEN;

		// flush grid; mark all full cells as open
		for (int k = 0; k < myGrid.length; k++)
			for (int l = 0; l < myGrid[k].length; l++)
				if (isFull(k, l))
					myGrid[k][l] = OPEN;
		
		// runs dfs on every open cell in top row to see which ones can be marked as full
		for (int col = 0; col < myGrid[0].length; col++) {
			if (isOpen(0, col))
				dfs(0, col);
		}
	}
	
	public boolean isOpen(int i, int j) {
		if (i < 0 || i >= myGrid.length || j < 0 || j >= myGrid[0].length)
			// out of bounds
			throw new IndexOutOfBoundsException("Index " + i + "," + j + " is bad!");
		
		// check if the cell (row, column) of myGrid is open
		return myGrid[i][j] == OPEN;
	}

	public boolean isFull(int i, int j) {
		if (i < 0 || i >= myGrid.length || j < 0 || j >= myGrid[0].length)
			// out of bounds
			throw new IndexOutOfBoundsException("Index " + i + "," + j + " is bad!");
		
		// check if the cell (row, column) of myGrid is full
		return myGrid[i][j] == FULL;
	}

	public int numberOfOpenSites() {
		// returns the number of calls to open new sites
		return myOpenSites;
	}

	public boolean percolates() {
		// run DFS to find all full sites
		for (int col = 0; col < myGrid[0].length; col++) {
			if (isOpen(0, col))
				dfs(0, col);
		}
		
		for (int col = 0; col < myGrid[0].length; col++) {
			if (isFull(myGrid.length - 1, col))
				return true;
		}
		return false;
	}

	/**
	 * Private helper method to mark all cells that are open and reachable from
	 * (row,col).
	 * 
	 * @param row
	 *            is the row coordinate of the cell being checked/marked
	 * @param col
	 *            is the col coordinate of the cell being checked/marked
	 */
	
	private void dfs(int row, int col) {
		// checks for out of bounds
		if (row < 0 || row >= myGrid.length || col < 0 || col >= myGrid.length)
			return;
		
		// checks if cell is full (already checked) or is just not open
		if (isFull(row, col) || !isOpen(row, col))
			return;
		
		// mark previously open cell as full
		myGrid[row][col] = FULL;
		
		// recursive calls to check adjacent cells 
		dfs (row - 1, col); // up
		dfs (row, col - 1); // left
		dfs (row, col + 1); // right
		dfs (row + 1, col); // down
	}
}
