package projectone;

public class WildCard extends Card {
    public WildCard(Color color, Value value) {
        super(Color.WILD, value);
        
        if (color != Color.WILD) {
            throw new IllegalArgumentException("projectone.WildCard color must be WILD, not " + color);
        }

        if (!(value == Value.WILD || value == Value.WILD_DRAW_FOUR)) {
            throw new IllegalArgumentException("projectone.WildCard must be WILD or WILD_DRAW_FOUR, not " + value);
        }
    }

    @Override
    public void applyEffect(UnoGame gameController) {
        Player currentPlayer = gameController.getCurrentPlayer();
        Color chosenColor = currentPlayer.chooseWildColor(gameController);
        gameController.setActiveWildColor(chosenColor);
        System.out.println(currentPlayer.getName() + " chose " + chosenColor + " as the new wild color.");


        if (this.value == Value.WILD_DRAW_FOUR) {
            Player nextPlayer = gameController.getNextPlayer();
            System.out.println(nextPlayer.getName() + " draws four cards and is skipped.");
            gameController.makePlayerDraw(nextPlayer, 4);
            gameController.skipNextPlayerTurn();
        }
    }
}
