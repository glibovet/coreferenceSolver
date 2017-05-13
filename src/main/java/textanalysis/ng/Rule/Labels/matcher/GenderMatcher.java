package textanalysis.ng.Rule.Labels.matcher;

import java.util.Arrays;
import java.util.List;
import textanalysis.ng.GramFeatureMatcher;
import textanalysis.ng.token.TokenForm;

public class GenderMatcher extends GramFeatureMatcher {

    List<String> genders;

    public GenderMatcher() {
        // TODO should be used some proper naming for all languages
        this.genders = Arrays.asList("m", "f", "n");
    }

    @Override
    public boolean match(TokenForm candidate, TokenForm obj) {

        List<Boolean> candGenders = this.getMatchWithList(this.genders, candidate.grammemes);
        List<Boolean> objGenders = this.getMatchWithList(this.genders, obj.grammemes);

        return candGenders.equals(objGenders);
    }

}
