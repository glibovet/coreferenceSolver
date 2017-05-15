package textanalysis.ng.token;

public class TokenPosition {

    public int start;
    public int end;

    public TokenPosition(int start, int end) {
        this.start = start;
        this.end = end;
    }
    
    public String toString() {
    
        return "{"+this.start+","+this.end+"}";
    }
    
    
    public TokenPosition(TokenPosition copy) {
        this.end = copy.end;
        this.start = copy.start;
    }
    

}
