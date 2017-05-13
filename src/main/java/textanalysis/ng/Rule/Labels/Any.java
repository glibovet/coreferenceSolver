package textanalysis.ng.Rule.Labels;

import java.util.ArrayList;
import textanalysis.ng.ParserMatch;
import textanalysis.ng.ParserToken;
import textanalysis.ng.RuleLabel;

public class Any extends RuleLabel {

    RuleLabel[] val;

    public Any(RuleLabel... rules) {
        this.val = rules;
    }

    @Override
    public boolean check(ParserToken token, ArrayList<ParserMatch> localStack) {
        for (RuleLabel r : this.val) {
            if (r.check(token, localStack)) {
                return true;
            }
        }
        return false;
    }

}
