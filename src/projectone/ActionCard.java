package projectone;

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
    /**
     * Constructs an projectone.ActionCard with the specified color and value.
     * projectone.ActionCard represents specific action cards in Uno, including SKIP, REVERSE, and DRAW_TWO.
     *
     * @param color the color of the action card; must not be WILD
     * @param value the value of the action card; must be one of SKIP, REVERSE, or DRAW_TWO
     * @throws IllegalArgumentException if the value is not SKIP, REVERSE, or DRAW_TWO
     * @throws IllegalArgumentException if the color is WILD
     */
    public ActionCard(Color color, Value value) {
        super(color, value);
        if (!(value == Value.SKIP || value == Value.REVERSE || value == Value.DRAW_TWO)) {
            throw new IllegalArgumentException("projectone.ActionCard must be SKIP, REVERSE, or DRAW_TWO, not " + value);
        }
        if (color == Color.WILD) {
            throw new IllegalArgumentException("projectone.ActionCard cannot have WILD color");
        }
    }

    @Override
    public void applyEffect(UnoGame gameController) {
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
