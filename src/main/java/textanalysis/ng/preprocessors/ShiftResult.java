package textanalysis.ng.preprocessors;

import java.util.List;
import textanalysis.ng.ParserToken;

public class ShiftResult {
    
    public enum State {
        CommonPrefix,
        Found,
        NotFound
    }
    
    public State prefixType;

    public Object result;

    public ShiftResult(State prefixType, Object result) {
        this.prefixType = prefixType;
        this.result = result;
    }

    public ParserToken getToken() {
        return (ParserToken) this.result;
    }

    public List<ParserToken> getStack() {
        return (List<ParserToken>) this.result;
    }

}
