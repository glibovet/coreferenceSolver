package textanalysis.ng.TokenTypes;

import java.util.ArrayList;
import textanalysis.ng.ParserToken;
import textanalysis.ng.token.TokenForm;
import textanalysis.ng.token.TokenPosition;
import textanalysis.ng.token.TokenType;

public class EndOfLineToken extends TokenType {

    
    public EndOfLineToken() {        
        super("endofline","[\\n\\r]+");
    }
    
    @Override
    public ParserToken transform(String value, int start, int end) {
        
        ArrayList<TokenForm> forms = new ArrayList();

        // TODO parse grammatic values
        TokenForm form = new TokenForm("\n");

        form.addGrammeme("END-OF-LINE");

        forms.add(form);

        return new ParserToken("\n", new TokenPosition(start, end), forms);
    }
    
    
    
  

    
}
