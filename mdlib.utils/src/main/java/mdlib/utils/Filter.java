package mdlib.utils;

import java.util.Objects;

public interface Filter<V> {
    boolean offer(V value);

    Filter<Object> FILTER_NOT_NULL = Objects::nonNull;
}
