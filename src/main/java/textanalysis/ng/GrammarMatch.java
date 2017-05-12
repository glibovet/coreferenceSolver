package textanalysis.ng;

import java.util.ArrayList;

public class GrammarMatch {

    public ParserGrammar matchedRule;

    // TODO
    // no need to use extra object here. just use tokens
    public ArrayList<ParserMatch> tokensMatched;

    public GrammarMatch(ParserGrammar matchedRule, ArrayList<ParserMatch> tokensMatched) {
        this.matchedRule = matchedRule;
        this.tokensMatched = tokensMatched;
    }
}
