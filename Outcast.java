/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private WordNet wordnet;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        // StdOut.println(Arrays.toString(nouns));
        int maxDistance = 0;
        String result = "";
        for (String s : nouns) {
            int len = 0;
            for (String d : nouns) {
                len += wordnet.distance(s, d);
            }
            if (len > maxDistance) {
                maxDistance = len;
                result = s;
            }
        }
        return result;
    }

    public static void main(String[] args) {
        args = new String[] {
                "synsets.txt", "hypernyms.txt", "outcast5.txt", "outcast8.txt", "outcast11.txt"
        };
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
