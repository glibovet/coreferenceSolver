package textanalysis.pipeline;

import java.util.ArrayList;
import java.util.List;

import org.languagetool.AnalyzedSentence;
import org.languagetool.AnalyzedToken;
import org.languagetool.AnalyzedTokenReadings;

public class TokenBag {

    public int length() {
        return this.tokens.size();
    }

    private ArrayList<Token> tokens = new ArrayList<>();

    public TokenBag(List<AnalyzedSentence> sentences) {
//        int sentenceId = 0;

        for (AnalyzedSentence match : sentences) {
//            sentenceId++;
            for (AnalyzedTokenReadings it : match.getTokensWithoutWhitespace()) {
                AnalyzedToken token = it.getReadings().get(0);
                Token tkn = new Token(token);

                int index = 0;
                for (AnalyzedToken iToken : it.getReadings()) {
                    if (index == 0) {
                        continue;
                    }
                    tkn.getTags().add(iToken.getPOSTag());
                }

                tokens.add(tkn);
            }

        }
    }

    public ArrayList<Token> getTokens() {
        return tokens;
    }

}
