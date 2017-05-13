package textanalysis.ng;

import java.util.ArrayList;
import java.util.List;

abstract public class RuleLabel {

    abstract public boolean check(ParserToken token, ArrayList<ParserMatch> localStack);

    protected List<ParserToken> getLocalStackToTokens(ArrayList<ParserMatch> localStack) {
        List<ParserToken> stack = new ArrayList();
        for (ParserMatch it : localStack) {
            stack.add(it.token);
        }
        return stack;
    }

}
