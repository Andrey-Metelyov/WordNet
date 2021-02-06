import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class SAP {
    private Digraph G;
    private int cachedLength;
    private int cachedAncestor;
    private Iterable<Integer> lastComputedV;
    private Iterable<Integer> lastComputedW;
    private BreadthFirstDirectedPaths bfdpV;
    private BreadthFirstDirectedPaths bfdpW;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) {
            throw new IllegalArgumentException("Digraph G is null");
        }
        this.G = G;
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (checkCachedResult(v, w)) {
            return cachedLength;
        }
        ancestor(v, w);
        return cachedLength;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        if (checkCachedResult(v, w)) {
            return cachedAncestor;
        }
        bfdpV = new BreadthFirstDirectedPaths(G, v);
        bfdpW = new BreadthFirstDirectedPaths(G, w);

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
        cachedLength = minLength;
        cachedAncestor = ancestor;
        return ancestor;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (checkCachedResult(v, w)) {
            return cachedLength;
        }
        ancestor(v, w);
        return cachedLength;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (checkCachedResult(v, w)) {
            return cachedAncestor;
        }
        lastComputedV = v;
        lastComputedW = w;
        bfdpV = new BreadthFirstDirectedPaths(G, v);
        bfdpW = new BreadthFirstDirectedPaths(G, w);

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
        cachedLength = minLength;
        cachedAncestor = ancestor;
        return ancestor;
    }

    private boolean checkCachedResult(int v, int w) {
        for (Integer vv : lastComputedV) {
            if (vv != v) {
                return false;
            }
        }
        for (Integer ww : lastComputedW) {
            if (ww != w) {
                return false;
            }
        }
        StdOut.println("Use cached result");
        return true;
    }

    private boolean checkCachedResult(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new IllegalArgumentException("v or w is null");
        }
        if (v.equals(lastComputedV) && w.equals(lastComputedW)) {
            StdOut.println("Use cached result");
            return true;
        }
        return false;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        // In in = new In(args[0]);
        In in = new In("digraph1.txt");
        Digraph G = new Digraph(in);
        // G = null;
        SAP sap = new SAP(G);
        // while (!StdIn.isEmpty()) {
        //     int v = StdIn.readInt();
        //     int w = StdIn.readInt();
        //     int length   = sap.length(v, w);
        //     int ancestor = sap.ancestor(v, w);
        //     StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        // }
        Bag<Integer> v = new Bag<>();
        Bag<Integer> w = new Bag<>();
        v.add(7);
        v.add(2);
        v.add(null);
        w.add(4);
        w.add(9);
        int length   = sap.length(v, w);
        int ancestor = sap.ancestor(v, w);
        StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        length   = sap.length(v, w);
        ancestor = sap.ancestor(v, w);
        StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
    }
}
