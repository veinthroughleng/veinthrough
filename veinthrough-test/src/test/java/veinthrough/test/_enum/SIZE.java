package veinthrough.test._enum;

import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Map;

/**
 * @author veinthrough
 *
 * A enum implementation, in which value use Pair
 */
public enum SIZE {
    INVALID(Integer.MIN_VALUE, 0),
    TOO_SMALL(0, 1),
    SMALL(1, 10),
    MEDIUM(10, 20),
    LARGE(20, 50),
    EXTRA_LARGE(50, 100),
    TOO_LARGE(100, Integer.MAX_VALUE);

    @Getter
    private Pair<Integer, Integer> scope;
    private static final Map<Integer, SIZE> VALUE_MAP;

    static {
        final ImmutableMap.Builder<Integer, SIZE> lefts = ImmutableMap.builder();
        for (SIZE enumItem : SIZE.values()) {
            lefts.put(enumItem.getScope().getLeft(), enumItem);
        }
        VALUE_MAP = lefts.build();
    }

    SIZE(Integer minimum, Integer maximum) {
        this.scope = ImmutablePair.of(minimum, maximum);
    }

    public Pair<Integer, Integer> getValue() {
        return this.getScope();
    }

    public String getScopeString() {
        return "[" + this.scope.getLeft() + "," +
                this.scope.getRight() +
                ")";
    }

    @Override
    public String toString() {
        return this.name().toLowerCase() + getScopeString();
    }

    public static SIZE forValue(Integer value) throws InvalidSizeException {
        return VALUE_MAP.get(
                VALUE_MAP.keySet().stream()
                        .filter(left -> left <= value)
                        .max(Integer::compare)
                        .orElseThrow(() ->
                                new InvalidSizeException("size should between(0, Integer.MAX_VALUE)")));
    }

    @NoArgsConstructor
    public static class InvalidSizeException extends Exception {
        private InvalidSizeException(String message) {
            super(message);
        }
    }
}
