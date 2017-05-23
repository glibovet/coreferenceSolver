/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tokenizer;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import org.junit.Test;
import textanalysis.ng.GrammarMatch;
import textanalysis.ng.Parser;
import textanalysis.ng.ParserTokenizer;
import textanalysis.ng.Rule.GrammarRuleI;
import textanalysis.ng.grammars.Brand;
import textanalysis.ng.grammars.GeoPlace;
import textanalysis.ng.grammars.Person;
import textanalysis.ng.preprocessors.DictionaryPreprocessor;
import textanalysis.ng.preprocessors.ParserTokenPreprocessor;

/**
 *
 * @author Lenovo
 */
public class TokenizerTest {

    @Test
    public void parseArticle() throws IOException {

        String sourcesDir = "C:\\course\\";
        String articleMappingPath = sourcesDir + "mappings975d7b41-9c46-44da-b07c-5ac7e03f3a9b.txt";

        List<String> articleLines = Files.readAllLines(Paths.get(articleMappingPath));

        int ind = 0;

        Parser entityParser = new Parser(
                new GrammarRuleI[][]{
                    Person.getGrammarRules(), // make this generic
                    Brand.getGrammarRules(),
                    GeoPlace.getGrammarRules()
                },
                new ParserTokenPreprocessor[]{
                    new DictionaryPreprocessor("Org/Education", "dictionaries/org_education.txt"),
                    new DictionaryPreprocessor("Org/Commercial", "dictionaries/org_commercial.txt"),
                    new DictionaryPreprocessor("Org/Abbr", "dictionaries/org_abbr.txt"),
                    new DictionaryPreprocessor("Org/Social", "dictionaries/org_social.txt"),
                    new DictionaryPreprocessor("Person/Position", "dictionaries/persons.txt")
                });

        for (String articleData : articleLines) {
            long start = System.currentTimeMillis();

            String[] exploded = articleData.split(" ", 2);

            String aContent = new String(Files.readAllBytes(Paths.get(sourcesDir + exploded[0] + ".txt")), Charset.forName("UTF-8"));

            List<GrammarMatch> tokens = entityParser.extract(aContent);
            int count = entityParser.extract(aContent).size();

            if (count > 0) {
//                System.out.println(exploded[0] + ":: -> " + count + " , took " + (System.currentTimeMillis() - start) + "ms. FT: " + tokens.get(0));
            }
//            if (ind++ % 200 == 0) {
//
//                
//            System.out.println(ind++);
//
//                
//                
//
////                List<ParserToken> tokens = tokenizer.transform(exploded[1]);
////                System.out.println(tokens.get(0));
//
////                System.out.println("`" + exploded[0] + "` -> `" + tokens.get(0) + "`");
//            }
        }

//        tokenizer.transform(text);
    }
}
