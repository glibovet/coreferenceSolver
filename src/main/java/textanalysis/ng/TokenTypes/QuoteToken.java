package textanalysis.ng.TokenTypes;

import java.util.ArrayList;
import textanalysis.ng.ParserToken;
import textanalysis.ng.token.TokenForm;
import textanalysis.ng.token.TokenPosition;
import textanalysis.ng.token.TokenType;

public class QuoteToken extends TokenType {

    @Override
    public ParserToken transform(String value, int start, int end) {

        ArrayList<TokenForm> forms = new ArrayList();

        TokenForm form = new TokenForm(value).addGrammeme("QUOTE");

        if (value.equals("«") || value.equals("„")) {
            form.addGrammeme("L-QUOTE");
        } else if (value.equals("\"") || value.equals("'")) {
            form.addGrammeme("G-QUOTE");
        } else {
            form.addGrammeme("R-QUOTE");
        }

        forms.add(form);

        return new ParserToken(value, new TokenPosition(start, end), forms);

    }
    // TODO fix the regex. now it matches `пов'язаний`
    public QuoteToken() {
        super("quote", "[\\\"\\'\\«\\»\\„\\“]");
    }

}
