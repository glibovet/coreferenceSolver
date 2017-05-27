package textanalysis;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Stream;
import org.bson.Document;
import org.bson.json.JsonWriterSettings;
import spark.ModelAndView;

import textanalysis.ng.GrammarMatch;
import textanalysis.ng.ParserToken;
import textanalysis.ng.Rule.GrammarRuleI;
import textanalysis.ng.preprocessors.ParserTokenPreprocessor;
import textanalysis.ng.token.TokenForm;

import textanalysis.parser.Parser;
import static spark.Spark.*;
import spark.TemplateViewRoute;
import spark.template.jtwig.JtwigTemplateEngine;

public class Main {

    public static void mains(String[] args) throws IOException {
        Parser zikParser = new Parser();
        zikParser.parseArticles();
    }

    static class Article {

        String id;
        String title;
        String body;
        boolean tagged = false;

        public Article(String id, String title, String body, boolean tagged) {
            this.id = id;
            this.title = title;
            this.body = body;
            this.tagged = tagged;
        }

        String neData = "";

        public void setNeData(Document data) throws IOException {

            this.neData = data.toJson(new JsonWriterSettings(true));

            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(sourcesDir + this.id + ".json"), "UTF-8"));

            try {
                out.write(this.neData);
            } finally {
                out.close();
            }

            this.tagged = true;

        }
    }

    private static String sourcesDir = "C:\\course_data\\zik\\";
    private final static Random randomGenerator = new Random();
    private final static Map<String, Article> articles = new HashMap();

    public static Article getArticleText(String articleId) throws IOException, Exception {

        if (articleId != null) {
            return articles.get(articleId);
        }

        Stream<Map.Entry<String, Article>> filter = articles.entrySet().stream().skip(randomGenerator.nextInt(articles.size())).filter(e -> !e.getValue().tagged);
        try {
            return filter.findFirst()
                    .orElseThrow(() -> new Exception("Can't find a text for marking"))
                    .getValue();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public static void main(String[] args) throws IOException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException {

        if (args.length > 0) {
            sourcesDir = args[0];
        }

        textanalysis.ng.Parser entityParser = new textanalysis.ng.Parser(
                new GrammarRuleI[][]{ //                    Person.getGrammarRules(), // make this generic
                //                    Brand.getGrammarRules()
                },
                new ParserTokenPreprocessor[]{ //                    new DictionaryPreprocessor("Org/Education", "dictionaries/org_education.txt",simple(not(gram("verb")))),
                //                    new DictionaryPreprocessor("Person/Position", "dictionaries/persons.txt",simple(not(gram("verb"))))
                });

        String articleMappingPath = sourcesDir + "mappings975d7b41-9c46-44da-b07c-5ac7e03f3a9b.txt";

        List<String> articleLines = Files.readAllLines(Paths.get(articleMappingPath));

        // prepare proper title names for articles
        // its terrible but i don't have a time for better code
        for (String article : articleLines) {
            String[] exploded = article.split(" ", 2);

            File path = new File(sourcesDir + exploded[0] + ".json");

            Article arti = new Article(exploded[0], exploded[1], new String(Files.readAllBytes(Paths.get(sourcesDir + exploded[0] + ".txt")), Charset.forName("UTF-8")), path.exists());

            if (path.exists()) {
                arti.neData = new String(Files.readAllBytes(Paths.get(sourcesDir + exploded[0] + ".json")));
            }

            articles.put(exploded[0], arti);
        }

        // set port of web server to 8081
        port(8080);

        get("/ner/tagger", (rq, rs) -> {
            Map data = new HashMap();

            data.put("texts", articles);

            return new ModelAndView(data, "tagging.twig");
        }, new JtwigTemplateEngine());

        get("/ner/input", (rq, rs) -> new ModelAndView(new HashMap(), "index.twig"), new JtwigTemplateEngine());

        post("/ner/extract", (request, response) -> {

            String text = request.queryMap().get("text").value();

            Document json = new Document();

            ArrayList<Document> list = new ArrayList();

            for (GrammarMatch it : entityParser.extract(text)) {

                Document node = new Document();
                node.put("rule", it.matchedRule.getName());

                Document position = new Document();
                position.put("start", it.getTokensPosition().start);
                position.put("end", it.getTokensPosition().end);
                node.put("pos", position);

                node.put("val", it.toString());

                list.add(node);
            }

            json.put("items", list);

            Document jsonTokens = new Document();
            List<Document> jt = new ArrayList();

            for (ParserToken pt : entityParser.getAllTokens()) {
                Document t = new Document("v", pt.value);
                t.put("sp", pt.position.start);
                t.put("ep", pt.position.end);

                // forms to json
                ArrayList<Document> forms = new ArrayList();

                for (TokenForm tf : pt.forms) {
                    Document f = new Document();
                    f.put("nf", tf.normalForm);
                    f.put("tags", tf.grammemes.toString());

                    forms.add(f);
                }

                t.put("f", forms);
                jt.add(t);
            }

            Map data = new HashMap();

            jsonTokens.put("tokens", jt);

            data.put("tokens", jsonTokens.toJson());

            data.put("text", text);
            data.put("json", json.toJson(new JsonWriterSettings(true)));

            return new ModelAndView(data, "extract.twig");

        }, new JtwigTemplateEngine());

        TemplateViewRoute markerHandler = (request, response) -> {

            Article art = getArticleText(request.params("document"));
            String text = art.body;

            Document json = new Document();

            ArrayList<Document> list = new ArrayList();

            for (GrammarMatch it : entityParser.extract(text)) {

                Document node = new Document();
                node.put("rule", it.matchedRule.getName());

                Document position = new Document();
                position.put("start", it.getTokensPosition().start);
                position.put("end", it.getTokensPosition().end);
                node.put("pos", position);

                node.put("val", it.toString());

                list.add(node);

            }

            json.put("items", list);

            Document jsonTokens = new Document();
            List<Document> jt = new ArrayList();

            for (ParserToken pt : entityParser.getAllTokens()) {
                Document t = new Document("v", pt.value);
                t.put("sp", pt.position.start);
                t.put("ep", pt.position.end);

                // forms to json
                ArrayList<Document> forms = new ArrayList();

                for (TokenForm tf : pt.forms) {
                    Document f = new Document();
                    f.put("nf", tf.normalForm);
                    f.put("tags", tf.grammemes.toString());

                    forms.add(f);
                }

                t.put("f", forms);
                jt.add(t);
            }

            Map data = new HashMap();

            data.put("title", art.title);
            data.put("article_id", art.id);

            data.put("article", art);

            jsonTokens.put("tokens", jt);

            data.put("tokens", jsonTokens.toJson());

            data.put("text", text);
            data.put("json", json.toJson(new JsonWriterSettings(true)));

            return new ModelAndView(data, "mark.twig");

        };

        get("/ner/marker/:document", markerHandler, new JtwigTemplateEngine());

        get("/ner/marker", markerHandler, new JtwigTemplateEngine());

        // save tagged data 
        post("/ner/markerSave", (req, res) -> {
            res.type("application/json");

            String documentId = req.queryMap().get("id").value();
            Document data = Document.parse(req.queryMap().get("data").value());

            articles.get(documentId).setNeData(data);

            Document response = new Document("state", true);
            response.put("got", data.toJson(new JsonWriterSettings(true)));
            response.put("got_id", documentId);

            return response.toJson();
        });

    }
}
