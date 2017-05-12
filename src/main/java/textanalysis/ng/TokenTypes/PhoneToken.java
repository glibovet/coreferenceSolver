package textanalysis.ng.TokenTypes;

import textanalysis.ng.ParserToken;
import textanalysis.ng.token.TokenType;

public class PhoneToken extends TokenType {

    
    public PhoneToken() {
        super("phone","(\\+)?([-\\s_()]?\\d[-\\s_()]?){10,14}");
    }
    
    @Override
    public ParserToken transform(String value, int start, int end) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
  

    
}
