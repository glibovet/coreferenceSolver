package textanalysis.pipeline;

public abstract class RuleOperation {
    
    abstract public boolean check(Token token);
    abstract public String[] params();
}
