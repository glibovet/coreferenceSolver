package textanalysis.pipeline.operations;

import textanalysis.pipeline.RuleOperation;
import textanalysis.pipeline.Token;

public class Any extends RuleOperation {

    @Override
    public boolean check(Token token) {
      return true;
    }

    @Override
    public String[] params() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    

}
