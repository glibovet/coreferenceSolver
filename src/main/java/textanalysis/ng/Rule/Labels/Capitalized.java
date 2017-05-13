package textanalysis.ng.Rule.Labels;

import java.util.ArrayList;
import textanalysis.ng.ParserMatch;
import textanalysis.ng.ParserToken;
import textanalysis.ng.RuleLabel;

public class Capitalized extends RuleLabel {

    @Override
    public boolean check(ParserToken token, ArrayList<ParserMatch> localStack) {
        return Character.isUpperCase(token.value.charAt(0));
    }

}
