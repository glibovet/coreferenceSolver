
import java.util.ArrayList;
import org.junit.Test;
import textanalysis.ng.GrammarMatch;
import textanalysis.ng.Parser;
import textanalysis.ng.ParserGrammar;
import textanalysis.ng.ParserMatch;
import textanalysis.ng.ParserTokenizer;
import static textanalysis.ng.Rule.Label.*;
import textanalysis.ng.Rule.SimpleGrammarRule;

/**
 *
 * @author Lenovo
 */
public class NGParserTest {

    private String getDefaultText() {
        return "Не те, що можливо. Це 100%, що так воно і буде. \n Щоразу, коли в країні щось подібне відбувається, кожну трагедію він обов'язково використовує для цього. Це, що називається, традиція. Він прийшов до влади на хвилі вибухів будинків, і багато хто говорить, що він безпосередньо брав участь в їхній організації. Дуже багато подібної інформації. Трагедія підводного човна \"Курськ\" допомогла йому встановити остаточний контроль над вільними медіа. Вибори губернаторів у нас були скасовані після теракту в Беслані і трагедії, коли загинули діти. Практично кожний новий наступ на демократичні свободи був пов'язаний із терористичними актами.";
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void matchSimplePerson() {

        String text = this.getDefaultText();

        ParserGrammar personGrammar = new ParserGrammar("Firstname_and_Lastname",
                new SimpleGrammarRule(gram("CYRILLIC")),
                new SimpleGrammarRule(gram("PUNCTUATION"), eq(",")),
                new SimpleGrammarRule(gram("CYRILLIC"))
        );

        ParserGrammar otherGrammar = new ParserGrammar("Firstname_and_other",
                new SimpleGrammarRule(gram("CYRILLIC")),
                new SimpleGrammarRule(gram("CYRILLIC")),
                new SimpleGrammarRule(gram("CYRILLIC"))
        );

        Parser p = new Parser(personGrammar, otherGrammar);

        for (GrammarMatch match : p.extract(text)) {

            System.out.println(match.matchedRule.getName() + ":");

            for (ParserMatch mToken : match.tokensMatched) {
                System.out.print(" " + mToken.token.value);
            }
            System.out.println("\n");
        }

        System.out.println("Matching done");

    }

    @Test
    public void textToTokens() {

        String text = this.getDefaultText();

        ParserTokenizer tokenizer = new ParserTokenizer();
        tokenizer.transform(text);

    }

}
