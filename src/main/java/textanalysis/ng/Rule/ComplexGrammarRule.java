package textanalysis.ng.Rule;

import java.util.ArrayList;
import textanalysis.ng.ParserMatch;
import textanalysis.ng.ParserToken;

public abstract class ComplexGrammarRule implements GrammarRuleI {

    protected boolean inserted = false;
    protected int currentIndex = 0;

    protected boolean firstRun = true;
    protected ArrayList<GrammarRuleI> rules = new ArrayList();

    public ArrayList<GrammarRuleI> getRules() {
        return rules;
    }

    public boolean isFirstRun() {
        return firstRun;
    }

    public void setFirstRun(boolean firstRun) {
        this.firstRun = firstRun;
    }

    public boolean isInserted() {
        return inserted;
    }

    public void setInserted(boolean inserted) {
        this.inserted = inserted;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    /**
     * Returns number of nested rules in the complex rule without final terminal
     * rule
     *
     * @return
     */
    public int rulesCount() {
        return this.rules.size() - 1;
    }

    @Override
    public boolean isSimple() {
        return false;
    }

    private boolean repeatable = false;

    public boolean isRepeatable() {
        return repeatable;
    }

    public void setRepeatable(boolean repeatable) {
        this.repeatable = repeatable;
    }

    public boolean shift(ParserToken token) {
        this.firstRun = false;
        return this.shift(token, false);
    }

    public abstract boolean shift(ParserToken token, boolean recheck);

    public abstract void reset();

    public ArrayList<ParserMatch> reduce() {
        return this.reduce(false, false);
    }

    public abstract ArrayList<ParserMatch> reduce(boolean endOfStream, boolean parentRepeatable);
}
