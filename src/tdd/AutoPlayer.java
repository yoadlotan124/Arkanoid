package tdd;

import java.util.ArrayList;
import java.util.Random;

/**
 * Represents an automated player in the TicTacToe game.
 * The tdd.AutoPlayer selects a random free position on the board for its move.
 * This player simulates a human player by making moves without any strategic logic.
 */
public class AutoPlayer extends Player {

    /**
     * Constructs an tdd.AutoPlayer with the specified name, id, and marker.
     *
     * @param name   the name of the player
     * @param id     the unique identifier for the player
     * @param marker the character used to mark the player's moves on the board
     */
    public AutoPlayer(String name, int id, char marker) {
        super(name, id, marker);
    }

    /**
     * Makes a move by selecting a random valid free position on the board.
     * The method gathers all free positions, randomly chooses one,
     * and places the player's marker there.
     *
     * @param board the game board on which the move is made
     */
    @Override
    public void move(Board board) {
        ArrayList<Integer> free = new ArrayList<Integer>();
        for (int i = 0; i < board.getBoardSize();  i++) {
            for (int j = 0; j < board.getBoardSize(); j++) {
                if (board.isValidPosition(String.valueOf(i * board.getBoardSize() + j + 1))) {
                    free.add(i * board.getBoardSize() + j + 1);
                }
            }
        }
        System.out.println("Player " + this.getName() + ", please enter your move. (enter a value from 1 - "
                + board.getBoardSize() * board.getBoardSize() + ")");
        board.print();
        Random rand = new Random();
        int randomIndex = rand.nextInt(free.size());
        int chosenPos =  free.get(randomIndex);
        board.placeTheMove(this.getMarker(), chosenPos);
    }
}