/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tokenizer;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import textanalysis.ng.GrammarMatch;
import textanalysis.ng.Parser;
import textanalysis.ng.Rule.GrammarRuleI;
import static textanalysis.ng.helpers.GrammarRule.*;
import static textanalysis.ng.helpers.Label.*;

/**
 *
 * @author Lenovo
 */
public class ConvertInputTextsToSentences {

    @Test
    public void convert() throws IOException {

        String sourcesDir = "C:\\course\\";
        String articleMappingPath = sourcesDir + "mappings975d7b41-9c46-44da-b07c-5ac7e03f3a9b.txt";

        List<String> articleLines = Files.readAllLines(Paths.get(articleMappingPath));

        int ind = 0;

        GrammarRuleI sentenceRule = complex(
                "Sentence",
                optional(gram("QUOTE")),
                simple(capitalized()),
                repeatable(not(any(eq("."), eq("?"), eq("!")))),
                //                    any(
                //                        complex(
                //                            simple(not(longer(1))), 
                //                            repeatable(gram("PUNCTUATION"),any(eq("."),eq("?"),eq("!")))
                //                        ),
                //                        complex(
                repeatable(gram("PUNCTUATION"), any(eq("."), eq("?"), eq("!"))) //                        )
                //                    )
               
        );

        /**
         * Create parser instance with only one grammar
         */
        Parser entityParser = new Parser(sentenceRule);
        
        FileWriter f0 = new FileWriter(sourcesDir+"sentences.txt");
        
        for (String articleData : articleLines) {
            long start = System.currentTimeMillis();

            String[] exploded = articleData.split(" ", 2);

//            System.out.println("********************************");
//            System.out.println(exploded[0]);
//
//            System.out.println("********************************");

            String aContent = new String(Files.readAllBytes(Paths.get(sourcesDir + exploded[0] + ".txt")), Charset.forName("UTF-8"));

            List<GrammarMatch> tokens = entityParser.extract(aContent);
            int count = entityParser.extract(aContent).size();
            
            
            
            
            
            if (count > 0) {
                for (GrammarMatch gm : tokens) {                    
                     f0.write(gm.toString() + "\n");
                     f0.flush();
                }
            }
            
        }        
        
        f0.close();
    }
}
