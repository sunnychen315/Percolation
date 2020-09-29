import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

// Estimates percolation threshold for an N-by-N percolation system.
public class PercolationStats {

    private final int experiments;
    private final int N;
    private final double[] threshold;


    // Perform T independent experiments (Monte Carlo simulations) on an
    // N-by-N grid.
    public PercolationStats(int N, int T) {
        this.N = N;
        experiments = T;
        threshold = new double[T]; //creating array of doubles w size T
        //corner cases
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException("The value of N or T is invalid");
        }


        for (int i = 0; i < T; i++) {
            Percolation percolation = new Percolation(N);
            int openSites = 0; //num of open blocks start at 0
            //keep going through while loop and open random spots until the system percolates
            while (!percolation.percolates()) {
                //getting random row and col between 0 and N-1
                int randomRow = StdRandom.uniform(0, N);
                int randomCol = StdRandom.uniform(0, N);
                //checking if the random site is open
                if (!percolation.isOpen(randomRow, randomCol)) {
                    percolation.open(randomRow, randomCol); //if the random site is not open, then open it
                    openSites++;

                }
                //the fraction of open sites is the num of open sites
                //over the total number of sites
                threshold[i] = (double) openSites / (N * N);
            }

        }


    }

    // Sample mean of percolation threshold.
    public double mean() {
        return StdStats.mean(threshold);
    }

    // Sample standard deviation of percolation threshold.
    public double stddev() {
        return StdStats.stddev(threshold);
    }

    // Low endpoint of the 95% confidence interval.
    public double confidenceLow() {
        return mean() - ((1.96 * stddev()) / Math.sqrt(experiments));
    }

    // High endpoint of the 95% confidence interval.
    public double confidenceHigh() {
        return mean() + ((1.96 * stddev()) / Math.sqrt(experiments));
    }

    // Test client. [DO NOT EDIT]
    public static void main(String[] args) {
        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        PercolationStats stats = new PercolationStats(N, T);
        StdOut.printf("mean           = %f\n", stats.mean());
        StdOut.printf("stddev         = %f\n", stats.stddev());
        StdOut.printf("confidenceLow  = %f\n", stats.confidenceLow());
        StdOut.printf("confidenceHigh = %f\n", stats.confidenceHigh());
    }
}
