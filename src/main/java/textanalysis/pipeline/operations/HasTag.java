package textanalysis.pipeline.operations;

import textanalysis.pipeline.RuleOperation;
import textanalysis.pipeline.Token;

public class HasTag extends RuleOperation {

    private String name;

    public HasTag(String name) {
        this.name = name;
    }

    @Override
    public boolean check(Token token) {
        return token.getTags().hasTag(this.name);
    }

    @Override
    public String[] params() {
        return new String[]{this.name};
    }

}
