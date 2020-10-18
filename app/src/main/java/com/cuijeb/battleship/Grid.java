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
        this.numPieces = pieceNames.length;
        // Pieces lives starts off same as pieceSize
        pieceLives = new int[this.pieceSizes.length];
        System.arraycopy(pieceSizes, 0, pieceLives, 0, this.pieceSizes.length);
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
        System.arraycopy(pieceSizes, 0, pieceLives, 0, this.pieceSizes.length);
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
        return enemy.enemysAttack(r, c);
    }

    // generates a random grid
    public void generateGrid(){
        for (int pieceName : pieceNames) {
            boolean placed;
            do {
                int r = (int) (Math.random() * grid.length);
                int c = (int) (Math.random() * grid.length);
                placed = placePiece(pieceName, r, c);
            } while (!placed);
        }
    }
    // Places a piece, if able to put down, then returns true
    // If not able to put down, then returns false
    public boolean placePiece(int pieceName, int row, int col) {
        int alignment = (int)(Math.random() * 2);
        boolean isHorizontal;
        isHorizontal = alignment == 1;

        // Grid Size for easy use
        int gridSize = grid.length;

        // If the piece is to be placed horizontally
        if (isHorizontal) {
            // Number of tiles (of the current piece)
            // left and right of the one you tapped
            // Size 2 --> -0
            // Size 3 --> 0-0
            // Size 4 --> 0-00 etc...
            int leftSize = (pieceSizes[pieceName - 1] - 1) / 2;
            int rightSize = pieceSizes[pieceName - 1] - leftSize - 1;

            // Check if the tile's position means you can place the piece
            if (col >= leftSize && col + rightSize < gridSize) {
                // Check if the tiles around the one you clicked are empty
                boolean isEmpty = true;
                for (int i = col - leftSize; i <= col + rightSize; i++) {
                    if (grid[row][i] != 0 && grid[row][i] != pieceNames[pieceName - 1]) {
                        isEmpty = false;
                        break;
                    }
                }
                // If it's empty ==> you can place the piece
                if (isEmpty) {
                    // Put piece where you selected
                    for (int i = col - leftSize; i <= col + rightSize; i++) {
                        grid[row][i] = pieceNames[pieceName - 1];
                    }
                    return true;
                }
            }
            // Same thing for vertical just little tweaks
        } else {
            int upSize = (pieceSizes[pieceName - 1] - 1) / 2;
            int bottomSize = pieceSizes[pieceName - 1] - upSize - 1;

            if (row >= upSize && row + bottomSize < gridSize) {
                boolean isEmpty = true;
                for (int i = row - upSize; i <= row + bottomSize; i++) {
                    if (grid[i][col] != 0 && grid[i][col] != pieceNames[pieceName - 1]) {
                        isEmpty = false;
                        break;
                    }
                }
                if (isEmpty) {
                    for (int i = row - upSize; i <= row + bottomSize; i++) {
                        grid[i][col] = pieceNames[pieceName - 1];
                    }
                    return true;
                }
            }
        }

        return false;
    }
}
