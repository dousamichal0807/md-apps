package mdlib.utils;

import java.util.function.Predicate;

public final class FilteredAtomicReference<V> {

    private final Predicate<? super V> filter;
    private V value;

    public FilteredAtomicReference(final Predicate<? super V> filter, final V initialValue) {
        this.filter = filter;
        this.set(initialValue);
    }

    public V get() {
        return value;
    }

    public V set(final V value) {
        if (filter != null && !filter.test(value))
            throw new IllegalArgumentException("Value did not pass through filter: " + value);

        V prev = this.value;
        this.value = value;
        return prev;
    }
}
