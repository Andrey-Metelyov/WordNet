import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {
    private Digraph G;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        this.G = G;
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        int ancestor = ancestor(v, w);
        if (ancestor == -1) {
            return -1;
        }
        BreadthFirstDirectedPaths bfdpV = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfdpW = new BreadthFirstDirectedPaths(G, w);
        return bfdpV.distTo(ancestor) + bfdpW.distTo(ancestor);
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        BreadthFirstDirectedPaths bfdpV = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfdpW = new BreadthFirstDirectedPaths(G, w);

        int minLength = -1;
        int ancestor = -1;
        for (int i = 0; i < G.V(); i++) {
            if (bfdpV.hasPathTo(i) && bfdpW.hasPathTo(i)) {
                int length = bfdpV.distTo(i) + bfdpW.distTo(i);
                if (minLength == -1 || length < minLength) {
                    minLength = length;
                    ancestor = i;
                }
            }
        }
        return ancestor;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        int ancestor = ancestor(v, w);
        if (ancestor == -1) {
            return -1;
        }
        BreadthFirstDirectedPaths bfdpV = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfdpW = new BreadthFirstDirectedPaths(G, w);
        return bfdpV.distTo(ancestor) + bfdpW.distTo(ancestor);
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        BreadthFirstDirectedPaths bfdpV = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfdpW = new BreadthFirstDirectedPaths(G, w);

        int minLength = -1;
        int ancestor = -1;
        for (int i = 0; i < G.V(); i++) {
            if (bfdpV.hasPathTo(i) && bfdpW.hasPathTo(i)) {
                int length = bfdpV.distTo(i) + bfdpW.distTo(i);
                if (minLength == -1 || length < minLength) {
                    minLength = length;
                    ancestor = i;
                }
            }
        }
        return ancestor;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        // In in = new In(args[0]);
        In in = new In("digraph1.txt");
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
