package org.example.appservlet.util;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class SetFromIterable {
    public static <T> Set<T> toSet(Iterable<T> i) {
        HashSet<T> set = new HashSet<>();
        for (Iterator<T> it = i.iterator(); it.hasNext();) {
            set.add(it.next());
        }
        return set;
    }

}
