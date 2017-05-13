package textanalysis.ng;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import textanalysis.ng.token.TokenForm;

public abstract class GramFeatureMatcher {

    abstract public boolean match(TokenForm lf, TokenForm rf);

    public List<Boolean> getMatchWithList(List<String> matchAgainst, HashSet<String> toBeMatched) {
        ArrayList<Boolean> result = new ArrayList();

        for (String val : matchAgainst) {
            if (toBeMatched.contains(val)) {
                result.add(true);
            } else {
                result.add(false);
            }
        }

        return result;
    }

}
