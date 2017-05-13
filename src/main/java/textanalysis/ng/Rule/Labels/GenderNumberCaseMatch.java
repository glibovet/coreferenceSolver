package textanalysis.ng.Rule.Labels;

import java.util.ArrayList;
import textanalysis.ng.ParserMatch;
import textanalysis.ng.ParserToken;
import textanalysis.ng.Rule.Labels.matcher.CaseMatcher;
import textanalysis.ng.Rule.Labels.matcher.GenderMatcher;
import textanalysis.ng.Rule.Labels.matcher.NumberMatcher;
import textanalysis.ng.Rule.RuleHelper;
import textanalysis.ng.RuleLabel;

public class GenderNumberCaseMatch extends RuleLabel {

    int pos;

    public GenderNumberCaseMatch(int pos) {
        this.pos = pos;
    }

    @Override
    public boolean check(ParserToken token, ArrayList<ParserMatch> localStack) {
        return RuleHelper.matchLabelWithDisambiguationResolving(this.pos, token, this.getLocalStackToTokens(localStack),
                new GenderMatcher(),
                new NumberMatcher(),
                new CaseMatcher()
        );
    }

}
