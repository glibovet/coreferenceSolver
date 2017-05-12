package textanalysis.ng.TokenTypes;

import textanalysis.ng.ParserToken;
import textanalysis.ng.token.TokenType;

public class EmailToken extends TokenType {

    
    public EmailToken() {
        super("email","[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+");
    }
    
    @Override
    public ParserToken transform(String value, int start, int end) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
  

    
}
