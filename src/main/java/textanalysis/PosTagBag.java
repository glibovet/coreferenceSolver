package textanalysis;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class PosTagBag implements Iterable {

    private HashSet<String> tags = new HashSet<String>();

    public void add(String input) {
        if (input != null && !input.isEmpty()) {
            String[] tagArray = input.split(":");
            for (String it : tagArray) {
                //    if (it.startsWith("&")) {
//                    this.tags.add(it.substring(1));
                //  } else {
                this.tags.add(it);
                //}
            }
        }
    }

    public PosTagBag(String input) {
        this.add(input);
    }
    
    // add some tricks from possibility theory
    public boolean hasTag(String tagName) {
        boolean first = this.tags.contains(tagName);
        if (!first) {
            return this.tags.contains("&" + tagName);
        } else {
            return first;
        }
    }

    @Override
    public Iterator iterator() {
        return this.tags.iterator();
    }

    public String toString() {
        List l = new ArrayList<String>();
        l.addAll(this.tags);
        return l.toString();
    }

    public boolean hasOneOf(String[] tags) {
        for (String tag : tags) {
            if (this.hasTag(tag)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasNot(String tag) {
        return !this.hasTag(tag);
    }

    public boolean hasAll(String[] tags) {

        int countFound = 0;

        for (String tag : tags) {
            if (this.hasTag(tag)) {
                countFound++;
                if (countFound == tags.length) {
                    return true;
                }
            }
        }

        return countFound == tags.length;
    }

}
