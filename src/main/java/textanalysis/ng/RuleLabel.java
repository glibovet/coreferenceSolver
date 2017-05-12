package textanalysis.ng;

import java.util.ArrayList;

abstract public class RuleLabel {

    abstract public boolean check(ParserToken token, ArrayList<ParserMatch> localStack);

}
