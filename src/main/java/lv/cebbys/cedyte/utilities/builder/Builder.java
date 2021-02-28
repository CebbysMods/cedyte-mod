package lv.cebbys.cedyte.utilities.builder;

import java.util.function.Consumer;

public interface Builder<T> extends Consumer<T> {
	default T build(T t) {
        this.accept(t);
        return t;
	}
}
