package textanalysis.ng.grammars.person;

import textanalysis.ng.ParserGrammar;

import static textanalysis.ng.Rule.Label.*;
import static textanalysis.ng.GrammarRule.*;
import textanalysis.ng.Rule.SimpleGrammarRule;

public class PersonFull extends ParserGrammar {
//    Full = [
//        {
//            'labels': [
//                gram('Surn'),
//                gram_not('Abbr'),
//            ],
//            'normalization': NormalizationType.Inflected,
//            'interpretation': {
//                'attribute': PersonObject.Attributes.Lastname,
//            },
//        },
//        {
//            'labels': [
//                gram('Name'),
//                gram_not('Abbr'),
//                gnc_match(-1, solve_disambiguation=True),
//            ],
//            'normalization': NormalizationType.Inflected,
//            'interpretation': {
//                'attribute': PersonObject.Attributes.Firstname,
//            },
//        },
//        {
//            'labels': [
//                gram('Patr'),
//                gram_not('Abbr'),
//                gnc_match(-1, solve_disambiguation=True),
//            ],
//            'normalization': NormalizationType.Inflected,
//            'interpretation': {
//                'attribute': PersonObject.Attributes.Middlename,
//            },
//        },
//    ]

    public PersonFull() {

        this.name = "Person_Full";

        this.rules.add(simple(gram("lname"), not(gram("abbr"))));
        this.rules.add(simple(gram("fname"), not(gram("abbr"))));
        this.rules.add(simple(gram("patr"), not(gram("abbr")), gnc_match(-1)));

        this.rules.add(new SimpleGrammarRule(true)); // termination

    }

}
