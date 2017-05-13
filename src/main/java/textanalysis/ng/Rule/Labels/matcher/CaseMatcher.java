package textanalysis.ng.Rule.Labels.matcher;

import java.util.Arrays;
import java.util.List;
import textanalysis.ng.GramFeatureMatcher;
import textanalysis.ng.token.TokenForm;

public class CaseMatcher extends GramFeatureMatcher {

    private final List<String> cases;

    public CaseMatcher() {
        this.cases = Arrays.asList("v_naz", "v_rod", "v_dav", "v_zna", "v_oru", "v_mis", "v_kly", "nv");
    }

    @Override
    public boolean match(TokenForm candidate, TokenForm obj) {

        List<Boolean> candCases = this.getMatchWithList(this.cases, candidate.grammemes);
        List<Boolean> objCases = this.getMatchWithList(this.cases, obj.grammemes);

        return candCases.equals(objCases);
    }

}
