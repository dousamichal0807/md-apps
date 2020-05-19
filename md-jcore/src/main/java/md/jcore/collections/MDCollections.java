package md.jcore.collections;

public final class MDCollections {

    // Do not create instances!
    private MDCollections() {
    }

    public static <E> BasicTree.Unmodifiable<E> unmodifiableTree(BasicTree<E> tree) {
        return (tree == null ? null : new BasicTree.Unmodifiable<>(tree));
    }
}
