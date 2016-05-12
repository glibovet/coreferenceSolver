package textanalysis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.languagetool.*;
import org.languagetool.language.Ukrainian;
import textanalysis.framework.Client;
import textanalysis.framework.Route;
import textanalysis.framework.Server;

import textanalysis.pipeline.Rule;
import textanalysis.pipeline.StepHandler;
import textanalysis.pipeline.TokenBag;

import textanalysis.pipeline.operations.*;

public class Main {

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

    public static void main(String[] args) throws IOException {

        try {

            Server web = new Server(8080);

            web.register(new Route("/home", (Client c) -> {

//                ObjectMapper om = new ObjectMapper();
//                try {
//                    System.out.println(om.writeValueAsString(c.Request));
//                } catch (JsonProcessingException ex) {
//                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//                }
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

                    JLanguageTool langTool = new JLanguageTool(new Ukrainian());

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

                try {

                    JLanguageTool langTool = new JLanguageTool(new Ukrainian());

                    // noun rules 
                    Rule nounAdjAny = new Rule("noun_adj_rodoviy", "noun").then("adj").then(new Any());
                    Rule nounAdj = new Rule("noun_adj", "noun").then("adj");
                    Rule adjectiveNoun = new Rule("adj_noun", "adj").then("noun");
                    Rule longNouns = new Rule("adj_2nouns", "adj").then(2, "noun");
                    Rule lngNAN = new Rule("middle_adj", "noun").then("adj").then("noun");
                    Rule outerNoun = new Rule("outer_noun", "noun").then(new Any()).then("noun");

                    Rule pronoun = new Rule("pronoun", "pron");
                    Rule pron_verb = new Rule("pron_noun", "pron").then("verb");

                    Rule verb = new Rule("verb", new OneOf(new HasTag("verb"), new HasTag("adjp"), new HasTag("advp")));

                    List<AnalyzedSentence> sentences = langTool.analyzeText(c.Request.param("text"));

                    StringBuilder outputBuffer = new StringBuilder();
                    StringBuilder outputHeader = new StringBuilder();

                    ArrayList<String> tokensOutput = new ArrayList<>();

                    TokenPipeline pipeline = new TokenPipeline(new TokenBag(sentences), new StepHandler() {

                        @Override
                        public void handle() {

                            checkRule(pron_verb);

                            checkRule(longNouns);
                            checkRule(lngNAN);
                            checkRule(nounAdjAny);
                            checkRule(outerNoun);
                            checkRule(nounAdj);
                            checkRule(adjectiveNoun);

//                            checkRule(verb);
                            checkRule(pronoun);

                            if (!RuleMatched) {
                                tokensOutput.add(Current.getToken().getToken());
                            } else {

                                tokensOutput.add("<span class='pCorref subG" + MatchedRule.getName().substring(0, 4) + " correfGrou" + Pipeline.sentenceIndex + "'><span>" + MatchedRule.lastMatches().toString() + "</span><div class=groupPopup style=display:none>[" + MatchedRule.getName() + ":" + MatchedRule.lastMatches().toStringAdvanced() + "]</div></span>");
                            }

                        }
                    });

                    pipeline.start();

                    outputBuffer.append(String.join(" ", tokensOutput));

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
}
