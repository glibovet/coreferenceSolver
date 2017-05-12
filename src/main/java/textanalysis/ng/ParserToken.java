package textanalysis.ng;

import java.util.HashMap;

public class ParserToken {

    public String value;
    public int position;
    public HashMap<String, String> forms;// TODO fix this to be the proper class instead of string 
    public NormalizationType normalization_type; // TODO fix this 
    public String interpretation; // TODO fix type

    /**
     * TODO fix this This is the copying constructor
     * @param c
     */
    public ParserToken(ParserToken c) {
        this.value = c.value;
        this.position = c.position;
        this.forms = c.forms; // deep copy please
        this.interpretation = c.interpretation;
        this.normalization_type = c.normalization_type;
    }

    public ParserToken(String value, int position, HashMap<String, String> forms, NormalizationType normalization_type, String interpretation) {
        this.value = value;
        this.position = position;
        this.forms = forms;
        this.normalization_type = normalization_type;
        this.interpretation = interpretation;
    }

}
