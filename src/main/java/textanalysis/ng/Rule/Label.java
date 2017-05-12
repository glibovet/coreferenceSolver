package textanalysis.ng.Rule;

import textanalysis.ng.Rule.Labels.Eq;
import textanalysis.ng.Rule.Labels.Gram;
import textanalysis.ng.RuleLabel;

public class Label {
    
    public static RuleLabel gram(String name) {
        return new Gram(name);
        
    }
    
    public static RuleLabel eq(String val) {
        return new Eq(val);
    }
    
}
