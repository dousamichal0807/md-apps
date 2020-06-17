import md.jgames.jchess.logic.Move.PawnPromotion;

public class EnumHashCode {
    public static void main(final String[] args) {
        for (PawnPromotion value : PawnPromotion.values()) {
            System.out.print(value.toString());
            System.out.print(" -> ");
            System.out.println(value.hashCode());
        }
    }
}
