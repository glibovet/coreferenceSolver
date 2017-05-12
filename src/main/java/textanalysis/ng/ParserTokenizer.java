package textanalysis.ng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParserTokenizer {

    private final Pattern tokenRegex;

    private final HashMap<String, Pattern> patternsMap = new HashMap();

    // TODO put this regex into some file
    public ParserTokenizer() {

        String russian_token_regex = "(?<cyrillic>[а-яєёїґі\\'][а-яёєїґі\\'\\-]*)";
        this.patternsMap.put("cyrillic", Pattern.compile(russian_token_regex, Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE));

        String latin_token_regex = "(?<latin>[a-z][a-z\\'\\-]*)";
        this.patternsMap.put("latin", Pattern.compile(latin_token_regex, Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE));

        String int_separated_token_regex = "(?<intseparated>[1-9]\\d*(\\s\\d{3})+)";
        this.patternsMap.put("intseparated", Pattern.compile(int_separated_token_regex, Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE));

        String int_range_token_regex = "(?<intrange>\\d+\\s*?[\\-\\—]\\s*?\\d+)";
        this.patternsMap.put("intrange", Pattern.compile(int_range_token_regex, Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE));

        String int_token_regex = "(?<int>[+-]?\\d+)";
        this.patternsMap.put("int", Pattern.compile(int_token_regex, Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE));

        String float_range_token_regex = "(?<floatrange>[\\d]+[\\.\\,][\\d]+\\s*?[\\-\\—]\\s*?[\\d]+[\\.\\,][\\d]+)";
        this.patternsMap.put("floatrange", Pattern.compile(float_range_token_regex, Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE));

        String float_token_regex = "(?<float>[+-]?[\\d]+[\\.\\,][\\d]+)";
        this.patternsMap.put("float", Pattern.compile(float_token_regex, Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE));

        String quote_token_regex = "(?<quote>[\\\"\\'\\«\\»\\„\\“])";
        this.patternsMap.put("quote", Pattern.compile(quote_token_regex, Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE));

        String punctuation_token_regex = "(?<punct>[\\—!\"\\#$%&'()*+,\\-./:;<=>?@\\[\\]^_`{|}~\\\\])";
        this.patternsMap.put("punct", Pattern.compile(punctuation_token_regex, Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE));

        String email_token_regex = "(?<email>[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+)";
        this.patternsMap.put("email", Pattern.compile(email_token_regex, Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE));

        String phone_number_token_regex = "(?<phone>(\\+)?([-\\s_()]?\\d[-\\s_()]?){10,14})";
        this.patternsMap.put("phone", Pattern.compile(phone_number_token_regex, Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE));

        String end_of_line_token_regex = "(?<endofline>[\\n\\r]+)";
        this.patternsMap.put("endofline", Pattern.compile(end_of_line_token_regex, Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE));

        String complete_token_regex = String.join("|",
                russian_token_regex,
                latin_token_regex,
                int_separated_token_regex,
                int_range_token_regex,
                int_token_regex,
                float_range_token_regex,
                float_token_regex,
                quote_token_regex,
                punctuation_token_regex,
                email_token_regex,
                phone_number_token_regex,
                end_of_line_token_regex
        );

        this.tokenRegex = Pattern.compile(complete_token_regex, Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
    }

    /**
     * It's the best solution so far :(
     *
     * @param tokenVal
     * @return
     */
    private String getGroupName(String tokenVal) {
        for (Entry<String, Pattern> gp : this.patternsMap.entrySet()) {
            if (gp.getValue().matcher(tokenVal).find()) {
                return gp.getKey();
            }
        }

        return "";
    }

    public ArrayList<ParserToken> transform(String text) {

        ArrayList<ParserToken> result = new ArrayList();

        Matcher m = this.tokenRegex.matcher(text);
        while (m.find()) {
            System.out.println("`" + m.group() + "`  -> " + this.getGroupName(m.group()));
        }

        return result;

    }

}
