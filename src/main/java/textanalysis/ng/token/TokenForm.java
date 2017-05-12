package textanalysis.ng.token;

import java.util.HashSet;

public class TokenForm {

    public HashSet<String> grammemes = new HashSet();
    public String normalForm;
    public double score = 0.0;
    public Object methodsStack = null;

    public TokenForm() {
    }

    public TokenForm(String normalForm) {
        this.normalForm = normalForm;
    }

    public TokenForm addGrammeme(String val) {
        this.grammemes.add(val);
        return this;
    }

}
