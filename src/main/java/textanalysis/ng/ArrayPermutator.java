package textanalysis.ng;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ArrayPermutator {

    public static <T> List<List<T>> permutate(List<List<T>> lists) {
        List<List<T>> permutations = new ArrayList<>();
        List<Integer> indices = new ArrayList<>(Collections.nCopies(lists.size(), 0));
        do {
            permutations.add(collect(lists, indices));
        } while (next(lists, indices));
        return permutations;
    }

    private static <T> List<T> collect(List<List<T>> lists, List<Integer> indices) {
        List<T> permutation = new ArrayList<T>(lists.size());
        for (int i = 0; i < lists.size(); ++i) {
            permutation.add(i, lists.get(i).get(indices.get(i)));
        }
        return permutation;
    }

    private static <T> boolean next(List<List<T>> lists, List<Integer> indices) {
        int i = 0, j;
        do {
            j = (indices.get(i) + 1) % lists.get(i).size();
            indices.set(i, j);
            ++i;
        } while (j == 0 && i < lists.size());
        return j != 0;
    }

}
