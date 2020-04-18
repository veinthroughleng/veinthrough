package veinthrough.api.generic;

import lombok.*;

/**
 * @author veinthrough
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pair<T> {
    @NonNull @Getter @Setter
    private T first;
    @NonNull @Getter @Setter
    private T second;

    @SuppressWarnings("unused")
    public void reverse() {
        T t = first;
        first = second;
        second = t;
    }

    @Override
    public String toString() {
        return "<" + getFirst().toString() + ","
                + getSecond().toString() + ">";
    }
}
