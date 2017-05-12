package textanalysis.ng.Rule.Labels;

import java.util.ArrayList;
import textanalysis.ng.ParserMatch;
import textanalysis.ng.ParserToken;
import textanalysis.ng.RuleLabel;

public class Gram extends RuleLabel{
    
    String value;
    
    public Gram(String name) {
       this.value = name;
    }

    // TODO fix
    @Override
    public boolean check(ParserToken token, ArrayList<ParserMatch> localStack) {
       return token.forms.containsKey(this.value);
    }
    
}
