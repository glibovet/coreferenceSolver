package textanalysis.ng.preprocessors;

import java.util.ArrayList;
import textanalysis.ng.ParserToken;

public class DictionaryPreprocessor extends ParserTokenPreprocessor {

    @Override
    public void proceed(ArrayList<ParserToken> tokens) {

        for (ParserToken token : tokens) {
            ShiftResult r = this.shift(token);

            if (r.prefixType.equals(ShiftResult.State.CommonPrefix)) {
                continue;
            }
            
            if (r.prefixType.equals(ShiftResult.State.Found)) {
                continue;
            } 
            
            this.stack = new ArrayList();
            

        }
    }
}
