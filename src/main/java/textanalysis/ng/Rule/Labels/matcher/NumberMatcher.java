package textanalysis.ng.Rule.Labels.matcher;

import java.util.Arrays;
import java.util.List;
import textanalysis.ng.GramFeatureMatcher;
import textanalysis.ng.token.TokenForm;

public class NumberMatcher extends GramFeatureMatcher {

    List<String> numbers;

    public NumberMatcher() {
        this.numbers = Arrays.asList("p", "s", "np", "ns");
    }

    @Override
    public boolean match(TokenForm candidate, TokenForm obj) {

        List<Boolean> candM = this.getMatchWithList(this.numbers, candidate.grammemes);
        List<Boolean> objM = this.getMatchWithList(this.numbers, obj.grammemes);

        return candM.equals(objM);

    }

}
