package textanalysis.pipeline.operations;

import textanalysis.PosTagBag;
import textanalysis.pipeline.RuleOperation;
import textanalysis.pipeline.Token;

public class SoftTag extends RuleOperation {
    
    private String name;
    
    public SoftTag(String name) {
        this.name = name;
    }
    
    @Override
    public boolean check(Token token) {
        PosTagBag tb = token.getTags();
        return tb.hasTag("&" + this.name) || tb.hasTag(this.name);
    }
    
    @Override
    public String[] params() {
        return new String[]{this.name};
    }
    
}
