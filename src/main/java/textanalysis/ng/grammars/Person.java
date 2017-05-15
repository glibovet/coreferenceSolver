package textanalysis.ng.grammars;

import static textanalysis.ng.helpers.GrammarRule.*;
import textanalysis.ng.Rule.GrammarRuleI;
import static textanalysis.ng.helpers.Label.*;
import textanalysis.ng.Rule.SimpleGrammarRule;

// this class should be named 
public class Person {

    public static GrammarRuleI[] getGrammarRules() {
        final String[] nobilityParticleDict = new String[]{
            "бе",
            "ла",
            "ле",
            "да",
            "де",
            "ді",
            "ван",
            "фон",
            "дель",
            "бен",
            "дю",
            "мак"
        };

        SimpleGrammarRule possibleLastname = new SimpleGrammarRule(
                not(gram("LATIN")),
                capitalized(),
                not(upper())
        );

        SimpleGrammarRule possibleLastnameSecond = new SimpleGrammarRule(
                not(gram("LATIN")),
                capitalized(),
                not(upper()),
                gnc_match(-1)
        );

        return new GrammarRuleI[]{
            //
            complex("Person_Full",
            simple(gram("lname"), not(gram("abbr"))),
            simple(gram("fname"), not(gram("abbr")), gnc_match(-1, true)),
            simple(gram("patr"), not(gram("abbr")), gnc_match(-1, true))
            ),
            //
            complex("Person_FullReversed",
            simple(gram("fname"), not(gram("abbr"))),
            simple(gram("patr"), not(gram("abbr")), gnc_match(-1, true)),
            simple(gram("lname"), not(gram("abbr")), gnc_match(-1, true))
            ),
            // феліпе родріго фернандес
            complex("Person_WithlatinMiddlename",
            simple(gram("fname")),
            repeatable(gram("fname"), gnc_match(-1, true)),
            simple(gram("lname"), gnc_match(-1, true), gnc_match(0, true))
            ),
            //
            complex("Person_InitialsAndLastname",
            simple(any(gram("fname"), gram("abbr"))),
            simple(gram("PUNCTUATION"), eq(".")),
            simple(any(gram("patr"), gram("abbr"), gnc_match(0, true))),
            simple(gram("PUNCTUATION"), eq(".")),
            simple(gram("lname"), not(gram("abbr")), gnc_match(0, true))
            ),
            //
            complex("Person_FirstnameAndLastname",
            simple(gram("fname"), not(gram("abbr"))),
            simple(gram("lname"), not(gram("abbr")), gnc_match(-1, true))
            ),
            //FirstnameAndLastnameWithQuotedNickname  ?
            //
            complex("Person_LastnameAndFirstname ",
            simple(gram("lname"), not(gram("abbr"))),
            simple(gram("fname"), not(gram("abbr")), gnc_match(-1, true))
            ),
            //FullReversedWithMiddlenameAsInitials 
            //LastnameAndInitials 
            //LastnameAndFirstnameAsInitials 
            //FirstnameAsInitialsAndLastname 
            complex("Person_FirstnameAndMiddlename",
            simple(gram("lname"), not(gram("abbr"))),
            simple(gram("patr"), not(gram("abbr")), gnc_match(-1, true))
            ),
            //
            complex("Person_Lastname",
            simple(gram("lname"), not(gram("abbr"), gram("prop")), capitalized(), not(gram("p")))
            ),
            //
            complex("Person_Middlename",
            simple(gram("patr"), not(gram("abbr")), capitalized(), not(gram("p")))
            ),
            //
            complex("Person_Firstname",
            simple(gram("fname"), not(gram("abbr")), capitalized(), not(gram("p")))
            ),
            complex("Person_WithNobilityParticle",
            simple(gram("fname"), not(gram("abbr"))),
            simple(dictionary(nobilityParticleDict)),
            simple(gram("lname"), not(gram("abbr")), gnc_match(0, true))
            ),
            /**
             * possible not proper results
             *
             */
            complex("PPerson_FirstLastName",
            simple(gram("fname"), not(gram("abbr")), capitalized(), any(gram("s"), gram("np"))),
            possibleLastname
            ),
            //
            complex("PPerson_InitAndLastname",
            simple(any(gram("fname"), gram("abbr"))),
            simple(gram("PUNCTUATION"), eq(".")),
            simple(any(gram("patr"), gram("abbr"), gnc_match(0, true))),
            simple(gram("PUNCTUATION"), eq(".")),
            possibleLastname
            ),
            //
            complex("PPerson_LastnameAndInitials",
            possibleLastname,
            simple(any(gram("fname"), gram("abbr"))),
            simple(gram("PUNCTUATION"), eq(".")),
            simple(any(gram("patr"), gram("abbr"), gnc_match(0, true))),
            simple(gram("PUNCTUATION"), eq("."))),
            //
            complex("PPerson_FirstnameAsInitialsAndLastname",
            simple(any(gram("fname"), gram("abbr"))),
            simple(gram("PUNCTUATION"), eq(".")),
            possibleLastname),
            //
            complex("PPerson_LastnameAndfirstnameAsInitials",
            possibleLastname,
            simple(any(gram("fname"), gram("abbr"))),
            simple(gram("PUNCTUATION"), eq("."))
            ),
            complex("PPerson_Full",
            skip(not(all(gram("PUNCTUATION"), eq(".")), gram("END-OF-LINE"))),
            possibleLastname,
            possibleLastnameSecond,
            simple(gram("patr"), not(gram("abbr")), capitalized(), not(gram("p")), gnc_match(-1))
            ),
            complex("PPerson_FirstLastUnknown",
            skip(not(all(gram("PUNCTUATION"), eq(".")), gram("END-OF-LINE"))),
            possibleLastname,
            possibleLastnameSecond
            ),
            complex("Person_Unnamed",
            repeatable(gram("adj")),
            simple(gram("noun"), gram("anim"), not(gram("p")), gnc_match(-1))
            ),
            //
            complex("Person_UnnamedPronoun",
            skip(not(all(gram("PUNCTUATION"), eq(",")))),
            simple(gram("&pron"), gram("noun")),
            skip(any(gram("verb"), gram("noun")))
            ),
            //
            complex("Person_UnnamedPosition",
            simple(gram("Person/Position"), not(gram("p")))
            )
        };
    }
}
