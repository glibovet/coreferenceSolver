package textanalysis.ng;

import textanalysis.ng.token.TokenType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import textanalysis.ng.TokenTypes.*;

public class ParserTokenizer {

    private Pattern tokenRegex;

    private final HashMap<String, TokenType> patternsMap = new HashMap();

    public static ParserTokenizer defaultTokenizer() {

        // todo use classloader to automatically load all classes within a package 
        // this should be done outside of controller
        ParserTokenizer pt = new ParserTokenizer();

        pt
                .handle(new CyrillicToken())
                .handle(new EmailToken())
                .handle(new EndOfLineToken())
                .handle(new FloatRangeToken())
                .handle(new FloatToken())
                .handle(new IntRangeToken())
                .handle(new IntSeparatedToken())
                .handle(new IntToken())
                .handle(new LatinToken())
                .handle(new PhoneToken())
                .handle(new PunctuationToken())
                .handle(new QuoteToken());

        pt.buildParserRegex();

        return pt;
    }

    private ParserTokenizer() {
      
    }

    private ParserTokenizer buildParserRegex() {
        ArrayList<String> patterns = new ArrayList();

        this.patternsMap.forEach((key, tt) -> {
            patterns.add(tt.getRawPattern());
        });

        String complete_token_regex = String.join("|", patterns);

        this.tokenRegex = Pattern.compile(complete_token_regex, Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

        return this;
    }

    public ParserTokenizer(TokenType... handleTokens) {
        for (TokenType tt : handleTokens) {
            this.handle(tt);
        }

        this.buildParserRegex();

    }

    public final ParserTokenizer handle(TokenType t) {
        this.patternsMap.put(t.getName(), t);
        return this;
    }

    /**
     * This function return the rule name which matched passed value It's the
     * best solution so far :(
     *
     * @param tokenVal
     * @return
     */
    private String getGroupName(String tokenVal) {
        for (Entry<String, TokenType> gp : this.patternsMap.entrySet()) {
            if (gp.getValue().getPattern().matcher(tokenVal).find()) {
                return gp.getKey();
            }
        }

        return "";
    }

    public ArrayList<ParserToken> transform(String text) {
        
        if (this.patternsMap.size() == 0) {
            throw new RuntimeException("Parser tokenizer wasn't properly initialized!");
        }
        
        ArrayList<ParserToken> result = new ArrayList();

        Matcher m = this.tokenRegex.matcher(text);

        while (m.find()) {

            String tokenTypeName = this.getGroupName(m.group());
            if (this.patternsMap.containsKey(tokenTypeName)) {
                result.add(this.patternsMap.get(tokenTypeName).transform(m.group(), m.start(), m.end()));
            } else {
                throw new RuntimeException("This shouldn't happen. Not found a tokenType for `" + m.group() + "`");
            }
        }

        return result;

    }

}
