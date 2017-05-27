package textanalysis.parser;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.languagetool.JLanguageTool;
import org.languagetool.MultiThreadedJLanguageTool;
import org.languagetool.language.Ukrainian;
import textanalysis.Main;

public class Parser {

    public class ZikEntry {

        public String title;
        public String link;

        public ZikEntry(String title, String link) {
            this.title = title;
            this.link = link;
        }

        public String toString() {
            return this.link + " => " + this.title;
        }
    }

    class ZikPost {

        public String Title = "";
        public String Description = "";
        public ArrayList<String> Body = new ArrayList<>();

    }

    public void parse() throws IOException {

        JLanguageTool langTool = new MultiThreadedJLanguageTool(new Ukrainian());
        FrequencyCalculator calc = new FrequencyCalculator("C:\\coreference\\tf.json");

    }

    public void parseArticles() throws IOException {

        ArrayList<ZikEntry> items = new ArrayList<>();

        JsonFactory f = new MappingJsonFactory();
        JsonParser jp = f.createParser(new File("c:\\workspace\\zikentities200_250.json"));

        JsonToken current;

        current = jp.nextToken();
        if (current != JsonToken.START_ARRAY) {
            System.out.println("Error: root should be object: quiting.");
            return;
        }

        ArrayList<String> map = new ArrayList<>();

        while (jp.nextToken() != JsonToken.END_ARRAY) {

            JsonNode node = jp.readValueAsTree();
            ZikPost post = this.parseArticle(node.get("link").asText());

            String uid = UUID.randomUUID().toString();

            map.add(uid + " " + post.Description);

            Path file = Paths.get("C:\\course\\" + uid + ".txt");
            file.toFile().getParentFile().mkdirs();
            
            // TODO also write the title into the file.
            Files.write(file, post.Body, Charset.forName("UTF-8"));

        }

        Path file = Paths.get("C:\\course\\mappings" + UUID.randomUUID().toString() + ".txt");
        file.toFile().getParentFile().mkdirs();
        Files.write(file, map, Charset.forName("UTF-8"));

//        System.out.println(this.parseArticle(items.get(10)).Body);
    }

    public ZikPost parseArticle(String link) throws IOException {

        ZikPost response = new ZikPost();

        Document articlesList = Jsoup.connect("http://zik.ua" + link).get();
        Elements body = articlesList.getElementsByTag("article").first().getElementsByTag("p");

        for (Element it : body) {
            if (it.hasClass("description")) {
                response.Description = it.text();
            } else {
                response.Body.add(it.text());
            }
        }

        return response;
    }

    public void parseNewsArticles() throws IOException {

        ArrayList<ZikEntry> items = new ArrayList<>();

        int pages = 50;

        int chunk = 4;

        int start = chunk * pages;
        int limit = chunk * pages + pages;

        for (int i = start; i < limit; i++) {

            Document articlesList = Jsoup.connect("http://zik.ua/rubric/ludyna?pg=" + i).get();

            Elements list = articlesList.getElementsByClass("news-list");

            for (Element it : list) {
                for (Element li : it.getElementsByTag("li")) {

                    Element a = li.getElementsByTag("a").first();
                    if (a.attr("href").toString().startsWith("/news")) {
                        items.add(new ZikEntry(a.text(), a.attr("href").toString()));
                    }
                }
            }

        }

        ObjectMapper om = new ObjectMapper();
        try {
            System.out.println(om.writeValueAsString(items));
            
            
            File nF = new File("C:\\workspace\\zikentities" + start + "_" + limit + ".json");
            nF.getParentFile().mkdirs();
            
            om.writeValue(nF, items);

        } catch (JsonProcessingException ex) {
            Logger.getLogger(Main.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
//
//        System.out.println("parsed zik list:" + articlesList.title());
    }

}