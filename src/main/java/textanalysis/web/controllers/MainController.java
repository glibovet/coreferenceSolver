package textanalysis.web.controllers;

import com.trg.server.protocols.http.HttpController;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.bson.Document;
import org.bson.json.JsonWriterSettings;
import org.languagetool.AnalyzedSentence;
import org.languagetool.JLanguageTool;
import textanalysis.Max;
import textanalysis.TokenPipeline;
import textanalysis.ng.GrammarMatch;
import textanalysis.ng.Parser;
import textanalysis.ng.ParserToken;
import textanalysis.ng.Rule.GrammarRuleI;
import textanalysis.ng.grammars.Brand;
import textanalysis.ng.grammars.Person;
import textanalysis.ng.preprocessors.DictionaryPreprocessor;
import textanalysis.ng.preprocessors.ParserTokenPreprocessor;
import textanalysis.ng.token.TokenForm;
import textanalysis.parser.FrequencyCalculator;
import textanalysis.pipeline.Rule;
import textanalysis.pipeline.StepHandler;
import textanalysis.pipeline.TokenBag;
import textanalysis.pipeline.operations.AllOf;
import textanalysis.pipeline.operations.Any;
import textanalysis.pipeline.operations.HasTag;
import textanalysis.pipeline.operations.OneOf;
import textanalysis.pipeline.operations.SoftTag;

public class MainController extends HttpController {

    public void indexAction() {

    }

    /**
     * Not used now
     */
    public void explainAction() {
        try {

            // get the service from container
            JLanguageTool langTool = (JLanguageTool) this.services.get("langTool");

            List<AnalyzedSentence> sentences = langTool.analyzeText(params.get("text"));

            StringBuilder outputBuffer = new StringBuilder();

            ArrayList<String> tokensOutput = new ArrayList<>();

            TokenPipeline pipeline = new TokenPipeline(new TokenBag(sentences), new StepHandler() {

                @Override
                public void handle() {
                    tokensOutput.add(Current.getToken().getToken() + "<span style=color:gray>" + Current.getTags() + "</span>");
                }
            });

            pipeline.start();

            outputBuffer.append(String.join(" ", tokensOutput));

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void analyzeAction() throws IOException {

        // read input data from file
//        String inputPath = "./input.txt";
//
//        byte[] encoded = Files.readAllBytes(Paths.get(inputPath));
//        String content = new String(encoded, Charset.forName("UTF8"));
//
//        this.params.put("text", content);

        long startTime = System.currentTimeMillis();

        Rule nounGather = new Rule("adj").then("noun").then("prep").then("noun")
                .name("noun_with_prep");

        Rule adjectivePerson = new Rule("adj").then(new AllOf("noun", "anim")).then("noun").then("noun")
                .name("noun_adj_person");

        Rule longAdjectivePerson = new Rule("adj").then("adj").then(new AllOf("noun", "anim")).then("noun").then("noun")
                .name("noun_long_adj_person");

        Rule nounAdjAny = new Rule("noun").then(new AllOf("adj", "n_rod")).then(new Any())
                .name("noun_adj_rodoviy");
        Rule nounAdj = new Rule("noun").then("adj")
                .name("noun_adj");
        Rule adjectiveNoun = new Rule("adj").then("noun")
                .name("noun_adj_noun");

        Rule nns = new Rule("noun").then(new AllOf(new HasTag("noun"), new OneOf("v_rod", "v_dav", "v_zna"))).then(new HasTag("noun"))
                .name("nouns_rodoviy_any");

        Rule longNouns = new Rule("adj").then(2, "noun")
                .name("noun_adj_2nouns");
        Rule lngNAN = new Rule("noun").then("adj").then("noun")
                .name("noun_middle_adj");

//            Rule outerNoun = new Rule("noun").then(new Any()).then("noun")
//                    .name("noun_outer_noun");
        Rule nouns = new Rule("noun").then(new AllOf("noun", "v_rod"))
                .name("noun_two_nouns");

        Rule pronoun = new Rule(new SoftTag("pron"))
                .name("noun_pronoun");
//            Rule pron_verb = new Rule("pron").then("verb")
//                    .name("noun_pron_noun");
        Rule verb = new Rule(new OneOf(new HasTag("verb"), new HasTag("adjp"), new HasTag("advp")))
                .name("verb");

        Rule noun = new Rule("noun").name("noun");

        try {

            JLanguageTool langTool = (JLanguageTool) this.services.get("langTool");
            FrequencyCalculator calc = (FrequencyCalculator) this.services.get("frequency_calc");

            List<AnalyzedSentence> sentences = langTool.analyzeText(this.params.get("text"));
            calc.calculateTf(sentences);

            StringBuilder outputBuffer = new StringBuilder();
            StringBuilder outputHeader = new StringBuilder();

            ArrayList<String> tokensOutput = new ArrayList<>();

            Max max = new Max();
            max.maxTfIdf = 0.0;

            TokenPipeline pipeline = new TokenPipeline(new TokenBag(sentences), new StepHandler() {

                @Override
                public void handle() {

                    String lemma = Current.getToken().getLemma();
                    if (lemma == null) {
                        lemma = Current.getToken().getToken();
                    }
                    double tfidf = calc.calculateTfIdf(lemma);

                    if (tfidf > 0.02) {

                        checkRule(longAdjectivePerson);
                        checkRule(nounGather);
                        checkRule(adjectivePerson);
//                                checkRule(pron_verb);
                        checkRule(longNouns);
                        checkRule(nns);
                        checkRule(lngNAN);
                        checkRule(nounAdjAny);
                        checkRule(nounAdj);
                        checkRule(adjectiveNoun);
                        checkRule(nouns);
                        checkRule(noun);
                    }

                    checkRule(pronoun);
                    checkRule(verb);

                    if (!RuleMatched) {
                        if (tfidf > max.maxTfIdf) {
                            max.maxTfIdf = tfidf;
                        }
                        tokensOutput.add("<span class='term' data-tfidf='" + tfidf + "'>" + Current.getToken().getToken() + "</span>");
                    } else {
                        tokensOutput.add("<span data-group='" + MatchedRule.getName().substring(0, 4) + "' class='group'><span>" + MatchedRule.lastMatches().withTfIdf(calc) + "</span><div class=groupPopup style=display:none>[" + MatchedRule.getName() + ":" + MatchedRule.lastMatches().toStringAdvanced() + "]</div></span>");
                    }

                }
            });

            pipeline.start();

            outputBuffer.append(String.join(" ", tokensOutput));
            outputBuffer.append("<script> var maxTfIdf =" + max.maxTfIdf + ";</script>");

            this.view.set("maxTfIdf", max.maxTfIdf);

            this.view.set("outputBuffer", outputBuffer);
            this.view.set("tokensOutput", tokensOutput);

            long stopTime = System.currentTimeMillis();
            long elapsedTime = stopTime - startTime;
            System.out.println("TIME:" + elapsedTime);
        } catch (Exception e) {
            System.err.println("there is error");
            e.printStackTrace();
        }

    }

    public void extractAction() throws IOException {

        Parser entityParser = new Parser(
                new GrammarRuleI[][]{
                    Person.getGrammarRules(), // make this generic
                    Brand.getGrammarRules()
                },
                new ParserTokenPreprocessor[]{
                    new DictionaryPreprocessor("Org/Education", "dictionaries/org_education.txt"),
                    new DictionaryPreprocessor("Person/Position", "dictionaries/persons.txt")
                });

        LinkedList<String> ll = new LinkedList();

        String inputPath = "./input.txt";

        byte[] encoded = Files.readAllBytes(Paths.get(inputPath));
        String text = new String(encoded, Charset.forName("UTF8"));

        Document json = new Document();

        ArrayList<Document> list = new ArrayList();

        for (GrammarMatch it : entityParser.resolveMatches(entityParser.extract(text))) {

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
                forms.add(f);
            }

            t.put("f", forms);
            jt.add(t);
        }

        jsonTokens.put("tokens", jt);

        this.view.set("tokens", jsonTokens.toJson());

        this.view.set("text", text);
        this.view.set("json", json.toJson(new JsonWriterSettings(true)));

//        this.response.setContent(json);
    }

}
