package veinthrough.api._interface;

import org.apache.commons.lang3.tuple.Pair;
import veinthrough.api.generic.Either;
import java.util.function.Function;
import java.util.function.ToIntFunction;

@FunctionalInterface
@SuppressWarnings("unused")
public interface CheckedFunction<T, R> {
    R apply(T t) throws Exception;

    static <T,R> Function<T,R> wrap(CheckedFunction<T, R> function) {
        return t -> {
            try {
                return function.apply(t);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    // 2. 将CheckedException包装成RuntimeException
    // 遇到CheckedException直接终止
    static <T> ToIntFunction<T> wrapInt(CheckedFunction<T,Integer> function) {
        return t -> {
            try {
                return function.apply(t);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    // 3. 将CheckedException放在返回值Either中,
    // 未抛出异常, 放入Either.right; 抛出异常, 放入Either.left
    // (1) Either.right只包含Exception
    // (2) Either.right包含出错的value和Exception, 使用Pair包装
    static <T, R> Function<T, Either> lift(CheckedFunction<T, R> function) {
        return t -> {
            try {
                return Either.right(function.apply(t));
            } catch (Exception ex) {
                return Either.left(ex);
            }
        };
    }

    static <T, R> Function<T, Either> liftWithValue(CheckedFunction<T, R> function) {
        return t -> {
            try {
                return Either.right(function.apply(t));
            } catch (Exception ex) {
                return Either.left(Pair.of(ex, t));
            }
        };
    }
}
