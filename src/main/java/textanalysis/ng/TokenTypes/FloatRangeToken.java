package textanalysis.ng.TokenTypes;

import textanalysis.ng.ParserToken;
import textanalysis.ng.token.TokenType;

public class FloatRangeToken extends TokenType {

    
    public FloatRangeToken() {
        super("floatrange","[\\d]+[\\.\\,][\\d]+\\s*?[\\-\\—]\\s*?[\\d]+[\\.\\,][\\d]+");
    }
    
    @Override
    public ParserToken transform(String value, int start, int end) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
  

    
}
