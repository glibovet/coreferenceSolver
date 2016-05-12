package textanalysis;

import java.util.ArrayList;
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

    public String toStringAdvanced() {
        StringBuilder result = new StringBuilder();
        for (Token it : this.Tokens) {
            result.append(" " + it.getToken().getToken()+"{"+it.getTags()+"}");
        }
        return result.toString();
    }

    public void clear() {
        this.Tokens.clear();
    }

}
