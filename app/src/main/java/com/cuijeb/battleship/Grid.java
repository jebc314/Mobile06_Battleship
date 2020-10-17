package com.cuijeb.battleship;

public class Grid {
    // Actual board
    public int[][] grid;
    // Number of pieces
    public int numPieces;
    // Names of pieces
    public int[] pieceNames;
    // Sizes of pieces
    public int[] pieceSizes;
    // Lives left of pieces
    public int[] pieceLives;
    // Hit/Miss baord
    public int[][] gridHM;

    // Default constructor idk
    public Grid(){}
    // Three argument constructor with size 5
    public Grid(int[][] grid, int[] pieceNames, int[] pieceSizes) {
        this.grid = grid;
        this.pieceNames = pieceNames;
        this.pieceSizes = pieceSizes;
        this.numPieces = 2;
        // Pieces lives starts off same as pieceSize
        pieceLives = new int[this.pieceSizes.length];
        for (int i = 0; i < this.pieceSizes.length; i++){
            pieceLives[i] = pieceSizes[i];
        }
        gridHM = new int[grid.length][grid[0].length];
    }
    //Four argument for all necessary arguments
    public Grid(int[][] grid, int[] pieceNames, int[] pieceSizes, int size) {
        this.grid = grid;
        this.pieceNames = pieceNames;
        this.pieceSizes = pieceSizes;
        this.numPieces = size;
        // Pieces lives starts off same as pieceSize
        pieceLives = new int[this.pieceSizes.length];
        for (int i = 0; i < this.pieceSizes.length; i++){
            pieceLives[i] = pieceSizes[i];
        }
        gridHM = new int[grid.length][grid[0].length];
    }

    // Methods
    // Precondition: Haven't tried r, c yet ==> prevent button attack from which you
    // haved attacked from before (meet precondition)
    public String enemysAttack(int r, int c) {
        // Get what piece or lack of piece (0 is empty) is at attack point
        int piece = grid[r][c];

        // If empty...
        if (piece == 0) {
            gridHM[r][c] = 1; // Mark as enemy missed
            // Tells enemy that they missed
            return "Miss";
        } else {
            // if not empty then...
            gridHM[r][c] = 2; // Marked as enemy hit
            // take away piece's life
            pieceLives[piece - 1]--;
            if (pieceLives[piece - 1] == 0) {
                // if the piece is dead
                numPieces--;
                // If there are no more pieces
                if (numPieces == 0){
                    return "You win!";
                    // Message to the enemy that they win
                }
                // Tells enemy which ship they sunk
                return "Sunk my " + piece + " ship";
            } else {
                // Tells enemy that they hit
                return "Hit!";
            }
        }
    }
    // Method that checks if this play/baord is dead
    public boolean isDead() {
        return numPieces == 0;
    }

    // Same precondition as enemysAttack
    // Precondition: Haven't tried r, c yet ==> prevent button attack from which you
    // haved attacked from before (meet precondition)
    public String attackEnemy(Grid enemy, int r, int c) {
        // The response from attacking the enemy
        String response = enemy.enemysAttack(r, c);
        return response;
    }

    // generates a random grid
    public void generateGrid(){
        for(int i = 0; i < pieceNames.length; i++) {

        }
    }
    // Places a piece, if able to put down, then returns true
    // If not able to put down, then returns false
    public boolean placePiece(int pieceName, int r, int c) {
        return false;
    }
}
