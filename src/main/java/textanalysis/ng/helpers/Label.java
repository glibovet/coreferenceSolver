package textanalysis.ng.helpers;

import java.util.ArrayList;
import textanalysis.ng.ParserMatch;
import textanalysis.ng.ParserToken;
import textanalysis.ng.Rule.GrammarRuleI;
import textanalysis.ng.Rule.Labels.All;
import textanalysis.ng.Rule.Labels.Any;
import textanalysis.ng.Rule.Labels.Capitalized;
import textanalysis.ng.Rule.Labels.Dictionary;
import textanalysis.ng.Rule.Labels.Eq;
import textanalysis.ng.Rule.Labels.GenderNumberCaseMatch;
import textanalysis.ng.Rule.Labels.Gram;
import textanalysis.ng.Rule.Labels.Not;
import textanalysis.ng.Rule.Labels.Upper;
import textanalysis.ng.RuleLabel;

public class Label {

    public static RuleLabel gram(String name) {
        return new Gram(name);

    }

    public static RuleLabel gnc_match(int stackelementPos) {
        return new GenderNumberCaseMatch(stackelementPos);
    }

    public static RuleLabel gnc_match(int stackelementPos, boolean solveDisambiguation) {
        return new GenderNumberCaseMatch(stackelementPos, solveDisambiguation);
    }

    public static RuleLabel eq(String val) {
        return new Eq(val);
    }

    public static RuleLabel capitalized() {
        return new Capitalized();
    }
    
    public static RuleLabel upper() {
        return new Upper();
    }
    
    public static RuleLabel any(RuleLabel... rules) {
        return new Any(rules);
    }
    
    public static RuleLabel not(RuleLabel... rules) {
        return new Not(rules);

    }

    public static RuleLabel all(RuleLabel... rules) {
        return new All(rules);
    }    
    
    public static RuleLabel dictionary(String ... dict) {
        return new Dictionary(dict);
    }
    
    
    public static RuleLabel longer(int x) {
        return new RuleLabel() {
            @Override
            public boolean check(ParserToken token, ArrayList<ParserMatch> localStack) {
               return (token.value.length() > x);
            }
            
        };
    }
    
    

}
