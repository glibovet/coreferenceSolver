package textanalysis.ng.Rule;

import java.util.ArrayList;
import textanalysis.ng.ParserMatch;
import textanalysis.ng.ParserToken;

public abstract class ComplexGrammarRule implements GrammarRuleI {

    @Override
    public boolean isSimple() {
        return false;
    }

    public boolean shift(ParserToken token) {
        return this.shift(token, false);
    }

    public abstract boolean shift(ParserToken token, boolean recheck);

    public abstract void reset();

    public ArrayList<ParserMatch> reduce() {
        return this.reduce(false);
    }

    public abstract ArrayList<ParserMatch> reduce(boolean endOfStream);
}
