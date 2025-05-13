
/**
 * Represents an action card in an Uno game (Skip, Reverse, Draw Two).
 *
 * Author: Andrew Nell
 * Student ID: 110450836
 * Email ID: NELAY007
 * AI Tools Used: None
 * This is my own work as defined by the University's Academic Integrity Policy.
 */

public class ActionCard extends Card {
    public ActionCard(Color color, Value value) {
        super(color, value);
        if (!(value == Value.SKIP || value == Value.REVERSE || value == Value.DRAW_TWO)) {
            throw new IllegalArgumentException("ActionCard must be SKIP, REVERSE, or DRAW_TWO, not " + value);
        }
        if (color == Color.WILD) {
            throw new IllegalArgumentException("ActionCard cannot have WILD color");
        }
    }

    @Override
    public void applyEffect(Game gameController) {
        switch (this.value) {
            case SKIP:
                gameController.skipNextPlayerTurn();
                break;
            case REVERSE:
                gameController.reversePlayDirection();
                if (gameController.getNumberOfPlayers() == 2) {
                    gameController.skipNextPlayerTurn();
                }
                break;
            case DRAW_TWO:
                Player nextPlayer = gameController.getNextPlayer();
                gameController.makePlayerDraw(nextPlayer, 2);
                gameController.skipNextPlayerTurn();
                break;
            default:
                break;
        }
    }
}
