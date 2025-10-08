package tdd;

import java.util.Scanner;

/**
 * Represents a player in the TicTacToe game.
 * Each player has a unique ID, a name, a marker (e.g., 'X' or 'O'),
 * and keeps track of their number of wins.
 * Also handles player input for making moves on the board.
 */
public class Player {
    private final int id;
    private final String name;
    private final char marker;
    private final Scanner sc;
    private int numberOfWins;

    /**
     * Constructs a Player with the given name, ID, and marker.
     *
     * @param name   the player's name
     * @param id     the unique identifier for the player
     * @param marker the character marker representing the player on the board
     */
    public Player(String name, int id, char marker) {
        this.id = id;
        this.name = name;
        this.numberOfWins = 0;
        this.marker = marker;
        sc = new Scanner(System.in);
    }

    /**
     * Returns the player's unique ID.
     *
     * @return the player ID
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the player's name.
     *
     * @return the player name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the player's marker character.
     *
     * @return the player's marker
     */
    public char getMarker() {
        return this.marker;
    }

    /**
     * Returns the Scanner used for input.
     *
     * @return the Scanner object for reading input
     */
    public Scanner getSC() {
        return this.sc;
    }

    /**
     * Closes the Scanner resource used for input.
     */
    public void closeSC() {
        this.sc.close();
    }

    /**
     * Returns the number of wins this player has achieved.
     *
     * @return the number of wins
     */
    public int getNumberOfWins() {
        return this.numberOfWins;
    }

    /**
     * Increments the number of wins for this player by one.
     */
    public void incrementNumberOfWins() {
        this.numberOfWins++;
    }

    /**
     * Resets the player's number of wins to zero.
     */
    public void resetNumberOfWins() {
        this.numberOfWins = 0;
    }

    /**
     * Prompts the player to make a move on the given board.
     * The method validates the input position and updates the board accordingly.
     *
     * @param board the game board where the move will be placed
     */
    public void move(Board board) {
        String movePos;

        while (true) {
            System.out.println("Player " + this.name + ", please enter your move. (enter a value from 1 - "
                    + board.getBoardSize() * board.getBoardSize() + ")");
            board.print();

            if (sc.hasNextLine()) {
                movePos = sc.nextLine();

                if (!board.isValidPosition(movePos)) {
                    System.out.println("Invalid move. Please try again.");
                } else {
                    break;
                }
            }
        }

        board.placeTheMove(this.marker, Integer.parseInt(movePos));
    }
}
