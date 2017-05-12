package textanalysis.ng;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Parser {

    private List<ParserGrammar> grammars = new ArrayList();
    private ArrayList<ParserPipeline> pipelines = new ArrayList();
    private ParserTokenizer tokenizer;

    public Parser(ParserGrammar ...  grammars) {
        this.grammars = Arrays.asList(grammars);
        this.tokenizer = new ParserTokenizer();
    }

    public ArrayList<ParserMatch> extract(String text) {
        return this.extract(text, true);
    }

    public ArrayList<ParserMatch> extract(String text, boolean flattenResult) {

        ArrayList<ParserMatch> match;

        ArrayList<ParserToken> stream = this.tokenizer.transform(text);

        // not yet implemented
//        for (ParserPipeline pipeline : this.pipelines) {
////            pipeline
//        }

        for (ParserToken token : stream) {
            for (ParserGrammar grammar : this.grammars) {
                boolean recheck = grammar.shift(token);
                match = grammar.reduce();

                if (match.size() > 0) {
                    return match;

                }

                if (recheck) {
                    grammar.shift(token);
                    match = grammar.reduce();

                    if (match.size() > 0) {
                        return match;

                    }

                }

            }
        }

        for (ParserGrammar grammar : this.grammars) {
            match = grammar.reduce(true);
            if (match.size() > 0) {
                return match;

            }
            grammar.reset();
        }

        throw new RuntimeException("This should not be happen!");

    }

}
