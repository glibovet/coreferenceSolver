package textanalysis.ng.Rule.Labels;

import java.util.ArrayList;
import textanalysis.ng.ParserMatch;
import textanalysis.ng.ParserToken;
import textanalysis.ng.RuleLabel;
import textanalysis.ng.token.TokenForm;

public class Gram extends RuleLabel {

    String value;

    public Gram(String name) {
        this.value = name;
    }

    // TODO fix
    @Override
    public boolean check(ParserToken token, ArrayList<ParserMatch> localStack) {
        for (TokenForm tf : token.forms) {
            if (tf.grammemes.contains(this.value)) {
                return true;
            }
        }
        return false;
    }

}
