package textanalysis.ng.Rule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import textanalysis.ng.ParserMatch;
import textanalysis.ng.ParserStack;
import textanalysis.ng.ParserToken;

public class OrGrammar extends ComplexGrammarRule {

    public String getName() {
        return "or_op";
    }

    private List<List<ParserMatch>> matches = new ArrayList();

    public OrGrammar() {
    }

    protected List<GrammarRuleI> workingRules;

    public OrGrammar(ComplexGrammarRule... rules) {

        this.rules.addAll(Arrays.asList(rules));

        this.workingRules = this.rules;

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

    /**
     * Todo need to check first run. eg
     * @param token
     * @return 
     */    
    @Override
    public boolean shift(ParserToken token, boolean rNotUsed) {
       boolean recheck = false;
        for ( GrammarRuleI wr :this.workingRules) {
            boolean ri = ((ComplexGrammarRule)wr).shift(token);
            if (ri) {
                recheck = true;
                break;
            }
        }
       
        this.updateWorkingRules();
                
        
        if(!recheck) {
            recheck = !this.matches.isEmpty() && this.workingRules.isEmpty();
        }
        
        return recheck;
    }

    @Override
    public void reset() {

        this.matches.clear();
        this.workingRules = this.rules;

        this.stack = new ParserStack();

        for (GrammarRuleI r : this.workingRules) {
            if (!r.isSimple()) {
                ComplexGrammarRule cgr = (ComplexGrammarRule) r;
                if (cgr.stack.size() > 0) {
                    cgr.reset();
                }
            }
        }
    }

    @Override
    public ArrayList<ParserMatch> reduce(boolean endOfStream, boolean parentRepeatable) {

        for (GrammarRuleI wr : this.workingRules) {
            List<ParserMatch> match = ((ComplexGrammarRule) wr).reduce(endOfStream, parentRepeatable);
            if (!match.isEmpty()) {
                this.matches.add(match);
            }
        }

        this.updateWorkingRules();

        if (!this.matches.isEmpty() && (this.workingRules.isEmpty() || endOfStream)) {

            this.matches.sort((a, b) -> {
                return a.size() - b.size();
            });

            List<ParserMatch> result = this.matches.get(0);

            this.reset();

            return (ArrayList) result;
        } else {
            return new ArrayList();
        }

    }

    private void updateWorkingRules() {

        List<GrammarRuleI> nworkingRules = new ArrayList();

        for (GrammarRuleI wr : this.workingRules) {
            if (wr.active()) {
                nworkingRules.add(wr);
            }
        }

        this.workingRules = nworkingRules;

    }
}
