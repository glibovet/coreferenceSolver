package textanalysis.ng.TokenTypes;

import textanalysis.ng.ParserToken;
import textanalysis.ng.token.TokenType;

public class IntRangeToken extends TokenType {

    @Override
    public ParserToken transform(String value, int start, int end) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public IntRangeToken() {
        super("intrange","\\d+\\s*?[\\-\\—]\\s*?\\d+");
    }
    
    

}
