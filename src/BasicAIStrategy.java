import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collections;

/**
 * A basic AI strategy that selects the first playable card and chooses wild color
 * based on frequency of colors in hand.
 */
public class BasicAIStrategy implements PlayerStrategy {
    @Override
    public Card chooseCard(UnoGame gameController, Hand hand, List<Card> playableCards, Card topPileCard, Color activeWildColor) {
        return playableCards.isEmpty() ? null : playableCards.get(0);
    }

    @Override
    public Color chooseWildColor(UnoGame gameController, Hand hand) {
        Map<Color, Integer> colorCount = new HashMap<>();
        for (Card card : hand.getCards()) {
            if (card.getColor() != Color.WILD) {
                colorCount.put(card.getColor(), colorCount.getOrDefault(card.getColor(), 0) + 1);
            }
        }

        return colorCount.isEmpty()
            ? Color.RED // Default
            : Collections.max(colorCount.entrySet(), Map.Entry.comparingByValue()).getKey();
    }
}
