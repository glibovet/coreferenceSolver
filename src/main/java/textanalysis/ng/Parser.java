package textanalysis.ng;

import textanalysis.ng.preprocessors.ParserTokenPreprocessor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.languagetool.AnalyzedTokenReadings;
import org.languagetool.tagging.uk.UkrainianTagger;
import textanalysis.ng.Rule.GrammarRuleI;
import textanalysis.ng.token.TokenPosition;

public class Parser {

    private final List<GrammarRuleI> grammars = new ArrayList();
    private final ArrayList<ParserTokenPreprocessor> preprocessors = new ArrayList();
    private ParserTokenizer tokenizer = ParserTokenizer.defaultTokenizer();
    private TokenOutputConvertor tokenOutputConvertor;

    public Parser(GrammarRuleI... grammars) {
        this.grammars.addAll(Arrays.asList(grammars));
    }

    public Parser(GrammarRuleI[] rules, ParserTokenPreprocessor[] pipelines) {
        this.grammars.addAll(Arrays.asList(rules));
        this.preprocessors.addAll(Arrays.asList(pipelines));
    }

    public Parser withTokenVisitor(TokenOutputConvertor tv) {
        this.tokenOutputConvertor = tv;
        return this;
    }

    public ArrayList<GrammarMatch> extract(String text) {

        ArrayList<GrammarMatch> bigResult = new ArrayList();

        List<ParserToken> stream = this.tokenizer.transform(text);

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

        // merge pos tags with ParserTokens
        int currentIndex = 0;
        for (ParserToken token : stream) {
            AnalyzedTokenReadings forms = tags.get(currentIndex);

            if (forms != null) {
                token.setRawForms(forms);
            }
            currentIndex++;
        }

        // preprocess tokens
        for (ParserTokenPreprocessor preprocessor : this.preprocessors) {
            stream = preprocessor.proceed(stream);
        }

        // match with grammars
//        int tokenIndex = 0;
//        int lastInsertTokenIndex = 0;
//
//        ArrayList<ParserMatch> notMatchedStack = new ArrayList();
        for (ParserToken token : stream) {
//            tokenIndex++;
            for (GrammarRuleI grule : this.grammars) {

                ParserGrammar grammar = (ParserGrammar) grule;

                boolean recheck = grammar.shift(token);
                ArrayList<ParserMatch> match = grammar.reduce();

                if (match.size() > 0) {

                    // empty not matched stack and add it to result
//                    if (notMatchedStack.size() > 0) {
//                        bigResult.add(new GrammarMatch(null, notMatchedStack));
//                        notMatchedStack = new ArrayList();
//                    }
                    bigResult.add(new GrammarMatch(grammar, match));
                }
//                else {
//
//                    if (lastInsertTokenIndex < tokenIndex) {
//                        notMatchedStack.add(new ParserMatch(0, token));
//                        lastInsertTokenIndex++;
//                    }
//                }

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

            ParserGrammar grammar = (ParserGrammar) grule;

            ArrayList<ParserMatch> match = grammar.reduce(true);
            if (match.size() > 0) {
                bigResult.add(new GrammarMatch(grammar, match));

            }
            grammar.reset();
        }

        return bigResult;
//        throw new RuntimeException("This should not be happen!");

    }

    public List<GrammarMatch> resolveMatches(ArrayList<GrammarMatch> matches) {

        List<GrammarMatch> result = new ArrayList();

//        def resolve_matches(self, matches, strict=True):
//      sort matches by tokens count in decreasing order
        Collections.sort(matches, (a, b) -> {
            return b.tokensMatched.size() - a.tokensMatched.size();
        });
        
        // i don't actually if there is need to create this class
        class MatchAndPos {

            TokenPosition pos;
            GrammarMatch match;

            public MatchAndPos(TokenPosition pos, GrammarMatch match) {
                this.pos = pos;
                this.match = match;
            }

        }

        ArrayList<MatchAndPos> positions = new ArrayList();

        for (GrammarMatch m : matches) {
            positions.add(new MatchAndPos(m.getPosition(),m));
        }

        Collections.sort(positions, (a, b) -> {
            return a.pos.start - b.pos.start;
        });

        TokenPosition currentSpan = null;
        GrammarMatch gm = null;
            
        
        int i = 0;
        boolean allAdded = false;
        for (MatchAndPos tp : positions) {
            i++;

            if (currentSpan == null) {
                currentSpan = new TokenPosition(tp.pos);
                gm = tp.match;
//                System.out.println(" cSpan = " + tp);
            } else {

                if (tp.pos.start >= currentSpan.end) {
//                    System.out.println(" cSpan => " + i + "|" + tp + " , " + tp.pos.start + " > " + currentSpan.end);

                    result.add(gm);

                    currentSpan = new TokenPosition(tp.pos);
                    gm = tp.match;
                    if (i == positions.size()) {
                        allAdded = true;
                    }    
                } else if (tp.pos.start == currentSpan.start) { // the same start
                    if (tp.pos.end <= currentSpan.end) {
//                        System.out.println("CONTAIN or SAME(" + tp + ")");
                    } else {
                        System.out.println("CURRENT IS BIGGER");
                    }
                } else {
                    if (tp.pos.end <= currentSpan.end) {
//                        System.out.println(">>> FALSE not(SAME (" + tp + "))");
                    } else {
                        System.out.println(">>>>>>>>>>>>>WOW");
                    }

                }
            }

        }
        
        if (!allAdded) {
            result.add(gm);
        }
        

//        positions.forEach(System.out::println);
        return result;
//        tree = IntervalTree()
//        for (grammar, match) in matches:
//            start, stop = get_tokens_position(match)
//            exists = tree[start:stop]
//            if exists and not strict:
//                for interval in exists:
//                    exists_grammar, _ = interval.data
//                    exists_contains_current_grammar = (
//                        interval.begin < start and interval.end > stop)
//                    exists_grammar_with_same_type = isinstance(
//                        exists_grammar, grammar.__class__)
//                    if not exists_grammar_with_same_type and exists_contains_current_grammar:
//                        exists = False
//            if not exists:
//                tree[start:stop] = (grammar, match)
//                yield (grammar, match)
    }

}
