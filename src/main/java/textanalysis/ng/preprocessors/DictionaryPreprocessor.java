package textanalysis.ng.preprocessors;

import com.zunama.Dawg;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import textanalysis.ng.ParserToken;
import textanalysis.ng.Rule.SimpleGrammarRule;

public class DictionaryPreprocessor extends ParserTokenPreprocessor {

    SimpleGrammarRule cond = null;

    public DictionaryPreprocessor(String grammarTag, String resourcePath) {
        this(grammarTag, resourcePath, null);
    }

    public DictionaryPreprocessor(String grammarTag, String resourcePath, SimpleGrammarRule condition) {

        this.cond = condition;
        this.grammarTag = grammarTag;

        try {
            Path path = Paths.get(DictionaryPreprocessor.class.getClassLoader().getResource(resourcePath).toURI());

            List<String> words = Files.readAllLines(path, StandardCharsets.UTF_8);

            this.dictionary = new Dawg(words);

        } catch (Exception ex) {
            throw new RuntimeException("Can't load dictionary for tag `" + grammarTag + "`:" + ex.getMessage());
        }

    }

    @Override
    /**
     * This method could change token numeration: In case if some tokens merge -
     * the numeration would shift
     */
    public List<ParserToken> proceed(List<ParserToken> tokens) {

        List<ParserToken> result = new ArrayList();

        int tokenIndex = 0;

        for (ParserToken token : tokens) {
            ShiftResult r = this.shift(token);

            if (r.prefixType.equals(ShiftResult.State.CommonPrefix)) {
                continue;
            }

            if (r.prefixType.equals(ShiftResult.State.Found)) {

//                System.out.println("added:"+r.getToken());
                ParserToken pt = r.getToken();
                pt.setTokenIndex(tokenIndex++);

                if (this.cond != null) {

                    if (this.cond.checkLocal(pt)) {
                        result.add(pt);
                    }
                }

                continue;
            } else {
                for (ParserToken pt : r.getStack()) {

                    pt.setTokenIndex(tokenIndex++);

                    result.add(pt);
                }
            }

            this.stack = new ArrayList();

        }

        return result;

    }
}
