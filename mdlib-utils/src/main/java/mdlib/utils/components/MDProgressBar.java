package mdlib.utils.components;

import javafx.animation.AnimationTimer;
import javafx.beans.binding.Bindings;
import javafx.scene.layout.Pane;

public final class MDProgressBar extends Pane {

    /**
     * Represents that progress bar is in inderminate state. Pass this value to
     * {@link #setValue(double)}, if you want to have progress bar been in this
     * state.
     */
    public static final double INDETERMINATE = -1D;

    private Animator animator;
    private double value;
    private final PBAnimationTimer internalAnimationTimer;

    /**
     * Returns the value of the progress bar as {@code double} in range from 0 to 1
     * (inclusive), or {@link #INDETERMINATE}, if the progress bar is in
     * indeterminate state.
     *
     * @return the value of the progress bar
     */
    public double getValue() {
        return value;
    }

    /**
     * Sets the value of the progress bar. Possible values are from range from 0 to
     * 1 inclusive, or {@link #INDETERMINATE} if the progress bar should be in
     * indeterminate state.
     * @param value value to be set
     */
    public void setValue(final double value) {
        if (value != INDETERMINATE && (value < 0 || value > 1))
            throw new IllegalArgumentException("Illegal progress bar value: " + value);
        this.value = value;
    }

    /**
     * Returns the {@link AnimationTimer} that is used to render the progress bar.
     * @return {@link AnimationTimer} for rendering the progress bar
     */
    public Animator getAnimator() {
        return animator;
    }

    /**
     * Sets the {@link Animator} for the progress bar.
     * @param animator {@link Animator} instance to be set
     * @throws NullPointerException if {@code null} is given as {@link Animator}
     */
    public void setAnimator(final Animator animator) {
        if (animator == null)
            throw new NullPointerException("Animator cannot be null");
        this.animator = animator;
    }

    /**
     * Creates new progress bar with initial value set to zero.
     */
    public MDProgressBar(final Animator animator) {
        this(INDETERMINATE, animator);
    }

    /**
     * Creates new progress bar using given progress bar value.
     *
     * @param value the value of the new progress bar
     * @throws IllegalArgumentException if given progress bar value is invalid
     *
     * @see #getValue()
     * @see #setValue(double)
     */
    public MDProgressBar(final double value, final Animator animator) {

        internalAnimationTimer = new PBAnimationTimer(this);
        Bindings.selectBoolean(this.sceneProperty(), "window", "showing").addListener((observable, wasShown, isShown) -> {
            if (isShown) internalAnimationTimer.start();
            else internalAnimationTimer.stop();
        });

        setValue(value);
        setAnimator(animator);
    }

    /**
     * Interface for making {@link MDProgressBar} animatable.
     *
     * @see #refresh(long, MDProgressBar)
     */
    public interface Animator {
        /**
         * Used to refresh given {@link MDProgressBar} at given system time in nanoseconds
         * @param nanoTime current system time in nanoseconds
         * @param progressBar progress bar to be rendered
         */
        void refresh(long nanoTime, MDProgressBar progressBar);
    }

    private static final class PBAnimationTimer extends AnimationTimer {
        private final MDProgressBar progressBar;

        PBAnimationTimer(final MDProgressBar progressBar) {
            this.progressBar = progressBar;
        }

        @Override
        public void handle(final long now) {
            progressBar.getAnimator().refresh(now, progressBar);
        }
    }
}
