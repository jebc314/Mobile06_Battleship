package com.cuijeb.battleship;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;

import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {

    // The grid's textviews
    private TextView[][] grid;
    // The number array version of what is on screen
    private int[][] numGrid = new int[5][5];
    // The label saying what the current piece is
    private TextView currentPieceLabel;
    // Pieces names like 1,2,..
    private int[] pieceNames;
    // The respective sizes for the pieces in pieceNames
    private int[] pieceSizes;
    // Which piece is being placed right now
    private int piecePos = 0;

    // Boolean for which way to place the piece
    private boolean isHorizontal = true;
    //The horizontal and verical buttons that switch isHorizontal
    private Button horizontalButton;
    private Button verticalButton;

    // Start Button
    private Button startButton;

    // Settings button
    private Button settingsButton;

    // Shared preferences
    private SharedPreferences sharedPreferences;

    // Fragment manager
    private FragmentManager fm;

    // Constraint layout
    private ConstraintLayout constraintLayout;

    // Size of grid
    private int gridSize;

    @Override
    protected void onRestart() {
        super.onRestart();
        // Set the background color
        String backgroundColor = sharedPreferences.getString("background_color", "blue");
        // View root = settingsButton.getRootView();
        switch(backgroundColor) {
            case "red":
                constraintLayout.setBackgroundColor(getResources().getColor(R.color.holo_red_light));
                break;
            case "green":
                constraintLayout.setBackgroundColor(getResources().getColor(R.color.holo_green_light));
                break;
            case "blue":
                constraintLayout.setBackgroundColor(getResources().getColor(R.color.holo_blue_light));
                break;
        }
        // Set Text Color
        String textColor = sharedPreferences.getString("text_color", "black");
        switch (textColor) {
            case "black":
                startButton.setTextColor(Color.BLACK);
                currentPieceLabel.setTextColor(Color.BLACK);
                horizontalButton.setTextColor(Color.BLACK);
                verticalButton.setTextColor(Color.BLACK);
                for (TextView[] r: grid)
                    for (TextView c: r)
                        c.setTextColor(Color.BLACK);
                break;
            case "white":
                startButton.setTextColor(Color.WHITE);
                currentPieceLabel.setTextColor(Color.WHITE);
                horizontalButton.setTextColor(Color.WHITE);
                verticalButton.setTextColor(Color.WHITE);
                break;
            case "blue":
                startButton.setTextColor(Color.BLUE);
                currentPieceLabel.setTextColor(Color.BLUE);
                horizontalButton.setTextColor(Color.BLUE);
                verticalButton.setTextColor(Color.BLUE);
                for (TextView[] r: grid)
                    for (TextView c: r)
                        c.setTextColor(Color.BLUE);
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Set the background color
        String backgroundColor = sharedPreferences.getString("background_color", "blue");
        // View root = settingsButton.getRootView();
        switch(backgroundColor) {
            case "red":
                constraintLayout.setBackgroundColor(getResources().getColor(R.color.holo_red_light));
                break;
            case "green":
                constraintLayout.setBackgroundColor(getResources().getColor(R.color.holo_green_light));
                break;
            case "blue":
                constraintLayout.setBackgroundColor(getResources().getColor(R.color.holo_blue_light));
                break;
        }
        // Set Text Color
        String textColor = sharedPreferences.getString("text_color", "black");
        switch (textColor) {
            case "black":
                startButton.setTextColor(Color.BLACK);
                currentPieceLabel.setTextColor(Color.BLACK);
                horizontalButton.setTextColor(Color.BLACK);
                verticalButton.setTextColor(Color.BLACK);
                for (TextView[] r: grid)
                    for (TextView c: r)
                        c.setTextColor(Color.BLACK);
                break;
            case "white":
                startButton.setTextColor(Color.WHITE);
                currentPieceLabel.setTextColor(Color.WHITE);
                horizontalButton.setTextColor(Color.WHITE);
                verticalButton.setTextColor(Color.WHITE);
                break;
            case "blue":
                startButton.setTextColor(Color.BLUE);
                currentPieceLabel.setTextColor(Color.BLUE);
                horizontalButton.setTextColor(Color.BLUE);
                verticalButton.setTextColor(Color.BLUE);
                for (TextView[] r: grid)
                    for (TextView c: r)
                        c.setTextColor(Color.BLUE);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setting current piece label / piece tracker
        currentPieceLabel = findViewById(R.id.piecepos_textview);
        // Gets the piece's names
        String[] stringNames = getResources().getStringArray(R.array.pieces);
        // Converts the piece's names to int in int array
        pieceNames = new int[stringNames.length];
        for (int i = 0; i<stringNames.length; i++)
            pieceNames[i] = Integer.parseInt(stringNames[i]);
        // Get's the sizes and puts it in int array
        String[] stringSizes = getResources().getStringArray(R.array.pieceSizes);
        pieceSizes = new int[stringSizes.length];
        for (int i = 0; i<stringSizes.length; i++)
            pieceSizes[i] = Integer.parseInt(stringSizes[i]);
        // Shows user which piece it is currently on and the piece's size
        currentPieceLabel.setText(pieceNames[piecePos] + " Size: " + pieceSizes[piecePos]);

        // get sharedpreferences
        sharedPreferences = getSharedPreferences("Settings", Context.MODE_PRIVATE);

        // Grid size from preferences
        gridSize = 5;//sharedPreferences.getInt("grid_size", 5);
        grid = new TextView[gridSize][gridSize];

        // getting grid pieces
        for (int row = 0; row < gridSize; row++) {
            for (int column = 0; column < gridSize; column++) {
                // Get's ID of textviews and puts reference in grid textview array
                String id = "textView"+ (row * gridSize + column);
                int resID = getResources().getIdentifier(id, "id", getPackageName());
                grid[row][column] = findViewById(resID);

                // Add On Click Listener to the textViews
                grid[row][column].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // If the piece is to be placed horizontally
                        if (isHorizontal) {
                            // Number of tiles (of the current piece)
                            // left and right of the one you tapped
                            // Size 2 --> -0
                            // Size 3 --> 0-0
                            // Size 4 --> 0-00 etc...
                            int leftSize = (pieceSizes[piecePos] - 1) / 2;
                            int rightSize = pieceSizes[piecePos] - leftSize - 1;

                            // Get the position of the tile
                            Resources res = v.getResources();
                            int pos = Integer.parseInt(res.getResourceEntryName(v.getId()).substring(8));
                            int row = pos / gridSize;
                            int col = pos % gridSize;

                            // Check if the tile's position means you can place the piece
                            if (col >= leftSize && col + rightSize < gridSize) {
                                // Check if the tiles around the one you clicked are empty
                                boolean isEmpty = true;
                                for (int i = col - leftSize; i <= col + rightSize; i++) {
                                    if (numGrid[row][i] != 0) {
                                        isEmpty = false;
                                    }
                                }
                                // If it's empty ==> you can place the piece
                                if (isEmpty) {
                                    // See if you need to remove the previous piece position
                                    for (int i = 0; i < gridSize; i++) {
                                        for (int j = 0; j < gridSize; j++) {
                                            if (numGrid[i][j] == pieceNames[piecePos]) {
                                                numGrid[i][j] = 0;
                                                grid[i][j].setText("");
                                            }
                                        }
                                    }
                                    // Put piece where you tapped
                                    for (int i = col - leftSize; i <= col + rightSize; i++) {
                                        numGrid[row][i] = pieceNames[piecePos];
                                        grid[row][i].setText("" + pieceNames[piecePos]);
                                    }
                                    // Move to next piece and display next piece's information
                                    piecePos++;
                                    // If all pieces have been placced...
                                    if (piecePos >= pieceNames.length)
                                        startButton.setEnabled(true);
                                    piecePos %= pieceNames.length;
                                    currentPieceLabel.setText(pieceNames[piecePos] + " Size: " + pieceSizes[piecePos]);
                                }
                            }
                        // Same thing for vertical just little tweaks
                        } else {
                            int upSize = (pieceSizes[piecePos] - 1) / 2;
                            int bottomSize = pieceSizes[piecePos] - upSize - 1;

                            Resources res = v.getResources();
                            int pos = Integer.parseInt(res.getResourceEntryName(v.getId()).substring(8));
                            int row = pos / gridSize;
                            int col = pos % gridSize;
                            if (row >= upSize && row + bottomSize < gridSize) {
                                boolean isEmpty = true;
                                for (int i = row - upSize; i <= row + bottomSize; i++) {
                                    if (numGrid[i][col] != 0) {
                                        isEmpty = false;
                                    }
                                }
                                if (isEmpty) {
                                    for (int i = 0; i < gridSize; i++) {
                                        for (int j = 0; j < gridSize; j++) {
                                            if (numGrid[i][j] == pieceNames[piecePos]) {
                                                numGrid[i][j] = 0;
                                                grid[i][j].setText("");
                                            }
                                        }
                                    }
                                    for (int i = row - upSize; i <= row + bottomSize; i++) {
                                        numGrid[i][col] = pieceNames[piecePos];
                                        grid[i][col].setText("" + pieceNames[piecePos]);
                                    }
                                    piecePos++;
                                    // If all pieces have been placced...
                                    if (piecePos >= pieceNames.length)
                                        startButton.setEnabled(true);
                                    piecePos %= pieceNames.length;
                                    currentPieceLabel.setText(pieceNames[piecePos] + " Size: " + pieceSizes[piecePos]);
                                }
                            }
                        }
                    }
                });

                // Setting up horizontal and vertical buttons
                // to change the isHorizontal condition
                horizontalButton = findViewById(R.id.horizontal_button);
                horizontalButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isHorizontal = true;
                    }
                });
                verticalButton = findViewById(R.id.vertical_button);
                verticalButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isHorizontal = false;
                    }
                });

                // Test fragment using start button
                /*startButton = findViewById(R.id.start_button);
                startButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (findViewById(R.id.constraint_layout) != null){
                            SettingsFragment fragment = new SettingsFragment();
                            FragmentManager fm = getSupportFragmentManager();
                            FragmentTransaction ft = fm.beginTransaction();
                            ft.add(R.id.constraint_layout, fragment);
                            ft.addToBackStack(null);
                            ft.commit();
                        }
                    }
                });*/

                // Start the game by going to the game activity
                startButton = findViewById(R.id.start_button);
                startButton.setEnabled(false);
                startButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Intent to start game
                        Intent intent = new Intent(getApplicationContext(), BottomActivity.class);
                        // Put Initial Grid in intent
                        Gson gson = new Gson();
                        Grid initialGrid = new Grid(numGrid, pieceNames, pieceSizes);
                        String gridJson = gson.toJson(initialGrid);
                        intent.putExtra("bottom_grid", gridJson);
                        // Put the opponents grid in intent
                        int[][] opponentNumGrid = new int[gridSize][gridSize];
                        Grid opponentGrid = new Grid(opponentNumGrid, pieceNames, pieceSizes);
                        opponentGrid.generateGrid();
                        gridJson = gson.toJson(opponentGrid);
                        intent.putExtra("top_grid", gridJson);
                        // Put initial move
                        int[] move = {(int)(Math.random() * gridSize), (int)(Math.random() * gridSize)};
                        intent.putExtra("opponent_move", move);
                        startActivity(intent);
                        finish();
                    }
                });

                /*
                // Start settings fragment
                fm = getSupportFragmentManager();
                settingsButton = findViewById(R.id.settingsmain_button);
                settingsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (findViewById(R.id.constraint_layout) != null){
                            SettingsFragment fragment = new SettingsFragment();
                            FragmentTransaction ft = fm.beginTransaction();
                            ft.add(R.id.constraint_layout, fragment);
                            ft.addToBackStack(null);
                            ft.commit();
                        }
                    }
                });*/

                // Start settings acitivty
                settingsButton = findViewById(R.id.settingsmain_button);
                settingsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                        startActivity(intent);
                    }
                });

                // get background
                constraintLayout = findViewById(R.id.main_contraintlayout);

                /*// Whenever settings are changed
                fm.removeOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
                    @Override
                    public void onBackStackChanged() {
                        // Set the background color
                        String backgroundColor = sharedPreferences.getString("background_color", "blue");
                        View root = settingsButton.getRootView();
                        switch(backgroundColor) {
                            case "red":
                                root.setBackgroundColor(getResources().getColor(R.color.holo_red_light));
                                break;
                            case "green":
                                root.setBackgroundColor(getResources().getColor(R.color.holo_green_light));
                                break;
                            case "blue":
                                root.setBackgroundColor(getResources().getColor(R.color.holo_green_light));
                                break;
                        }
                    }
                });*/
            }


        }

    }
}