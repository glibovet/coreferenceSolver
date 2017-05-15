package textanalysis.ng.preprocessors;

import com.zunama.Dawg;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import textanalysis.ng.ParserToken;

public class DictionaryPreprocessor extends ParserTokenPreprocessor {

    public DictionaryPreprocessor(String grammarTag, String resourcePath) {
        
        this.grammarTag = grammarTag;
        
        try {
            Path path = Paths.get(DictionaryPreprocessor.class.getClassLoader().getResource(resourcePath).toURI());

            List<String> words = Files.readAllLines(path, StandardCharsets.UTF_8);

            this.dictionary = new Dawg(words);

        } catch (Exception ex) {
            throw new RuntimeException("Can't load dictionary for tag `" + grammarTag + "`:"+ex.getMessage());
        }

    }

    @Override
    public List<ParserToken> proceed(List<ParserToken> tokens) {

        List<ParserToken> result = new ArrayList();

        for (ParserToken token : tokens) {
            ShiftResult r = this.shift(token);

            if (r.prefixType.equals(ShiftResult.State.CommonPrefix)) {
                continue;
            }

            if (r.prefixType.equals(ShiftResult.State.Found)) {
                
//                System.out.println("added:"+r.getToken());
                
                result.add(r.getToken());
                continue;
            } else {
                for (ParserToken pt : r.getStack()) {
                    result.add(pt);
                }
            }

            this.stack = new ArrayList();

        }

        return result;

    }
}
