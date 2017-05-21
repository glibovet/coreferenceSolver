package textanalysis.ng.grammars;

import textanalysis.ng.Rule.GrammarRuleI;
import static textanalysis.ng.helpers.GrammarRule.*;
import static textanalysis.ng.helpers.Label.*;

public class Brand {

    public static GrammarRuleI[] getGrammarRules() {

        return new GrammarRuleI[]{
//            complex("Brand_Latin",
//                repeatable(gram("LATIN"), capitalized()),
//                optional(any(eq("and"),eq("&"))),
//                optional(gram("LATIN"), capitalized()),
//                optional(gram("INT"))
//            ),
            //
            complex("Brand_Website",
                simple(gram("LATIN")),
                repeatable( // not works properly
                    simple(eq(".")),
                    simple(gram("LATIN"))
//                    optional(eq("-")),
//                    optional(gram("LATIN"))
                )
            )
        };

    }

}
