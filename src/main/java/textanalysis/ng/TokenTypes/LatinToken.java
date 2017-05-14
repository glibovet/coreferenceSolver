package textanalysis.ng.TokenTypes;

import java.util.ArrayList;
import textanalysis.ng.ParserToken;
import textanalysis.ng.token.TokenForm;
import textanalysis.ng.token.TokenPosition;
import textanalysis.ng.token.TokenType;

public class LatinToken extends TokenType {

    @Override
    public ParserToken transform(String value, int start, int end) {
        ArrayList<TokenForm> forms = new ArrayList();

        TokenForm form = new TokenForm(value);

        form.addGrammeme("LATIN");

        forms.add(form);

        return new ParserToken(value, new TokenPosition(start, end), forms);
    }

    public LatinToken() {
        super("latin", "[a-z][a-z\\'\\-]*");
    }

}
