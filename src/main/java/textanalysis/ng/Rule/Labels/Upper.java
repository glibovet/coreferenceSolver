package textanalysis.ng.Rule.Labels;

import java.util.ArrayList;
import textanalysis.ng.ParserMatch;
import textanalysis.ng.ParserToken;
import textanalysis.ng.RuleLabel;

public class Upper extends RuleLabel {

    @Override
    public boolean check(ParserToken token, ArrayList<ParserMatch> localStack) {
        return token.value.equals(token.value.toUpperCase());
    }

}
