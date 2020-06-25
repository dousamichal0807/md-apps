package md.jgames.jchess.testing;

import md.jgames.jchess.logic.Move.PawnPromotion;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PawnPromotionHashCodeTest {
    @Test
    public void test() {
        for (int i = 0; i < PawnPromotion.values().size(); i++) {
            assertEquals(i, PawnPromotion.values().get(i).hashCode(), "Invalid value at value with index " + i);
        }
    }
}
