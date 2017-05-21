package textanalysis.ng;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import textanalysis.ng.Rule.ComplexGrammarRule;
import textanalysis.ng.Rule.GrammarRuleI;
import textanalysis.ng.Rule.SimpleGrammarRule;

public class ParserGrammar extends ComplexGrammarRule {

    public String getName() {
        return name;
    }

    protected String name = "";

    private ParserStack stack = new ParserStack();

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
    public boolean shift(ParserToken token, boolean recheck) {

        GrammarRuleI rule = this.rules.get(this.currentIndex);

        ParserToken copied = new ParserToken(token);

//        System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOOOO"+token.value+"["+this.name+"]"+this.currentIndex);
        if (!rule.isSimple()) {
            ComplexGrammarRule cRule = (ComplexGrammarRule) rule;
            
            cRule.setInserted(false);
            recheck = cRule.shift(token);
            
            
            
            if (cRule.isRepeatable() && cRule.isInserted() && cRule.getCurrentIndex() == cRule.rulesCount()) {
                cRule.setCurrentIndex(0);
                System.out.println("+++{"+token.value+"}+++++++++++++++++ NEED TO CHANGE INDEX OF PARENT AND COMPLEX ");
            }
            
//            System.out.println(String.join("", Collections.nCopies(depth, "\t"))+this.name + " | going to check complex (" + this.currentIndex + ") for `" + token.value + "`");
          
            
//            if (cRule.isRepeatable() && !cRule.isFirstRun() && !cRule.isInserted()) {
//                System.out.println("increased current rule number for `"+this.name+"`");
//                this.currentIndex++;
//            }


//            System.out.println("---["+this.name+"]------------------------------------>"+cRule.isInserted());
            
//            System.out.println(cRule.isInserted()+"|"+this.currentIndex+"|"+cRule.getCurrentIndex() +"/"+cRule.rulesCount());
//            if (cRule.isRepeatable()) {
////                if (cRule.getCurrentIndex())
//                System.out.println("should move to "+(this.currentIndex - cRule.rulesCount()));
////                this.currentIndex = ;
//            }
            if (!cRule.active()) {
                cRule.reset();
                this.reset();
            }

            return recheck;

        } else {
            SimpleGrammarRule sRule = (SimpleGrammarRule) rule;

//            System.out.println(String.join("", Collections.nCopies(depth, "\t")) + this.name + " | checking simple (" + this.currentIndex + ") for " + token.value);
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
                    // System.out.println(":: reset not optional, or repeatable =  " + sRule.repeatable);
                    this.reset();
                }
                // TODO check if the lastIndex could be not equal to currentIndex
                if ((this.currentIndex != lastIndex) && (!recheck || (sRule.optional || sRule.repeatable))) {
                    return this.shift(token, recheck);
                } else {
                    // System.out.println(":*: reset " + this.currentIndex + " != " + lastIndex);
                    this.reset();
                }

            } else {
                // tokens matched some rule
                if (!sRule.terminal) {
                    if (!sRule.skip) { // append to result if its not a terminal symbol or rule that should be skiped in result

                        // add some extra info
                        copied.normalization_type = sRule.normalization;
                        copied.interpretation = sRule.interpretation;

//                        System.out.println("[" + this.name + "] inserted `" + copied.value + "` as [" + this.stack.size() + "]");
                        this.stack.push(new ParserMatch(this.currentIndex, copied));
                        this.inserted = true;
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
    public boolean match(final ParserToken token, List<RuleLabel> labels) {
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
        
//        System.out.println("reset of `"+this.name+"`");
        
        this.stack = new ParserStack();
        this.currentIndex = 0;

        for (GrammarRuleI r : this.rules) {
            if (!r.isSimple()) {
                ((ComplexGrammarRule) r).reset();
            }
        }
    }

    @Override
    public ArrayList<ParserMatch> reduce(boolean endOfStream, boolean parentRepeatable) {

        ArrayList<ParserMatch> result = new ArrayList();

        GrammarRuleI currentRule = this.rules.get(this.currentIndex);
        GrammarRuleI terminalRule = this.rules.get(this.rules.size() - 1);

//        System.out.println("[" + this.name + "]reducing (" + this.currentIndex + ") to (" + (this.rules.size() - 1) + ")");
//         System.out.println("****reduce");
        boolean isComplex = !currentRule.isSimple();

        if (isComplex && currentRule.active()) {

//            System.out.println("--> complex:"+((ParserGrammar) currentRule).getName());
            ComplexGrammarRule cgr = (ParserGrammar) currentRule;
            result = cgr.reduce(endOfStream, cgr.isRepeatable());
            
//            System.out.println("is inserted = "+cgr.isInserted());
            
            if (result.size() > 0) {

//                   System.out.println("["+this.name+"]reducing ("+this.currentIndex+") to ("+(this.rules.size() - 1)+")");
//                System.out.println("result is = " + result);

//                System.out.println(" _> "+cgr.isInserted());

                if (!cgr.isRepeatable()) {

                    this.currentIndex++;
                    currentRule = this.rules.get(this.currentIndex);
                    isComplex = !currentRule.isSimple();

                    for (ParserMatch m : result) {
                        this.stack.push(new ParserMatch(this.currentIndex, m.token));
                    }

                }

            } else {
                
                
                 if (cgr.isRepeatable()) {
                     if (cgr.isInserted()) {
                          this.currentIndex++;
                     }
                 }
                
////                     System.out.println("["+this.name+"]reducing ("+this.currentIndex+") to ("+(this.rules.size() - 1)+")");
//                    System.out.println("result is zero. going to next, non repeatable rule::"+result);
//                    this.currentIndex++;
////
////                    currentRule = this.rules.get(this.currentIndex);
////                    isComplex = !currentRule.isSimple();
////                    for (ParserMatch m : result) {
////                        this.stack.push(new ParserMatch(this.currentIndex, m.token));
////                    }
//
//                }
            }
        }

//        if (parentRepeatable) {
////            if (!this.isInserted()) {
//
//                System.out.println("> FLAT X terminal");
//                return this.stack.flatten();
////            }
//            
////            return new ArrayList();
//            
//        } else {
        if (currentRule.equals(terminalRule) && this.stack.size() > 0) {

            result = this.stack.flatten();
//            System.out.println(">[" + this.currentIndex + "]>>>>FLAT (" + this.name + ") "+this.stack.size());
            if (!parentRepeatable) {
                this.reset();
            }
            return result;

        }
        
        
        
//        }

        if (endOfStream && !isComplex) {            
//            System.out.println("-------:" + this.stack.size());
            SimpleGrammarRule sRule = (SimpleGrammarRule) currentRule;

            boolean repeatableAndHaveMatches = (sRule.repeatable && this.stack.have_matches_by_rule_index(this.currentIndex));
            boolean nextRuleIsTerminal = this.rules.get(this.currentIndex + 1).equals(terminalRule);

            if ((repeatableAndHaveMatches || sRule.optional) && nextRuleIsTerminal) {

//                System.out.println("repeatableAndHaveMatches : " + this.stack.flatten());

                result = this.stack.flatten();
                this.reset();
                return result;
            }
        }

        return result;
    }
}
