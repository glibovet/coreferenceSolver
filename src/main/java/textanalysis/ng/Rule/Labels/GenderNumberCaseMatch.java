package textanalysis.ng.Rule.Labels;

import java.util.ArrayList;
import java.util.Arrays;
import textanalysis.ng.ParserMatch;
import textanalysis.ng.ParserToken;
import textanalysis.ng.Rule.Labels.matcher.*;
import textanalysis.ng.Rule.RuleHelper;
import textanalysis.ng.RuleLabel;

public class GenderNumberCaseMatch extends RuleLabel {

    private int pos;
    private boolean solveDisabiguation;
    private boolean match_all_disambiguation_forms = true;

    public GenderNumberCaseMatch(int pos) {
        this.pos = pos;
    }

    public GenderNumberCaseMatch(int stackelementPos, boolean solveDisambiguation) {
        this.pos = stackelementPos;
        this.solveDisabiguation = solveDisambiguation;
    }

    @Override
    public boolean check(ParserToken token, ArrayList<ParserMatch> localStack) {
        return RuleHelper.matchLabelWithDisambiguationResolving(this.pos, token, this.getLocalStackToTokens(localStack), Arrays.asList(
                new GenderMatcher(),
                new NumberMatcher(),
                new CaseMatcher()
        ), this.solveDisabiguation, this.match_all_disambiguation_forms);
    }

}
