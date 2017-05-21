package textanalysis.ng;

public class ParserMatch {

    public int index;
    public ParserToken token;

    public ParserMatch(int index, ParserToken token) {
        this.index = index;
        this.token = token;
    }

    @Override
    public String toString() {
        return "ParserMatch{" + token + '}';
    }
    
    
}
