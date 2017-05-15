package textanalysis.ng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.languagetool.AnalyzedTokenReadings;
import org.languagetool.tagging.uk.UkrainianTagger;
import textanalysis.ng.token.TokenPosition;

public class GrammarMatch {

    public ParserGrammar matchedRule;

    // TODO
    // no need to use extra object here. just use tokens
    public ArrayList<ParserMatch> tokensMatched;

    public GrammarMatch(ParserGrammar matchedRule, ArrayList<ParserMatch> tokensMatched) {
        this.matchedRule = matchedRule;
        this.tokensMatched = tokensMatched;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        ParserToken previousToken = this.tokensMatched.get(0).token;

        sb.append(previousToken.value);

        for (int i = 1; i < this.tokensMatched.size(); i++) {
            ParserToken currentToken = this.tokensMatched.get(i).token;

            if ((previousToken.position.end + 1) == currentToken.position.start) {
                // TODO add exact number of elements. not one space
                sb.append(" ");
            }

            sb.append(currentToken.value);
            previousToken = currentToken;
        }

        return sb.toString();
    }

    public List<AnalyzedTokenReadings> getPosTaggerInfo() {

        // TODO make this more flexible
        UkrainianTagger ut = new UkrainianTagger();
        List<String> sentence = new ArrayList();

        this.tokensMatched.forEach(t -> {
            sentence.add(t.token.value);
        });

        List<AnalyzedTokenReadings> tag = null;
        try {
            tag = ut.tag(sentence);
        } catch (IOException ex) {
            Logger.getLogger(GrammarMatch.class.getName()).log(Level.SEVERE, null, ex);
        }

        return tag;
    }

    public TokenPosition getPosition() {

        if (this.tokensMatched.size() == 1) {
            return this.tokensMatched.get(0).token.position;
        } else {

            TokenPosition startTokenP = this.tokensMatched.get(0).token.position;
            TokenPosition endTokenP = this.tokensMatched.get(this.tokensMatched.size() - 1).token.position;

            return new TokenPosition(startTokenP.start, endTokenP.end);
        }

    }

}
