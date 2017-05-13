package textanalysis.ng.TokenTypes;

import java.util.ArrayList;
import textanalysis.ng.ParserToken;
import textanalysis.ng.token.TokenForm;
import textanalysis.ng.token.TokenPosition;
import textanalysis.ng.token.TokenType;

public class CyrillicToken extends TokenType {

    public CyrillicToken() {
        // и - can't be the first literal
        super("cyrillic", "[а-яєёїґі][а-яёєїґі\\'\\-]*");
    }

    @Override
    // TODO add here one more param - forms
    public ParserToken transform(String value, int start, int end) {
        ParserToken pt = new ParserToken();

        pt.value = value;
        pt.position = new TokenPosition(start, end);

        // TODO parse grammatic values
        // get the morfological forms 
        ArrayList<TokenForm> forms = new ArrayList();

        TokenForm form = new TokenForm(value);

        form.addGrammeme("CYRILLIC");

        forms.add(form);

        pt.forms = forms;

        return pt;
    }

}
