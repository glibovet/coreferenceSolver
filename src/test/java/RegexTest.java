
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class RegexTest {

    private void testRegexWithInput(String regex, String input) {

        Pattern p = Pattern.compile(regex, Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

        Matcher m = p.matcher(input);

        assertTrue(m.find());
    }

    @Test
    public void testRussian() {

        String russian_token_regex = "(?<russian>[а-яё][а-яё\\-]*)";//

        String input = "Привет, чо по чом";

        this.testRegexWithInput(russian_token_regex, input);

    }

    @Test
    public void testLatin() {

        String regex = "(?<latin>[a-z][a-z\\'\\-]*)";

        String input = "Hello my friend";

        this.testRegexWithInput(regex, input);
    }

    @Test
    public void testIntSeparated() {

        String regex = "(?<intseparated>[1-9]\\d*(\\s\\d{3})+)";
        String input = "5 300";

        this.testRegexWithInput(regex, input);

    }

    @Test
    public void testIntRange() {

        String regex = "(?<intrange>\\d+\\s*?[\\-\\—]\\s*?\\d+)";
        String input = "5 - 300";

        this.testRegexWithInput(regex, input);

    }

    @Test
    public void testInt() {

        String regex = "(?<int>[+-]?\\d+)";
        String input = "300";

        this.testRegexWithInput(regex, input);

    }

    @Test
    public void testFloatRange() {

        String regex = "(?<floatrange>[\\d]+[\\.\\,][\\d]+\\s*?[\\-\\—]\\s*?[\\d]+[\\.\\,][\\d]+)";
        String input = "300.55 - 99.97";

        this.testRegexWithInput(regex, input);

    }

    @Test
    public void testFloat() {

        String regex = "(?<float>[+-]?[\\d]+[\\.\\,][\\d]+)";
        String input = "300.55";

        this.testRegexWithInput(regex, input);

    }

    @Test
    public void testQuote() {

        String regex = "(?<quote>[\\\"\\'\\«\\»\\„\\“])";
        String input = "'Wassup' said bro";

        this.testRegexWithInput(regex, input);

    }

    @Test
    public void testEmail() {

        String regex = "(?<email>[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+)";
        String input = "my email is dot5enko@yandex.ru";

        this.testRegexWithInput(regex, input);

    }

    @Test
    public void testPhone() {

        String regex = "(?<phone>(\\+)?([-\\s_()]?\\d[-\\s_()]?){10,14})";
        String input = "my email is dot5enko@yandex.ru and my phone is +380633274782";

        this.testRegexWithInput(regex, input);

    }

    @Test
    public void testEndline() {

        String regex = "(?<endofline>[\\n\\r]+)";
        String input = "my email is dot5enko@yandex.ru \n and my phone is +380633274782";

        this.testRegexWithInput(regex, input);

    }

}
