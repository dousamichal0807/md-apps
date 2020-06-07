package mdlib.utils;

import java.util.Objects;

public interface Filter<V> {
    public boolean offer(V value);

    public static final Filter<Object> FILTER_NOT_NULL = Objects::nonNull;
}
