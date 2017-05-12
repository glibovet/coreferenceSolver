/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import org.junit.Test;
import textanalysis.ng.Parser;
import textanalysis.ng.ParserGrammar;
import textanalysis.ng.ParserMatch;
import static textanalysis.ng.Rule.Label.*;
import textanalysis.ng.Rule.SimpleGrammarRule;

/**
 *
 * @author Lenovo
 */
public class NGParserTest {

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void hello() {

        String text = "Лев Толстой написал роман «Война и Мир»";

        ParserGrammar personGrammar = new ParserGrammar("Firstname_and_Lastname",
                new SimpleGrammarRule(gram("Name")),
                new SimpleGrammarRule(eq("Surn"))
        );

        Parser p = new Parser(personGrammar);

        for (ParserMatch match : p.extract(text)) {
            System.out.println(match.token + " -> "+ match.index);
        }
    }
}
