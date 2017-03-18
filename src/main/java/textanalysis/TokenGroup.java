package textanalysis;

import java.util.ArrayList;
import textanalysis.parser.FrequencyCalculator;
import textanalysis.pipeline.Token;

public class TokenGroup {

    public ArrayList<Token> Tokens = new ArrayList<>();

    public void add(Token t) {
        this.Tokens.add(t);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Token it : this.Tokens) {
            result.append(" " + it.getToken().getToken());
        }
        return result.toString();
    }

    public String withTfIdf(FrequencyCalculator fc) {
        StringBuilder result = new StringBuilder();
        for (Token it : this.Tokens) {
            
               String lemma = it.getToken().getLemma();
                if (lemma == null) {
                    lemma = it.getToken().getToken();
                }
            
            double tfidf = fc.calculateTfIdf(lemma);
            result.append("<span class='term' data-tfidf='" + tfidf + "'> " + it.getToken().getToken() + "</span>");
        }
        return result.toString();
    }

    public String toStringAdvanced() {
        StringBuilder result = new StringBuilder();
        for (Token it : this.Tokens) {
            result.append(" " + it.getToken().getToken() + "{" + it.getTags() + "}");
        }
        return result.toString();
    }

    public void clear() {
        this.Tokens.clear();
    }

}