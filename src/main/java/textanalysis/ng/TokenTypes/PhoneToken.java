package textanalysis.ng.TokenTypes;

import java.util.ArrayList;
import textanalysis.ng.ParserToken;
import textanalysis.ng.token.TokenForm;
import textanalysis.ng.token.TokenPosition;
import textanalysis.ng.token.TokenType;

public class PhoneToken extends TokenType {

    public PhoneToken() {
        super("phone", "(\\+)?([-\\s_()]?\\d[-\\s_()]?){10,14}");
    }

    @Override
    public ParserToken transform(String value, int start, int end) {

        ArrayList<TokenForm> forms = new ArrayList();

        TokenForm form = new TokenForm(value);

        form.addGrammeme("PHONE");

        forms.add(form);

        return new ParserToken(value, new TokenPosition(start, end), forms);

    }

}
