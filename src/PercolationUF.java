
/**
 * Simulate a system to see its Percolation Threshold, but use a UnionFind
 * implementation to determine whether simulation occurs. The main idea is that
 * initially all cells of a simulated grid are each part of their own set so
 * that there will be n^2 sets in an nxn simulated grid. Finding an open cell
 * will connect the cell being marked to its neighbors --- this means that the
 * set in which the open cell is 'found' will be unioned with the sets of each
 * neighboring cell. The union/find implementation supports the 'find' and
 * 'union' typical of UF algorithms.
 * <P>
 * 
 * @author Owen Astrachan
 * @author Jeff Forbes
 *
 */

public class PercolationUF implements IPercolate {
	private final int OUT_BOUNDS = -1;
	
	public int[][] myGrid;
	private int myOpenSites;
	IUnionFind myUnion;
	
	private int top;
	private int bottom;
	

	/**
	 * Constructs a Percolation object for a nxn grid that that creates
	 * a IUnionFind object to determine whether cells are full
	 */
	public PercolationUF(int n) {
		if (n <= 0) throw new IllegalArgumentException();
		myOpenSites = 0;
		myGrid = new int[n][n];
		top = n * n;
		bottom = n * n + 1;
		myUnion = new QuickUWPC(n * n + 2);
	}

	/**
	 * Return an index that uniquely identifies (row,col), typically an index
	 * based on row-major ordering of cells in a two-dimensional grid. However,
	 * if (row,col) is out-of-bounds, return OUT_BOUNDS.
	 */
	public int getIndex(int row, int col) {
		// TODO complete getIndex
		if (row < 0 || row >= myGrid.length || col < 0 || col >= myGrid[0].length) {
			// throw new IndexOutOfBoundsException("Index " + row + "," + col + " out of bounds.");
			return OUT_BOUNDS;
		}
		
		return row * myGrid[row].length + col;
	}

	public void open(int i, int j) {		
		if (i < 0 || i >= myGrid.length || j < 0 || j >= myGrid[0].length)
			// out of bounds
			throw new IndexOutOfBoundsException("Index " + i + "," + j + " is bad!");
		
		if (myGrid[i][j] != BLOCKED)
			return;
		
		myOpenSites++;
		myGrid[i][j] = OPEN;
		
		connect(i, j);
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
		return myUnion.connected(top, getIndex(i, j));
	}

	public int numberOfOpenSites() {
		// returns the number of calls to open new sites
		return myOpenSites;
	}

	public boolean percolates() {
		// TODO complete percolates
		return myUnion.connected(top, bottom);
	}

	/**
	 * Connect new site (row, col) to all adjacent open sites
	 */
	private void connect(int row, int col) {
		// union cell with virtual top or bottom
		if (row == 0) myUnion.union(getIndex(row, col), top);
		if (row == myGrid.length - 1) myUnion.union(getIndex(row, col), bottom);
		
		// union cell with four adjacent cells if cells are open and not out of bounds
		if ((row - 1 >= 0 && row - 1 < myGrid.length) && isOpen(row - 1, col)) myUnion.union(getIndex(row, col), getIndex(row - 1, col));
		if ((row + 1 >= 0 && row + 1 < myGrid.length) && isOpen(row + 1, col)) myUnion.union(getIndex(row, col), getIndex(row + 1, col));
		if ((col - 1 >= 0 && col - 1 < myGrid.length) && isOpen(row, col - 1)) myUnion.union(getIndex(row, col), getIndex(row, col - 1));
		if ((col + 1 >= 0 && col + 1 < myGrid.length) && isOpen(row, col + 1)) myUnion.union(getIndex(row, col), getIndex(row, col + 1));
	}
}

