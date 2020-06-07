package mdlib.jmath.math;

public abstract class MDNumberSet implements MDMathEntity {

    protected MDNumberSet() {
    }

    /**
     * Returns, if the set contains particular value.
     *
     * @param number the number to be tested if it is in the set
     * @return if {@code number} is found in the set
     */
    public abstract boolean contains(MDNumber number);

    /**
     * Returns, if the set is an empty set.
     *
     * @return if is an empty set
     */
    public abstract boolean isEmpty();

    public abstract static class Double implements MDMathEntity {

        protected Double() {
        }

        /**
         * Returns, if the set contains particular number.
         *
         * @param number the number to be tested if it is in the set
         * @return if {@code number} is found in the set
         */
        public abstract boolean contains(MDNumber.Double number);

        /**
         * Returns, if the set is an empty set.
         *
         * @return if is an empty set
         */
        public abstract boolean isEmpty();
    }
}
