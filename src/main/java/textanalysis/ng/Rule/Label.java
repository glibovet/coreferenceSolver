package textanalysis.ng.Rule;

import textanalysis.ng.Rule.Labels.Any;
import textanalysis.ng.Rule.Labels.Capitalized;
import textanalysis.ng.Rule.Labels.Eq;
import textanalysis.ng.Rule.Labels.GenderNumberCaseMatch;
import textanalysis.ng.Rule.Labels.Gram;
import textanalysis.ng.Rule.Labels.Not;
import textanalysis.ng.RuleLabel;


public class Label {
    
    public static RuleLabel gram(String name) {
        return new Gram(name);
        
    }
    
    public static RuleLabel gnc_match(int stackelementPos) {
        return new GenderNumberCaseMatch(stackelementPos);
    }
    
    
    public static RuleLabel eq(String val) {
        return new Eq(val);
    }
    
    public static RuleLabel capitalized() {
        return new Capitalized();
    }    
    
    public static RuleLabel any(RuleLabel ... rules) {
        return new Any(rules);
    }
    
     public static RuleLabel not(RuleLabel ... rules) {
         return new Not(rules);
     
     }
     
    
    
}
