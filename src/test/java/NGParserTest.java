
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.languagetool.AnalyzedToken;
import org.languagetool.AnalyzedTokenReadings;
import org.languagetool.tagging.uk.UkrainianTagger;

import textanalysis.ng.GrammarMatch;
import textanalysis.ng.Parser;
import textanalysis.ng.ParserGrammar;
import textanalysis.ng.ParserMatch;
import textanalysis.ng.ParserTokenizer;

import static textanalysis.ng.GrammarRule.*;
import static textanalysis.ng.Rule.Label.*;
import textanalysis.ng.token.TokenForm;

/**
 *
 * @author Lenovo
 */
public class NGParserTest {

//    @Test
    public void tagger() throws IOException {

        UkrainianTagger ut = new UkrainianTagger();
        List<String> sentence = new ArrayList();

        sentence.add("Степан");
        sentence.add("прийшов");

        List<AnalyzedTokenReadings> tag = ut.tag(sentence);

        for (AnalyzedTokenReadings tr : tag) {
            for (AnalyzedToken it : tr.getReadings()) {
                System.out.println(it.getPOSTag());
            }

            System.out.println("\n");

        }

    }

    private String getDefaultText() {

        return "Не те, що можливо. Це 100%, що так воно і буде. \n Щоразу, коли в країні щось подібне відбувається, кожну трагедію він обов'язково використовує для цього. Це, що називається, традиція. Він прийшов до влади на хвилі вибухів будинків, і багато хто говорить, що він безпосередньо брав участь в їхній організації. Дуже багато подібної інформації. Трагедія підводного човна \"Курськ\" допомогла йому встановити остаточний контроль над вільними медіа. Вибори губернаторів у нас були скасовані після теракту в Беслані і трагедії, коли загинули діти. Практично кожний новий наступ на демократичні свободи був пов'язаний із терористичними актами.";
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void matchSimplePerson() {

//        class TagsAndString {
//
//            String text;
//            List<AnalyzedTokenReadings> tags;
//
//            public TagsAndString(String text, List<AnalyzedTokenReadings> tags) {
//                this.text = text;
//                this.tags = tags;
//            }
//        }
        String text = this.getDefaultText();

        ParserGrammar sentenceGrammar = new ParserGrammar("Sentence",
                simple(capitalized()), // or not 
                repeatable(not(any(eq("."), eq("?"), eq("!")))),
                simple(gram("PUNCTUATION"), any(eq("."), eq("?"), eq("!")))
        );
        
        
        
        Parser sentenceParser = new Parser(sentenceGrammar);

        ArrayList<String> sentences = new ArrayList();

        for (GrammarMatch match : sentenceParser.extract(text)) {
//            sentences.add(match.toString());

            System.out.println(match);

            for (ParserMatch pm : match.tokensMatched) {
                if (pm.token.forms.size() > 1) {

//                    System.out.println(pm.token.value);
                    for (TokenForm tf : pm.token.forms) {
//                        System.out.println(tf.grammemes);
                    }
                }
            }

//          
        }

//        // pase sentences
//        for (TagsAndString it : sentences) {
//
//        }
        System.out.println("Matching done");

    }

    @Test
    public void textToTokens() {

        String text = this.getDefaultText();

        ParserTokenizer tokenizer = new ParserTokenizer();
        tokenizer.transform(text);

    }

}
