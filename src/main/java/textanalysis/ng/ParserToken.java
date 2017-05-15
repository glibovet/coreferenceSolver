package textanalysis.ng;

import java.util.ArrayList;
import java.util.List;
import org.languagetool.AnalyzedToken;
import org.languagetool.AnalyzedTokenReadings;
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

    public void setRawForms(AnalyzedTokenReadings forms) {

        int index = 0;
        for (AnalyzedToken tokenForm : forms.getReadings()) {

            String[] grammemes = new String[]{};
            String tags = tokenForm.getPOSTag();
            if (tags != null) {
                grammemes = tags.split(":");
            }

            if (index == 0 && this.forms.size() > 0) {
                this.forms.get(0).normalForm = tokenForm.getLemma();

                // TODO optimize to use cons var
                for (String gr : grammemes) {
                    this.forms.get(0).grammemes.add(gr);
                }

            } else {
                this.forms.add(new TokenForm(tokenForm.getLemma(), grammemes));
            }

            index++;

        }
    }
    
    public String toString() {
        return "[Token("+value+":{"+this.forms+"})]";
    }
    

}
