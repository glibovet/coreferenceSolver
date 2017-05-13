package textanalysis.ng.Rule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import textanalysis.ng.GramFeatureMatcher;
import textanalysis.ng.ParserToken;
import textanalysis.ng.token.TokenForm;

public class RuleHelper {

    public static boolean matchLabelWithDisambiguationResolving(int index, ParserToken token, List<ParserToken> stack, ArrayList<GramFeatureMatcher> match_labels) {
        return matchLabelWithDisambiguationResolving(index, token, stack, match_labels, false, true);
    }

    public static boolean matchLabelWithDisambiguationResolving(int index, ParserToken token, List<ParserToken> stack, GramFeatureMatcher... matchers) {
        List<GramFeatureMatcher> match_labels = Arrays.asList(matchers);

        return matchLabelWithDisambiguationResolving(index, token, stack, match_labels, false, true);
    }

    public static boolean matchLabelWithDisambiguationResolving(int index, ParserToken token, List<ParserToken> stack, List<GramFeatureMatcher> match_labels, boolean solve_disambiguation, boolean match_all_disambiguation_forms) {
        
        index = stack.size() + index;
        
        ArrayList<TokenForm> case_forms = new ArrayList();
        ArrayList<TokenForm> candidate_forms = new ArrayList();

        for (TokenForm candidate_form : token.forms) {
            if (!match_all_disambiguation_forms) {
                if (case_forms.size() > 0 && candidate_forms.size() > 0) {
                    break;
                }
            }

            for (TokenForm case_form : stack.get(index).forms) {
                boolean match = false;

                int falses = 0;

                for (GramFeatureMatcher grammarMatcher : match_labels) {
                    if (!grammarMatcher.match(candidate_form, case_form)) {
                        falses++;
                        break;
                    }
                }

                if (falses == 0) {
                    match = true;
                }

                if (match) {
                    if (solve_disambiguation) {
                        if (!case_forms.contains(case_form)) {
                            case_forms.add(case_form);
                        }

                        if (!candidate_forms.contains(candidate_form)) {
                            candidate_forms.add(candidate_form);
                        }

                        if (!match_all_disambiguation_forms) {
                            break;
                        }

                    } else {
                        return true;
                    }
                }
            }
        }

        if (solve_disambiguation && (!case_forms.isEmpty() && !candidate_forms.isEmpty())) {
            token.forms = candidate_forms;
            stack.get(index).forms = case_forms;
            return true;
        }
        return false;
    }

}
