/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.zunama.Dawg;
import java.util.Arrays;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;


public class DafsaTest {
    
    @Test
    public void testDafsaDict() {
        
        
       String[] words = new String[]{
            "привіт",
            "світ",
            "як",
            "твої",
            "спарви",
            "молоко",
            "миша"
        };
       
       
        String[] wordsEn = new String[]{
            "hello",
            "world",
            "how",
            "its",
            "hell",
            "going",
            "iterable",
            "gong"
        };
        
        
//        Path path = Paths.get("/path/to/wordlist.txt");
//        List<String> words = Files.readAllLines(path, StandardCharsets.UTF_8);
        
        
        Dawg dawg = new Dawg(Arrays.asList(words));
        
        for (String w: words) {
            assertTrue("Cannot find `"+w+"` in dict = ",dawg.search(w));
        }
        
        assertFalse(dawg.search("тво"));
        
    }
}
