package textanalysis.ng;

import java.util.ArrayList;

public class ParserStack {

    // TODO possible it should be the hashMap to preserve the keys
    private ArrayList<ParserMatch> allocator = new ArrayList();

    /**
     * TODO make allocator reversed before foreach iteration
     *
     * @param ruleIndex
     * @return
     */
    public boolean have_matches_by_rule_index(int ruleIndex) {

        for (ParserMatch m : this.allocator) {
            if (m.index == ruleIndex) {
                return true;
            }
        }
        return false;
    }

    /**
     * @TODO: Should return list of ParserToken objects
     * @return 
     */
    public ArrayList<ParserMatch> flatten() {

        ArrayList<ParserMatch> result = new ArrayList();

        for (ParserMatch it : this.allocator) {
            result.add(it);
        }

        return result;
    }

    public void push(ParserMatch element) {
        this.allocator.add(element);
    }

    public int size() {
        return this.allocator.size();
    }

}
