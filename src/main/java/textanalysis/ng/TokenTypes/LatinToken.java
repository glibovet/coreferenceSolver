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

        boolean roman = value.trim().matches("^M{0,4}(CM|CD|D?C{0,3})(XC|XL|L?X{0,3})(IX|IV|V?I{0,3})$");

        if (roman) {
            form.addGrammeme("NUMBER");
            form.addGrammeme("ROMAN");
        }

        forms.add(form);

        return new ParserToken(value, new TokenPosition(start, end), forms);
    }

    public LatinToken() {
        super("latin", "[a-z][a-z\\'\\-]*");
    }

}
