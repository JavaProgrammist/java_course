package ru.otus;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import java.util.List;

public class HelloOtus {

    public static void main(String[] args) {
        List<String> values = Lists.newArrayList("a", null, "b", "c");
        Iterable<String> withoutNulls = Iterables.filter(values, Predicates.notNull());
        withoutNulls.forEach(System.out::println);
    }
}
