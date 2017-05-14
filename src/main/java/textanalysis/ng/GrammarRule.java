package textanalysis.ng;

import textanalysis.ng.Rule.ComplexGrammarRule;
import textanalysis.ng.Rule.GrammarRuleI;
import textanalysis.ng.Rule.SimpleGrammarRule;

public class GrammarRule {

    public static SimpleGrammarRule repeatable(RuleLabel... labels) {
        SimpleGrammarRule result = new SimpleGrammarRule(labels);

        result.repeatable = true;

        return result;
    }
    
    public static SimpleGrammarRule skip(RuleLabel... labels) {
        SimpleGrammarRule result = new SimpleGrammarRule(labels);

        result.skip = true;

        return result;
    }
    

    public static SimpleGrammarRule simple(RuleLabel... labels) {
        SimpleGrammarRule result = new SimpleGrammarRule(labels);

        return result;
    }

    public static ComplexGrammarRule complex(String name, GrammarRuleI ... rules) {
        ParserGrammar result = new ParserGrammar(name, rules);
        
        return result;
    }

}
