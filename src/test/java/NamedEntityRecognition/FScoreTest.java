package NamedEntityRecognition;

import org.junit.Test;
import textanalysis.ng.Parser;
import textanalysis.ng.Rule.GrammarRuleI;
import textanalysis.ng.grammars.Brand;
import textanalysis.ng.grammars.Person;
import static textanalysis.ng.helpers.GrammarRule.simple;
import static textanalysis.ng.helpers.Label.gram;
import static textanalysis.ng.helpers.Label.not;
import textanalysis.ng.preprocessors.DictionaryPreprocessor;
import textanalysis.ng.preprocessors.ParserTokenPreprocessor;

public class FScoreTest {

    @Test
    public void calculateFscore() {

        Parser entityParser = new textanalysis.ng.Parser(
                new GrammarRuleI[][]{
                    Person.getGrammarRules(), // make this generic
                    Brand.getGrammarRules()
                },
                new ParserTokenPreprocessor[]{
                    new DictionaryPreprocessor("Org/Education", "dictionaries/org_education.txt", simple(not(gram("verb")))),
                    new DictionaryPreprocessor("Person/Position", "dictionaries/persons.txt", simple(not(gram("verb"))))
                });

    }
}
