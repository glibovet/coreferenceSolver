package textanalysis.ng.grammars;

import textanalysis.ng.Rule.GrammarRuleI;
import static textanalysis.ng.helpers.GrammarRule.*;
import static textanalysis.ng.helpers.Label.*;

public class Brand {

    public static GrammarRuleI[] getGrammarRules() {

        return new GrammarRuleI[]{
            complex("Brand_Latin",
                simple(gram("LATIN"), capitalized()),
                optional(gram("INT"))
            ),
            //
            complex("Brand_WithConjunction",
                simple(gram("LATIN"), capitalized()),
                simple(any(eq("&"), eq("/"))),
                simple(gram("LATIN"), capitalized())
            ),
//            complex("Brand_Website",
//                simple(gram("LATIN")),
//                complex(
//                    simple(eq(".")),
//                    simple(gram("LATIN"))
//                )
//            )
        };

    }

}
