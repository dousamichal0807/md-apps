package mdlib.utils;

public final class FilteredAtomicReference<V> {

    private final Filter<? super V> filter;
    private V value;

    public FilteredAtomicReference(final Filter<? super V> filter, final V initialValue) {
        this.filter = filter;
        this.set(initialValue);
    }

    public V get() {
        return value;
    }

    public V set(final V value) {
        if (filter != null && !filter.offer(value))
            throw new IllegalArgumentException("Value did not pass through filter: " + value);

        V prev = this.value;
        this.value = value;
        return prev;
    }
}
