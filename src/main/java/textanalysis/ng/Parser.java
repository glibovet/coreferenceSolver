package textanalysis.ng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.languagetool.AnalyzedTokenReadings;
import org.languagetool.tagging.uk.UkrainianTagger;
import textanalysis.ng.Rule.GrammarRuleI;

public class Parser {

    private List<GrammarRuleI> grammars = new ArrayList();
    private ArrayList<ParserPipeline> pipelines = new ArrayList();
    private ParserTokenizer tokenizer;

    public Parser(GrammarRuleI ... grammars) {
        this.grammars = Arrays.asList(grammars);
        this.tokenizer = new ParserTokenizer();
    }

    public ArrayList<GrammarMatch> extract(String text) {
        return this.extract(text, true);
    }

    public ArrayList<GrammarMatch> extract(String text, boolean flattenResult) {

        ArrayList<GrammarMatch> bigResult = new ArrayList();

        ArrayList<ParserToken> stream = this.tokenizer.transform(text);

        /**
         * Get morfological data for whole list of tokens
         */
        // TODO make this flexible 
        // provide tagger as a parameter to parser
        // add posibility to skip tagging to boost performance
        List<AnalyzedTokenReadings> tags = null;
        
        try {
            UkrainianTagger ut = new UkrainianTagger();
            List<String> sentence = new ArrayList();

            stream.forEach(t -> {
                sentence.add(t.value);
            });

            tags = ut.tag(sentence);
        } catch (IOException ex) {
            Logger.getLogger(GrammarMatch.class.getName()).log(Level.SEVERE, null, ex);
        }

        // not yet implemented
//        for (ParserPipeline pipeline : this.pipelines) {
////            pipeline
//        }

        int currentIndex = 0;
        for (ParserToken token : stream) {
            
            AnalyzedTokenReadings forms = tags.get(currentIndex);
            
            if (forms != null) {
                token.setRawForms(forms);                
//                if (token.value.equals("він")) {
//                    System.out.println(forms);
//                }
            }
            currentIndex++;
            
            for (GrammarRuleI grule : this.grammars) {
                
                ParserGrammar grammar = (ParserGrammar)grule;
                
                boolean recheck = grammar.shift(token);
                ArrayList<ParserMatch> match = grammar.reduce();

                if (match.size() > 0) {
                    bigResult.add(new GrammarMatch(grammar, match));

                }

                if (recheck) {
                    grammar.shift(token);
                    match = grammar.reduce();

                    if (match.size() > 0) {
                        bigResult.add(new GrammarMatch(grammar, match));
                    }

                }

            }
        }

        for (GrammarRuleI grule : this.grammars) {
            
            ParserGrammar grammar = (ParserGrammar)grule;
            
            ArrayList<ParserMatch> match = grammar.reduce(true);
            if (match.size() > 0) {
                bigResult.add(new GrammarMatch(grammar, match));

            }
            grammar.reset();
        }

        return bigResult;
//        throw new RuntimeException("This should not be happen!");

    }

}
