package textanalysis.ng.Rule.Labels;

import java.util.ArrayList;
import textanalysis.ng.ParserMatch;
import textanalysis.ng.ParserToken;
import textanalysis.ng.RuleLabel;
import textanalysis.ng.token.TokenForm;

public class Dictionary extends RuleLabel {

    String[] dict;

    public Dictionary(String[] dict) {
        this.dict = dict;
    }

    @Override
    public boolean check(ParserToken token, ArrayList<ParserMatch> localStack) {
        for (TokenForm tf : token.forms) {
            for (String w : this.dict) {
                if (w.equals(tf.normalForm)) {
                    return true;
                }
            }
        }
        return false;
    }

}
