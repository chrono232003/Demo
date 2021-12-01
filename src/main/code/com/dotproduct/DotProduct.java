package com.dotproduct;

import java.util.Vector;
import java.util.stream.IntStream;

public class DotProduct {

    public static Integer computeDotProduct(Vector<Integer> a, Vector<Integer> b) {
        return IntStream.range(0, a.size()).mapToObj(i -> a.get(i) * b.get(i)).reduce(0, Integer::sum);
    }
}
