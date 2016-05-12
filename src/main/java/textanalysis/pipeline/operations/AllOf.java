package textanalysis.pipeline.operations;

import textanalysis.pipeline.Rule;
import textanalysis.pipeline.RuleOperation;
import textanalysis.pipeline.Token;

public class AllOf extends RuleOperation {

    RuleOperation[] rules;

    public AllOf(RuleOperation... rules) {
        this.rules = rules;
    }

    @Override
    public boolean check(Token token) {
        for (RuleOperation curRule : this.rules) {
            if (!curRule.check(token)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String[] params() {
        return new String[]{};
    }

}
