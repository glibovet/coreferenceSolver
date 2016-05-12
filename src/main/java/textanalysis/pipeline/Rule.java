package textanalysis.pipeline;

import java.util.ArrayList;
import java.util.HashSet;
import textanalysis.TokenGroup;
import textanalysis.TokenPipeline;
import textanalysis.pipeline.operations.HasTag;

public class Rule {

    private String name;
    private int startOffset = 0;
    private ArrayList<RuleOperation> operations = new ArrayList<>();
    private HashSet<String> skipRules = new HashSet<>();

    public String getName() {
        return this.name;
    }

    public Rule(String name, String tag) {
        this.name = name;
        this.then(tag);
    }

    public Rule(String name, RuleOperation op) {
        this.name = name;
        this.then(op);
    }

    public Rule() {
        this.name = "ANONYMOUS_RULE";
    }

    public Rule then(String tag) {
        return this.then(1, new HasTag(tag));
    }

    public Rule then(int times, String tag) {
        return this.then(times, new HasTag(tag));
    }

    public Rule then(RuleOperation op) {
        return this.then(1, op);
    }

    private int paddingLeft = 0;
    private int paddingRight = 0;

    public Rule paddingLeft(int count) {
        this.paddingLeft = count;
        return this;
    }

    public Rule paddingRight(int count) {
        this.paddingRight = count;
        return this;
    }

    public Rule then(int times, RuleOperation op) {
        for (int i = 0; i < times; i++) {
            this.operations.add(op);
        }

        return this;
    }

    public Rule skip(String r) {
        this.skipRules.add(r);
        return this;
    }

    public Rule skip(String[] r) {

        for (String it : r) {
            this.skip(it);
        }

        return this;
    }

    public HashSet<String> skipRules() {
        return this.skipRules;
    }

    public Rule startFrom(int offset) {
        this.startOffset = offset;
        return this;
    }

    private TokenGroup lastMatch = new TokenGroup();

    public TokenGroup lastMatches() {
        return this.lastMatch;
    }

    public boolean check(TokenPipeline currentPipeline) {
        // fully determined check

        this.lastMatch.clear();

        int position = this.startOffset + currentPipeline.currentIndex;
        int ruleOffset = 0;

        if (position + this.operations.size() > currentPipeline.length) {
            return false;
        }

        if (this.paddingLeft > 0 && (position + ruleOffset - this.paddingLeft) >= 0) {
            for (int p = this.paddingLeft; p > 0; p--) {
                this.lastMatch.add(currentPipeline.tokens.getTokens().get(position + ruleOffset - p));
            }
        }

        for (RuleOperation curOp : this.operations) {
            Token curToken = currentPipeline.tokens.getTokens().get(position + ruleOffset);
            if (!curOp.check(curToken)) {
                return false;
            }
            this.lastMatch.add(curToken);
            ruleOffset++;
        }

        if (this.paddingRight > 0 && (position + ruleOffset + this.paddingRight) <= currentPipeline.length) {
            for (int p = 0; p < this.paddingRight; p++) {
                this.lastMatch.add(currentPipeline.tokens.getTokens().get(position + ruleOffset + p));
            }
        }

        currentPipeline.skip(this.operations.size() - 1);

        return true;
    }

}
