package textanalysis.ng.token;

import java.util.regex.Pattern;
import textanalysis.ng.ParserToken;

public abstract class TokenType {

    private String name;
    private Pattern pattern;
    private String rawPattern;

    public String getName() {
        return this.name;
    }

    public Pattern getPattern() {
        return this.pattern;
    }

    public String getRawPattern() {
        return this.rawPattern;
    }

    public TokenType() {

    }

    public TokenType(String name, String pattern) {
        this.name = name;
        this.rawPattern = "(?<" + name + ">" + pattern + ")";
        this.pattern = Pattern.compile(this.rawPattern, Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
    }

    abstract public ParserToken transform(String value, int start, int end);
    
    public void getWordForms(String word) {
    
    }
    
}
