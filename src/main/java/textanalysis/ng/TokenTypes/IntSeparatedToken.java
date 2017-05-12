package textanalysis.ng.TokenTypes;

import java.util.ArrayList;
import textanalysis.ng.ParserToken;
import textanalysis.ng.token.TokenForm;
import textanalysis.ng.token.TokenPosition;
import textanalysis.ng.token.TokenType;

public class IntSeparatedToken extends TokenType {

    @Override
    public ParserToken transform(String value, int start, int end) {

        ArrayList<TokenForm> forms = new ArrayList();

        // TODO parse grammatic values
        TokenForm form = new TokenForm(value.replace(" ",""));

        form.addGrammeme("NUMBER").addGrammeme("INT").addGrammeme("INT-SEPARATED");

        forms.add(form);

        return new ParserToken(value, new TokenPosition(start, end), forms);

    }

    public IntSeparatedToken() {
        super("intseparated", "[1-9]\\d*(\\s\\d{3})+");
    }

}
