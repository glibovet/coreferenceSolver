/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Coreference;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.logging.Level;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.sentenceiterator.BasicLineIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Lenovo
 */
public class Word2VecTest {

    public Word2VecTest() {
    }

    private static Logger log = LoggerFactory.getLogger(Word2VecTest.class);

    @Test
    public void distance() {

        try {
            
            // Gets Path to Text file
            String sourcesDir = "C:\\course\\";
            String filePath = sourcesDir + "merged.txt";

            log.info("Load & Vectorize Sentences....");
            // Strip white space before and after for each line
            SentenceIterator iter = new BasicLineIterator(filePath);
            // Split on white spaces in the line to get words
            TokenizerFactory t = new DefaultTokenizerFactory();

            /*
            CommonPreprocessor will apply the following regex to each token: [\d\.:,"'\(\)\[\]|/?!;]+
            So, effectively all numbers, punctuation symbols and some special symbols are stripped off.
            Additionally it forces lower case for all tokens.
             */
            t.setTokenPreProcessor(new CommonPreprocessor());

            log.info("Building model....");
            try {
                Word2Vec vec = new Word2Vec.Builder()
                        .minWordFrequency(5)
                        .iterations(1)
                        .layerSize(100)
                        .seed(42)
                        .windowSize(5)
                        .iterate(iter)
                        .tokenizerFactory(t)
                        .build();

                log.info("Fitting Word2Vec model....");
                vec.fit();

                log.info("Writing word vectors to text file....");

                // Write word vectors to file
                WordVectorSerializer.writeWordVectors(vec, "pathToWriteto.txt");

                // Prints out the closest 10 words to "day". An example on what to do with these Word Vectors.
                log.info("Closest Words:");
                Collection<String> lst = vec.wordsNearest("day", 10);
                System.out.println("10 Words closest to 'day': " + lst);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }

        } catch (FileNotFoundException ex) {
            java.util.logging.Logger.getLogger(Word2VecTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
