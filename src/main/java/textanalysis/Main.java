package textanalysis;

import com.trg.server.di.ServiceContainer;
import com.trg.server.protocols.http.RequestHandler;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.Random;

import org.languagetool.*;
import org.languagetool.language.*;

import textanalysis.parser.FrequencyCalculator;

import textanalysis.parser.Parser;

public class Main {

    public static void main(String[] args) throws IOException {
        Parser zikParser = new Parser();
        zikParser.parseArticles();
    }

    public static void mains(String[] args) throws IOException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException {

        System.setProperty("file.encoding", "UTF-8");
        Field charset = Charset.class.getDeclaredField("defaultCharset");
        charset.setAccessible(true);
        charset.set(null, null);

        try {

            ServiceContainer sc = ServiceContainer.getInstance();

            com.trg.server.Server x = new com.trg.server.Server(() -> {
                return new RequestHandler("textanalysis.web.controllers");
            }, sc);

            // put language tool into service container
            JLanguageTool langTool = new JLanguageTool(new Ukrainian());
            sc.add("langTool", langTool);

            FrequencyCalculator calc = new FrequencyCalculator("src/main/resources/tf.json");
            sc.add("frequency_calc", calc);

            x.start(() -> {
                System.out.println("started server on port " + x.getPort());
            }, 8081);

        } catch (Exception e) {
            System.out.println("Error ocurred:" + e.getMessage());
        }
    }

    public static String getHexColor() {

        int RANDOM_HEX_LENGTH = 6;

        Random randomService = new Random();
        StringBuilder sb = new StringBuilder();
        while (sb.length() < RANDOM_HEX_LENGTH) {
            sb.append(Integer.toHexString(randomService.nextInt()));
        }
        sb.setLength(RANDOM_HEX_LENGTH);
        return sb.toString();
    }

}
