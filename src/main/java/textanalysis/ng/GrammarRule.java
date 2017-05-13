package textanalysis.ng;

import textanalysis.ng.Rule.SimpleGrammarRule;

public class GrammarRule {

    public static SimpleGrammarRule repeatable(RuleLabel... labels) {
        SimpleGrammarRule result = new SimpleGrammarRule(labels);

        result.repeatable = true;

        return result;
    }

    public static SimpleGrammarRule simple(RuleLabel... labels) {
        SimpleGrammarRule result = new SimpleGrammarRule(labels);

        return result;
    }

}
