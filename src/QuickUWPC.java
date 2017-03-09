/**
 * Represents a union-find data structure a using weighted quick union
 * with path compression structure.
 * <p>
 */

public class QuickUWPC implements IUnionFind {
	private int myComponents;	
	private int[] parentID;
	private int[] size;

	/**
	 * Default constructor
	 */
	public QuickUWPC() {
		myComponents = 0;
		parentID = null;
        size = null;
	}
	
	/**
	 * Constructor that creates N isolated components
	 */
	public QuickUWPC(int N) {
		initialize(N);
	}
	
	// instantiate N isolated components 0 through N-1
	public void initialize(int n) {
		myComponents = n;
		parentID = new int[n];
        size = new int[n];
        for (int i = 0; i < n; i++) {
            parentID[i] = i;
            size[i] = 1;
        }
	}
	
	// return number of connected components
	public int components() {
		return myComponents;
	}
	
	// returns id of component corresponding to element x once the root of x is found
	// implements path compression
	public int find(int x) {
		validate(x);
		while (x != parentID[x]) {
			parentID[x] = parentID[parentID[x]];
			x = parentID[x];
		}
		return x;
	}

    // validate that p is a valid index
    private void validate(int p) {
    	int n = parentID.length;
        if (p < 0 || p >= n) {
            throw new IndexOutOfBoundsException("index " + p + " is not between 0 and " + (n - 1));  
        }
    }
	
	// returns true if elements p and q are in the same component
	public boolean connected(int p, int q) {
		return find(p) == find(q);
	}

	// merge components containing p and q
	public void union(int p, int q) {
        int rootP = find(p);
        int rootQ = find(q);
        if (rootP == rootQ) return;

        // make smaller root point to larger one
        if (size[rootP] < size[rootQ]) {
            parentID[rootP] = rootQ;
            size[rootQ] += size[rootP];
        }
        else {
            parentID[rootQ] = rootP;
            size[rootP] += size[rootQ];
        }
        myComponents--;
    }
}
