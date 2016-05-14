package textanalysis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.languagetool.*;
import org.languagetool.language.*;

import textanalysis.framework.Client;
import textanalysis.framework.Route;
import textanalysis.framework.Server;
import textanalysis.parser.FrequencyCalculator;

import textanalysis.parser.Parser;

import textanalysis.pipeline.Rule;
import textanalysis.pipeline.StepHandler;
import textanalysis.pipeline.TokenBag;

import textanalysis.pipeline.operations.*;

public class Main {

    public static void mains(String[] args) throws IOException {
        Parser zikParser = new Parser();
        zikParser.parse();
    }

    public static void main(String[] args) throws IOException {

        try {

            JLanguageTool langTool = new MultiThreadedJLanguageTool(new Ukrainian());
            FrequencyCalculator calc = new FrequencyCalculator("src/main/resources/tf.json");
            
            langTool.analyzeText("test sentence to load all libs");

            System.out.println("Base Started");

            Rule nounGather = new Rule("adj").then("noun").then("prep").then("noun")
                    .name("noun_with_prep");

            Rule nounAdjAny = new Rule("noun").then(new AllOf("adj", "n_rod")).then(new Any())
                    .name("noun_adj_rodoviy");
            Rule nounAdj = new Rule("noun").then("adj")
                    .name("noun_adj");
            Rule adjectiveNoun = new Rule("adj").then("noun")
                    .name("adj_noun");

            Rule nns = new Rule("noun").then(new AllOf(new HasTag("noun"), new OneOf("v_rod", "v_dav", "v_zna"))).then(new OneOf(new HasTag("noun"), new Any()))
                    .name("nouns_rodoviy_any");

            Rule longNouns = new Rule("adj").then(2, "noun")
                    .name("adj_2nouns");
            Rule lngNAN = new Rule("noun").then("adj").then("noun")
                    .name("middle_adj");

            Rule outerNoun = new Rule("noun").then(new Any()).then("noun")
                    .name("outer_noun");

            Rule nouns = new Rule("noun").then(new AllOf("noun", "v_rod"))
                    .name("two_nouns");

            Rule pronoun = new Rule("pron")
                    .name("pronoun");
            Rule pron_verb = new Rule("pron").then("verb")
                    .name("pron_noun");
            Rule verb = new Rule(new OneOf(new HasTag("verb"), new HasTag("adjp"), new HasTag("advp")))
                    .name("verb");

            Rule noun = new Rule("noun").name("noun");

            Server web = new Server(8080);

            web.register(new Route("/home", (Client c) -> {

                return "<html>"
                        + "<head>"
                        + "  <meta charset=\"utf-8\">"
                        + "  <title>Correferential solvage</title>"
                        + " </head>"
                        + "<body>"
                        + "<form method='POST' action='/analyze'>"
                        + "<p><textarea name=text rows=15 style='width:100%'></textarea></p>"
                        + "<p><input name=submit type=submit value=Analyze></p>"
                        + "</form>"
                        + "</body>"
                        + "</html>";
            }));

            web.register(new Route("/explain", "POST", (Client c) -> {

                try {

                    List<AnalyzedSentence> sentences = langTool.analyzeText(c.Request.param("text"));
                    
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

                    return outputBuffer.toString();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    return "error";
                }
            }));

            web.register(new Route("/analyze", "POST", (Client c) -> {
                long startTime = System.currentTimeMillis();

                try {

                    // noun rules 
                    List<AnalyzedSentence> sentences = langTool.analyzeText(c.Request.param("text"));
                    calc.calculateTf(sentences);
                    
                    StringBuilder outputBuffer = new StringBuilder();
                    StringBuilder outputHeader = new StringBuilder();

                    ArrayList<String> tokensOutput = new ArrayList<>();

                    TokenPipeline pipeline = new TokenPipeline(new TokenBag(sentences), new StepHandler() {

                        @Override
                        public void handle() {

//                            checkRule(nounGather);
//                            checkRule(pron_verb);
//
//                            checkRule(longNouns);
//
//                            checkRule(nns);
//
//                            checkRule(lngNAN);
//                            checkRule(nounAdjAny);
////                            checkRule(outerNoun);
//                            checkRule(nounAdj);
//                            checkRule(adjectiveNoun);
//                            checkRule(nouns);
//
//                            checkRule(verb);
//                            checkRule(pronoun);
//                            checkRule(noun);

                            if (!RuleMatched) {
                                
                                String lemma = Current.getToken().getLemma();
                                if (lemma == null) {
                                    lemma = Current.getToken().getToken();
                                }
                                
                                double tfidf = calc.calculateTfIdf(lemma);
                                double percent = (0.2/tfidf)/100;
                                
                                tokensOutput.add("<span style='background-color:rgba(127,191,63,"+percent+")'>"+Current.getToken().getToken()+"</span>");
                            } else {

                                tokensOutput.add("<span class='pCorref subG" + MatchedRule.getName().substring(0, 4) + " correfGrou" + Pipeline.sentenceIndex + "'><span>" + MatchedRule.lastMatches().toString() + "</span><div class=groupPopup style=display:none>[" + MatchedRule.getName() + ":" + MatchedRule.lastMatches().toStringAdvanced() + "]</div></span>");
                            }

                        }
                    });

                    pipeline.start();

                    outputBuffer.append(String.join(" ", tokensOutput));

                    long stopTime = System.currentTimeMillis();
                    long elapsedTime = stopTime - startTime;
                    System.out.println(elapsedTime);

                    return "<html>"
                            + "<head>"
                            + "  <meta charset='utf-8'>"
                            + "  <title>Correferential solvage</title>"
                            + "<script src=https://code.jquery.com/jquery-2.2.3.min.js></script>"
                            + "<script>" + Server.readFile("src/main/resources/code.js") + "</script>"
                            + " </head>"
                            + "<body><div class=context style=display:none><div><div class=selectionVal ></div></div><div class=explainResponse></div><div ><input type=button class=explainBtn value=explain></div></div>"
                            + "<div class='Wrapper' style='line-height:40px;width:860px;margin:0 auto;padding:25px 30px;text-align:justify'>"
                            + "<style>"
                            + Server.readFile("src/main/resources/style.css")
                            + outputHeader
                            + "</style>"
                            + outputBuffer
                            + "</div>"
                            + "</body>"
                            + "</html>";
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    return "error";
                }

            }));

            web.start();

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
