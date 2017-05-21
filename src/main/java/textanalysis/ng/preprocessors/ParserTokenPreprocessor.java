package textanalysis.ng.preprocessors;

import com.zunama.Dawg;
import java.util.ArrayList;
import java.util.List;
import textanalysis.ng.helpers.ArrayPermutator;
import textanalysis.ng.ParserToken;
import textanalysis.ng.token.TokenForm;
import textanalysis.ng.token.TokenPosition;

abstract public class ParserTokenPreprocessor {

    protected List<ParserToken> stack = new ArrayList();
    protected Dawg dictionary;
    protected String grammarTag;

    public String getGrammarTag() {
        return grammarTag;
    }

    public void setGrammarTag(String grammarTag) {
        this.grammarTag = grammarTag;
    }

    // this method should be definitelly rewrited
    protected ShiftResult shift(ParserToken token) {

        List<ParserToken> possible_stack = new ArrayList();

        for (ParserToken pt : this.stack) {
            possible_stack.add(pt);
        }

        possible_stack.add(token);

        boolean match = this.matchesPrefix(possible_stack);
        if (match) {
            this.stack.add(token);
            return new ShiftResult(ShiftResult.State.CommonPrefix, token);
        } else {
            this.stack = new ArrayList();
            String matchedWord = this.matchesCompleteWord(possible_stack);
            if (!matchedWord.isEmpty()) {
                return new ShiftResult(ShiftResult.State.Found, this.createNewToken(possible_stack, matchedWord));
            } else {
                return new ShiftResult(ShiftResult.State.NotFound, possible_stack);
            }
        }
    }

    protected boolean matchesPrefix(List<ParserToken> stack) {

        List<String> wordForms = this.mergeStack(stack);

        for (String form : wordForms) {
            if (!this.dictionary.search(form) && this.dictionary.prefixExist(form)) {
                return true;
            }
        }
        return false;
    }

    protected List<String> mergeStack(List<ParserToken> stack) {
        List<String> result = new ArrayList();

        List<List<String>> toPermutate = new ArrayList();
        for (ParserToken pt : stack) {
            List<String> wForms = new ArrayList();

            wForms.add(pt.value);
            for (TokenForm tf : pt.forms) {
                wForms.add(tf.normalForm);
            }

            toPermutate.add(wForms);
        }

        for (List<String> perm : ArrayPermutator.permutate(toPermutate)) {
            // TODO move this out of this class .delimiter
            result.add(String.join("_", perm));
        }

        return result;
    }

    protected String matchesCompleteWord(List<ParserToken> stack) {

        List<String> wordForms = this.mergeStack(stack);

        for (String form : wordForms) {

            if (this.dictionary.search(form)) {
                return form;
            }
        }
        return "";
    }

    public abstract List<ParserToken> proceed(List<ParserToken> tokens);

    public String getOriginalForm(List<ParserToken> stack) {

        List<String> origin = new ArrayList();

        for (ParserToken it : stack) {
            origin.add(it.value);
        }

        return String.join("_", origin);
    }

    public TokenPosition getPosition(List<ParserToken> stack) {
        if (stack.size() >= 2) {
            return new TokenPosition(stack.get(0).position.start, stack.get(stack.size() - 1).position.end);
        } else {
            return stack.get(0).position;
        }
    }

    private ParserToken createNewToken(List<ParserToken> stack, String matchedWord) {

        // IF not a replacement of forms
        ArrayList<TokenForm> forms = stack.get(0).forms;

        TokenForm f = forms.get(0);
        f.normalForm = matchedWord;
        f.grammemes.add(this.getGrammarTag());

        ParserToken pt  =new ParserToken(
                getOriginalForm(stack),
                getPosition(stack),
                forms
        );
        
        return pt;
    }
}
