import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

// Models an N-by-N percolation system.
public class Percolation {
    private final boolean[][] grid;
    private final int N;
    private final WeightedQuickUnionUF sites;
    final WeightedQuickUnionUF sites2; // to prevent backwash
    private int openSites = 0;
    private final int virtualTop;
    private final int virtualBottom;


    // Create an N-by-N grid, with all sites blocked.
    //...
    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException("N must be greater than 0");
        }
        this.N = N;
        this.virtualTop = N * N; //virtual top is at N^2
        this.virtualBottom = N * N + 1; //virtual bottom is at N^2 + 1
        this.grid = new boolean[N][N]; //creating new grid
        this.sites = new WeightedQuickUnionUF(N * N + 2); //add 2 for virtual bottom and top sites
        this.sites2 = new WeightedQuickUnionUF(N * N + 1); //add 1, only connects to virtual top

        //initializing entire grid with false (all sides blocked)
        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {
                grid[r][c] = false;
            }
        }

    }


    // Open site (row, col) if it is not open already.
    public void open(int row, int col) {
        if ((row < 0 || row > N) || (col < 0 || col > N)) {
            throw new IllegalArgumentException("The value for row is outside its prescribed range");
        }
        if (!isOpen(row, col)) { //if the site is closed, open it
            grid[row][col] = true; //opening the side
            openSites++;

        }
        int siteNum = encode(row, col);
        //once you open a side, must check if there are adjacent open sized to connect
        //checking top (if top is open, connect top w/ target site
        if (row - 1 >= 0) {
            if (isOpen(row - 1, col)) {
                sites.union(siteNum, encode(row - 1, col));
                sites2.union(siteNum, encode(row - 1, col));
            }
        }
        //checking bottom
        if (row + 1 < N) {
            if (isOpen(row + 1, col)) {
                sites.union(siteNum, encode(row + 1, col));
                sites2.union(siteNum, encode(row + 1, col));
            }
        }
        //checking left
        if (col - 1 >= 0) {
            if (isOpen(row, col - 1)) {
                sites.union(siteNum, encode(row, col - 1));
                sites2.union(siteNum, encode(row, col - 1));
            }
        }
        //checking right
        if (col + 1 < N) {
            if (isOpen(row, col + 1)) {
                sites.union(siteNum, encode(row, col + 1));
                sites2.union(siteNum, encode(row, col + 1));
            }
        }
        //if the site opened is on top row, must connect to virtual top
        if (row == 0) {
            sites.union(siteNum, virtualTop);
            sites2.union(siteNum, virtualTop);
        }
        //if the site opened is on bottom row, connect to virtual bottom
        if (row == N - 1) {
            sites.union(siteNum, virtualBottom);
        }


    }

    // Is site (row, col) open?
    public boolean isOpen(int row, int col) {
        if ((row < 0 || row > N - 1) || (col < 0 || col > N - 1)) {
            throw new IllegalArgumentException("The value for row is outside its prescribed range");
        } else
            return grid[row][col];
    }

    // Is site (row, col) full? is the ist connected to the top row?
    public boolean isFull(int row, int col) {
        if ((row < 0 || row > N - 1) || (col < 0 || col > N - 1)) {
            throw new IllegalArgumentException("The value for row is outside its prescribed range");
        }
        int site = encode(row, col);
        if (isOpen(row, col)) {
            return sites2.connected(virtualTop, site); //check if site is connected to virtual top and connect

        }
        return false;
    }

    // Number of open sites.
    public int numberOfOpenSites() {
        return openSites;
    }

    // Does the system percolate?
    public boolean percolates() {
        return sites.connected(virtualTop, virtualBottom); //if the virtual top is connected to virtual bottom, then
        // the system percolates
    }

    // An integer ID (1...N) for site (row, col).
    private int encode(int row, int col) {
        return N * row + col;

    }

    // Test client. [DO NOT EDIT]
    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);
        int N = in.readInt();
        Percolation perc = new Percolation(N);
        while (!in.isEmpty()) {
            int i = in.readInt();
            int j = in.readInt();
            perc.open(i, j);
        }
        StdOut.println(perc.numberOfOpenSites() + " open sites");
        if (perc.percolates()) {
            StdOut.println("percolates");
        } else {
            StdOut.println("does not percolate");
        }

        // Check if site (i, j) optionally specified on the command line
        // is full.
        if (args.length == 3) {
            int i = Integer.parseInt(args[1]);
            int j = Integer.parseInt(args[2]);
            StdOut.println(perc.isFull(i, j));
        }
    }
}
