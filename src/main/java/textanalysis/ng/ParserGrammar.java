package textanalysis.ng;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import textanalysis.ng.Rule.ComplexGrammarRule;
import textanalysis.ng.Rule.GrammarRuleI;
import textanalysis.ng.Rule.SimpleGrammarRule;

public class ParserGrammar extends ComplexGrammarRule {

    public String getName() {
        return name;
    }

    protected String name = "";
    protected ArrayList<GrammarRuleI> rules = new ArrayList();
    
    private ParserStack stack = new ParserStack();
    private int currentIndex = 0;

    public ParserGrammar() {
    }

    public ParserGrammar(String name, GrammarRuleI... rules) {

        this.name = name;
        this.rules.addAll(Arrays.asList(rules));

        //  + empty terminal rule 
        this.rules.add(new SimpleGrammarRule(true));
    }

    /**
     * TODO check if this method works fine
     *
     * @return
     */
    @Override
    public boolean active() {
        for (GrammarRuleI r : this.rules) {
            if (!r.isSimple()) {
                if (r.active()) {
                    return true;
                }
            }
        }

        return this.stack.size() > 0;
    }

    @Override
    public boolean shift(ParserToken token, boolean recheck) { // recheck = false

        GrammarRuleI rule = this.rules.get(this.currentIndex);

        ParserToken copied = new ParserToken(token);

        if (!rule.isSimple()) {
            ComplexGrammarRule cRule = (ComplexGrammarRule) rule;
            recheck = cRule.shift(token);
            if (!cRule.active()) {
                cRule.reset();
                this.reset();
            }
            return recheck;

        } else {

            SimpleGrammarRule sRule = (SimpleGrammarRule) rule;

            if (sRule.terminal) {
                return true;
            }

            int lastIndex = 0;

            if (!this.match(copied, sRule.labels) && !sRule.terminal) {
                lastIndex = this.currentIndex;
                recheck = false;

                if (sRule.optional || (sRule.repeatable && this.stack.have_matches_by_rule_index(this.currentIndex))) {
                    this.currentIndex++;
                } else {
                    recheck = true;
                    this.reset();
                }
                // TODO check if the lastIndex could be not equal to currentIndex
                if ((this.currentIndex != lastIndex) && (!recheck || (sRule.optional || sRule.repeatable))) {
                    return this.shift(token, recheck);
                } else {
                    this.reset();
                }

            } else {
                // tokens matched some rule
                if (!sRule.terminal) {
                    if (!sRule.skip) { // append to result if its not a terminal symbol or rule that should be skiped in result

                        // add some extra info
                        copied.normalization_type = sRule.normalization;
                        copied.interpretation = sRule.interpretation;

                        this.stack.push(new ParserMatch(this.currentIndex, copied));
                    }
                    if (!sRule.repeatable) {
                        this.currentIndex++;
                    }
                }
            }
        }

        return recheck;
    }

    /**
     * Check if all rules are matched
     *
     * @param token
     * @param labels
     * @return
     */
    public boolean match(ParserToken token, List<RuleLabel> labels) {
        ArrayList<ParserMatch> localStack = this.stack.flatten();
        for (RuleLabel label : labels) {
            if (!label.check(token, localStack)) {
                return false;
            }
        }
        return true;
    }

    /**
     * TODO properly implement
     */
    @Override
    public void reset() {
        this.stack = new ParserStack();
        this.currentIndex = 0;

        for (GrammarRuleI r : this.rules) {
            if (!r.isSimple()) {
                ((ComplexGrammarRule) r).reset();
            }
        }
    }

    @Override
    public ArrayList<ParserMatch> reduce(boolean endOfStream) {
        ArrayList<ParserMatch> result = new ArrayList();

        GrammarRuleI currentRule = this.rules.get(this.currentIndex);
        GrammarRuleI terminalRule = this.rules.get(this.rules.size() - 1);

        boolean isComplex = !currentRule.isSimple();

        if (isComplex && currentRule.active()) {
            result = ((ComplexGrammarRule) currentRule).reduce(endOfStream);
            if (result.size() > 0) {
                this.currentIndex++;
                currentRule = this.rules.get(this.currentIndex);
                isComplex = !currentRule.isSimple();
                for (ParserMatch m : result) {
                    this.stack.push(new ParserMatch(this.currentIndex, m.token));
                }
            }
        }

        // TODO fix
        // i'm not sure this works properly
        if (currentRule.equals(terminalRule) && this.stack.size() > 0) {
            result = this.stack.flatten(); // TODO python have : match = self.stack
            this.reset();
            return result;
        }

        if (endOfStream && !isComplex) {

            SimpleGrammarRule sRule = (SimpleGrammarRule) currentRule;

            boolean repeatableAndHaveMatches = (sRule.repeatable && this.stack.have_matches_by_rule_index(this.currentIndex));
            boolean nextRuleIsTerminal = this.rules.get(this.currentIndex + 1).equals(terminalRule);

            if ((repeatableAndHaveMatches || sRule.optional) && nextRuleIsTerminal) {
                result = this.stack.flatten();
                this.reset();
                return result;
            }
        }

        return result;
    }
}
