package textanalysis.ng.helpers;

import com.trg.helpers.StringHelper;
import textanalysis.ng.ParserGrammar;
import textanalysis.ng.Rule.ComplexGrammarRule;
import textanalysis.ng.Rule.GrammarRuleI;
import textanalysis.ng.Rule.SimpleGrammarRule;
import textanalysis.ng.RuleLabel;

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

    public static ComplexGrammarRule complex(String name, GrammarRuleI... rules) {
        ParserGrammar result = new ParserGrammar(name, rules);

        return result;
    }
    
    /**
     * Anonymous complex rule. for use in nested complex rules
     * @param rules
     * @return 
     */
     public static ComplexGrammarRule complex(GrammarRuleI... rules) {
         
         String anonymousName = StringHelper.random(4);
         
        ParserGrammar result = new ParserGrammar(anonymousName, rules);

        return result;
    }
    

    public static SimpleGrammarRule optional(RuleLabel... labels) {
        SimpleGrammarRule result = new SimpleGrammarRule(labels);

        result.optional = true;

        return result;
    }

}
