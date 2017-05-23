package textanalysis.ng.grammars;

import textanalysis.ng.Rule.GrammarRuleI;
import textanalysis.ng.Rule.SimpleGrammarRule;
import static textanalysis.ng.helpers.GrammarRule.*;
import static textanalysis.ng.helpers.Label.*;

public class Organisation {

    public static GrammarRuleI[] getGrammarRules() {

        final String[] regionTypeDictionary = new String[]{};

        GrammarRuleI NAMED_ORG_INITIALS_PREFIX_RULE = any(
                complex(
                simple(eq("ім")),
                simple())
        );

        return new GrammarRuleI[]{
            //
//            complex("Org_named",
//            any(simple(eq("ім")),
//            simple()))
//    

//),
//            //
//            complex("Place_complex",
//            simple(gram("adj"), dictionary(complexObjectPrefixDictionary)),
//            simple(gram("noun"), gram("Geo/Name"), gnc_match(-1, true))
//            ),
//            //
//            complex("Place_partial",
//            simple(gram("adj"), dictionary(partialObjectPrefixDictionary)),
//            simple(gram("noun"), gram("Geo/Name"), gnc_match(-1, true))
//            ),
//            //
//            complex("Place_republic",
//            simple(gram("adj"), capitalized()),
//            repeatable_optional(gram("adj"), gnc_match(-1, true)),
//            simple(dictionary("республіка", "федерація"), gnc_match(0, true))
//            ),
//            // states
//            complex("Place_states",
//            simple(any(gram("adj"), gram("adjp")), capitalized()),
//            repeatable_optional(gram("adj"), gnc_match(-1, true)),
//            simple(dictionary("штат", "емірат"), gnc_match(0, true)),
//            optional(gram("v_rod"))
//            ),
//            //
//            complex("Place_federal_district",
//            simple(gram("adj")),
//            simple(dictionary("федеральний"), gnc_match(-1, true)),
//            simple(dictionary("округ"), gnc_match(-1, true))
//            ),
//            //
//            complex("Place_simple",
//            simple(gram("Geo/Name"), capitalized())
//            ),
//            /**
//             * Streets
//             */
//            //               /* SHORTENED STREET */
//            //                    simple(dictionary(streetNameShortDictionary)),
//            //                    optional(eq("."))
//            //                /* SHORT STREET NAME */
//
//            complex("Place_Street_Adj_Full",
//            simple(gram("adj"), not(gram("abbr")), capitalized()), // should it be capitalized or not?
//            repeatable_optional(gram("adj"), not(gram("abbr")), gnc_match(-1, true)),
//            simple(dictionary(streetNameDictionary), gnc_match(-1, true))
//            ),
//            //
//            complex("Place_Street_Adj_Full_reversed",
//            simple(dictionary(streetNameDictionary)),
//            simple(gram("adj"), not(gram("abbr")), capitalized()), // should it be capitalized or not?
//            repeatable_optional(gram("adj"), not(gram("abbr")), gnc_match(-1, true))
//            ),
//            // shortened 
//            complex("Place_Street_Adj_Full_reversed_short",
//            simple(dictionary(streetNameShortDictionary)),
//            optional(eq(".")),
//            simple(gram("adj"), not(gram("abbr")), capitalized()), // should it be capitalized or not?
//            repeatable_optional(gram("adj"), not(gram("abbr")), gnc_match(-1, true))
//            ),
//            //
//            complex("Place_Street_Adj_Full_reversed_short",
//            simple(gram("adj"), not(gram("abbr")), capitalized()), // should it be capitalized or not?
//            repeatable_optional(gram("adj"), not(gram("abbr")), gnc_match(-1, true)),
//            simple(dictionary(streetNameShortDictionary)),
//            optional(eq("."))
//            ),
//            //
//            complex("Place_Street_Adj_Noun_Full",
//            simple(dictionary(streetNameDictionary)),
//            simple(gram("adj"), not(gram("abbr")), capitalized()), // should it be capitalized or not?
//            repeatable_optional(gram("adj"), not(gram("abbr")), gnc_match(-1, true)),
//            repeatable(gram("v_rod"), gnc_match(-1, true), not(gram("abbr")))
//            ),
//            //
//            complex("Place_Street_Adj_Noun_Full",
//            simple(dictionary(streetNameShortDictionary)),
//            optional(eq(".")),
//            simple(gram("adj"), not(gram("abbr")), capitalized()), // should it be capitalized or not?
//            repeatable_optional(gram("adj"), not(gram("abbr")), gnc_match(-1, true)),
//            repeatable(gram("v_rod"), gnc_match(-1, true), not(gram("abbr")))
//            ),
        };
    }

}
