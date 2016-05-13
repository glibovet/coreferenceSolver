package textanalysis.pipeline;

import textanalysis.PosTagBag;
import org.languagetool.AnalyzedToken;

public class Token {
    
    private PosTagBag tags;
    private AnalyzedToken token;

    public Token(AnalyzedToken token) {
        this.token = token;
        this.tags = new PosTagBag(token.getPOSTag());
    }

    
    public void setToken(AnalyzedToken t) {
    
        this.token = t;
    }
    
    public PosTagBag getTags() {
        return tags;
    }

    public AnalyzedToken getToken() {
        return token;
    }
    
}
