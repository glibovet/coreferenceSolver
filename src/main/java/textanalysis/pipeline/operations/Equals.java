package textanalysis.pipeline.operations;

import textanalysis.pipeline.RuleOperation;
import textanalysis.pipeline.Token;

public class Equals extends RuleOperation {
    
    String value;
    
    public Equals(String value) {
        this.value = value;
    }
    
    @Override
    public boolean check(Token token) {
        return token.getToken().getToken().equals(this.value);
    }

    @Override
    public String[] params() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
