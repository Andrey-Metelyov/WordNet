/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.ST;

public class WordNet {
    private final ST<Integer, Bag<String>> synset = new ST<>();
    private final ST<String, Bag<Integer>> nouns = new ST<>();
    private SAP sap;

    public WordNet(String synsets, String hypernyms) {
        processSynsets(synsets);
        processHypernyms(hypernyms);
    }

    private void processSynsets(String synsets) {
        In in = new In(synsets);
        String line;

        while ((line = in.readLine()) != null) {
            String[] elements = line.split(",", 3);
            int id = Integer.parseInt(elements[0]);
            String[] nounsArray = elements[1].split(" ");
            Bag<String> nounsBag = new Bag<>();
            for (String noun : nounsArray) {
                nounsBag.add(noun);
                if (nouns.contains(noun)) {
                    Bag<Integer> idBag = nouns.get(noun);
                    idBag.add(id);
                    nouns.put(noun, idBag);
                    // System.out.println(noun);
                }
                else {
                    Bag<Integer> idBag = new Bag<>();
                    idBag.add(id);
                    nouns.put(noun, idBag);
                }
            }
            // String gloss = elements[2];

            synset.put(id, nounsBag);

            // System.out.println(id);
            // System.out.println(Arrays.toString(nounsArray));
            // System.out.println(gloss);
        }
        // for (Integer integer : synset) {
        //     System.out.println(integer + ": " + synset.get(integer));
        // }
        // for (String noun : nouns) {
        //     System.out.println(noun + ": " + nouns.get(noun));
        // }
    }

    // + ": c + nouns.get(noun)onstructor takes the name of the two input files
    private void processHypernyms(String hypernyms) {
        In in = new In(hypernyms);
        String line;
        ST<Integer, Bag<Integer>> st = new ST<>();
        SET<Integer> keys = new SET<>();

        while ((line = in.readLine()) != null) {
            // System.out.println(line);
            String[] elements = line.split(",", 2);
            int id = Integer.parseInt(elements[0]);
            keys.add(id);
            if (elements.length < 2) {
                continue;
            }
            String[] hyp = elements[1].split(",");
            Bag<Integer> bag = new Bag<>();
            for (String s : hyp) {
                int key = Integer.parseInt(s);
                bag.add(key);
                keys.add(key);
            }
            if (st.contains(id)) {
                System.out.println("???");
            }
            st.put(id, bag);
        }
        Digraph g = new Digraph(keys.size());
        for (int key : st) {
            Bag<Integer> hyps = st.get(key);
            for (int hyp : hyps) {
                g.addEdge(key, hyp);
            }
        }
        checkRootedGraph(g);
        sap = new SAP(g);
        // System.out.println(G.V());
        // System.out.println(G.E());
    }

    private void checkRootedGraph(Digraph digraph) {
        // DirectedCycle directedCycle = new DirectedCycle(digraph);
        // if (!directedCycle.hasCycle()) {
        //     Topological topological = new Topological(digraph);
        //     if (topological.hasOrder()) {
        //         return;
        //     }
        // }
        // throw new IllegalArgumentException(
        //         "The input to the constructor does not correspond to a rooted DAG.");
        int root = findRoot(0, digraph);
        for (int v = 1; v < digraph.V(); v++) {
            int r = findRoot(v, digraph);
            if (r != root) {
                throw new IllegalArgumentException("The input to the constructor does not correspond to a rooted DAG.");
            }
        }
    }

    private int findRoot(int v, Digraph digraph) {
        int root = v;
        while (digraph.outdegree(root) != 0) {
            root = digraph.adj(root).iterator().next();
            if (root == v) {
                throw new IllegalArgumentException(
                        "The input to the constructor does not correspond to a rooted DAG (cycle found).");
            }
        }
        return root;
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nouns.keys();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return nouns.contains(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        checkNouns(nounA, nounB);
        Bag<Integer> bagA = nouns.get(nounA);
        Bag<Integer> bagB = nouns.get(nounB);
        return sap.length(bagA, bagB);
    }

    private void checkNouns(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException(
                    "Any of the noun arguments in distance() or sap() is not a WordNet noun");
        }
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        checkNouns(nounA, nounB);
        Bag<Integer> bagA = nouns.get(nounA);
        Bag<Integer> bagB = nouns.get(nounB);
        int ancestor = sap.ancestor(bagA, bagB);
        Bag<String> ancestors = synset.get(ancestor);
        StringBuilder result = new StringBuilder();
        for (String s : ancestors) {
            result.append(s).append(" ");
        }
        return result.toString();
    }

    // do unit testing of this class
    public static void main(String[] args) {
        // args = new String[] { "synsets.txt", "hypernyms.txt", "worm", "bird" };
        // args = new String[] { "synsets.txt", "hypernyms6InvalidTwoRoots.txt", "worm", "bird" };
        WordNet wordNet = new WordNet(args[0], args[1]);
        // wordNet = new WordNet("synsets.txt", null);
        // wordNet = new WordNet(null, "hypernyms.txt");
        // System.out.println(wordNet.nouns());
        System.out.println(wordNet.distance(args[2], args[3]));
        System.out.println(wordNet.sap(args[2], args[3]));
    }
}
