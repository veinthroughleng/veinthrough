package veinthrough.api.generic;

import com.google.common.base.Preconditions;
import com.google.common.collect.Ordering;

import java.util.Arrays;
import java.util.Comparator;

public class PairAlg {
    // <T extends Comparable<? super T>
    @SuppressWarnings({"SameParameterValue", "unused"})
    public static <T extends Comparable<? super T>> Pair<T> minMax(T[] a) {
        Preconditions.checkArgument(a != null && a.length > 0);
        return minMax(Arrays.asList(a));
    }

    // <T extends Comparable<T>
    @SuppressWarnings({"SameParameterValue"})
    @Deprecated
    public static <T extends Comparable<T>> Pair<T> minMax2(T[] a) {
        Preconditions.checkArgument(a != null && a.length > 0);
        return minMax2(Arrays.asList(a));
    }


    // <T extends Comparable<? super T>
    public static <T extends Comparable<? super T>> Pair<T> minMax(Iterable<T> a) {
        Preconditions.checkArgument(a != null && a.iterator().hasNext());

        // [OrderingTest].1. convert Comparator<T> to Ordering<T>
        // [OrderingTest].4. Comparator.naturalOrder(), 使用既有的compareTo() from Comparable<T>作为comparator
        Ordering<T> ordering = Ordering.<T>from(
                Comparator.naturalOrder());

        // [OrderingTest].1. convert Comparator<T> to Ordering<T>
//        Ordering<T> ordering = Ordering.<T>from(
//                (left, right) -> left.compareTo(right));

        // [OrderingTest].2. implement the abstract Ordering<T> by override compare()
//        Ordering<T> ordering = new Ordering<T>() {
//            @Override
//            public int compare(T arg0, T arg1) {
//                return arg0.compareTo(arg1);
//            }
//        };

        // [OrderingTest]. 10.min()/max(): 使用生成的ordering来计算min/max
        return Pair.<T>builder()
                .first(ordering.min(a))
                .second(ordering.max(a))
                .build();
    }

    // <T extends Comparable<T>
    @Deprecated
    public static <T extends Comparable<T>> Pair<T> minMax2(Iterable<T> a) {
        Preconditions.checkArgument(a != null && a.iterator().hasNext());

        // [OrderingTest].3. natural(), 使用既有的compareTo() from Comparable<T>
        // [OrderingTest].10.min()/max(): 使用生成的ordering来计算min/max
        Ordering<T> ordering = Ordering.natural();
        return Pair.<T>builder()
                .first(ordering.min(a))
                .second(ordering.max(a))
                .build();
    }
}