package textanalysis.pipeline.operations;

import textanalysis.pipeline.RuleOperation;
import textanalysis.pipeline.Token;

public class Lemma extends RuleOperation {

    String value;
    
    public Lemma(String v) {
        this.value = v;
    }

    @Override
    public boolean check(Token token) {
        return token.getToken().getLemma().equals(token);
    }

    @Override
    public String[] params() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
