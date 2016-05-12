package textanalysis.pipeline;

import textanalysis.PosTagBag;
import java.util.HashSet;
import textanalysis.TokenPipeline;

public abstract class StepHandler {

    protected Token Prev, Next, Current;
    protected TokenPipeline Pipeline;
    private HashSet<String> skipRules = new HashSet<>();

    public StepHandler() {
    }

    abstract public void handle();

    public final void setPipeline(TokenPipeline pipeline) {
        this.Pipeline = pipeline;
    }

    public final void setPrevToken(Token prev) {
        this.Prev = prev;
    }

    public final void setToken(Token c) {
        this.Current = c;
    }

    public final PosTagBag prevTags() throws NullPointerException {
        return this.Prev.getTags();
    }

    public final PosTagBag nextTags() throws NullPointerException {
        return this.Next.getTags();
    }

    public String Token() {
        return Token(0);
    }

    public String Token(int position) {
        int offsetPosition = position + this.Pipeline.currentIndex;
        if (offsetPosition >= 0) {
            return this.Pipeline.tokens.getTokens().get(offsetPosition).getToken().getToken();
        } else {
            return "";
        }
    }

    public PosTagBag TokenTags() {
        return TokenTags(0);
    }

    public PosTagBag TokenTags(int position) {
        int offsetPosition = position + this.Pipeline.currentIndex;
        if (offsetPosition >= 0 && this.Pipeline.length <= offsetPosition) {
            return this.Pipeline.tokens.getTokens().get(offsetPosition).getTags();
        } else {
            return new PosTagBag("");
        }
    }

    public StepHandler skipRules(String[] rulesToSkip) {
        for (String it : rulesToSkip) {
            this.skipRules.add(it);
        }
        return this;
    }

    public StepHandler skipTokens(int count) {
        this.Pipeline.skip(count);
        return this;
    }

    protected boolean RuleMatched = false;
    protected Rule MatchedRule;

    public boolean checkRule(Rule r) {

        if (this.RuleMatched) {
            return false;
        } else {
            if (this.skipRules.contains(r.getName())) {
                return false;
            }
            if (r.check(this.Pipeline)) {
                this.RuleMatched = true;
                this.MatchedRule = r;
                this.skipRules.addAll(r.skipRules());
                return true;
            } else {
                return false;
            }
        }
    }

    public void clear() {
        this.skipRules.clear();
        this.MatchedRule = null;
        this.RuleMatched = false;
    }

}
