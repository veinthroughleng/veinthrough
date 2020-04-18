package veinthrough.api._interface;

import java.util.function.Supplier;

public interface Condition extends Supplier<Boolean> {
    default Boolean met() {
        return this.get();
    }
}
