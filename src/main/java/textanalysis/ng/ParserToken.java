package textanalysis.ng;

import java.util.ArrayList;
import textanalysis.ng.token.TokenForm;
import textanalysis.ng.token.TokenPosition;

public class ParserToken {

    public String value;
    public TokenPosition position;
    public ArrayList<TokenForm> forms = new ArrayList();// TODO fix this to be the proper class instead of string 
    public NormalizationType normalization_type = null; // TODO fix this 
    public String interpretation = null; // TODO fix type

    public ParserToken() {
    }

    /**
     * TODO fix this This is the copying constructor
     *
     * @param c
     */
    public ParserToken(ParserToken c) {
        this.value = c.value;
        this.position = c.position;
        this.forms = c.forms; // deep copy please
        this.interpretation = c.interpretation;
        this.normalization_type = c.normalization_type;
    }

    public ParserToken(String value, TokenPosition position, ArrayList<TokenForm> forms) {
        this.value = value;
        this.position = position;
        this.forms = forms;
//        this.normalization_type = normalization_type;
//        this.interpretation = interpretation;
    }

}
