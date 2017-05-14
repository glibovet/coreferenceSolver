
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import textanalysis.ng.ArrayPermutator;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Lenovo
 */
public class ArrayPermutatorTest {

//    @Test
    public void permutate() {

        List<List<String>> listToPermutate = new ArrayList();

        listToPermutate.add(Arrays.asList("Сергій", "Cерхіо"));
        listToPermutate.add(Arrays.asList("Олег", "Алежка", "Oleh"));
        listToPermutate.add(Arrays.asList("Semen", "Cьома", "Алексеєвіч"));
         listToPermutate.add(Arrays.asList("galya", "галя"));

//        System.out.println();
        
        
        for (List<String> it:ArrayPermutator.permutate(listToPermutate)) {
            System.out.println(it);
        }
        
    }
}
