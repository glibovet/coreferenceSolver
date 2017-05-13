package textanalysis.ng.Rule.Labels;

import java.util.ArrayList;
import textanalysis.ng.ParserMatch;
import textanalysis.ng.ParserToken;
import textanalysis.ng.RuleLabel;

public class Eq extends RuleLabel {
    
    String val;

    public Eq(String val) {
        this.val = val;
    }
    
    @Override
    public boolean check(ParserToken token, ArrayList<ParserMatch> localStack) {
        return token.value.equals(this.val);
    }

}
