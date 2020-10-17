
package com.cuijeb.battleship;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;


public class TopActivity extends AppCompatActivity {

    // The grid's textviews for HITS/MISSES
    private TextView[][] grid = new TextView[5][5];

    // The Intent from Setup Activity
    private Intent intent;
    // Grid object from intent
    private Grid top_grid;
    // ^ Will not display ships but will display it's receiving HITS/MISSES

    // Grid object of bottom grid or player grid
    private Grid bottom_grid;

    // Player attack on enemy
    private int[] playersAttack = {-1,-1};

    // Finish turn button
    private Button finishButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top);

        // Get intent with the info from the other screen
        intent =  getIntent();
        // Get Grid object from intent

        // Getting the grid from intent
        Gson gson = new Gson();
        String json = intent.getStringExtra("top_grid");
        top_grid = gson.fromJson(json, Grid.class);

        for (int row = 0; row < 5; row++) {
            for (int column = 0; column < 5; column++) {
                // Get's ID of textviews and puts reference in grid textview array
                String id = "top_textView" + (row * 5 + column);
                int resID = getResources().getIdentifier(id, "id", getPackageName());
                grid[row][column] = findViewById(resID);
                // Set grid's value

                // Set grid's background color for miss or hit by opponent
                if (top_grid.gridHM[row][column] != 0) {
                    if (top_grid.gridHM[row][column] == 1) {
                        grid[row][column].setBackgroundColor(Color.GRAY);
                    } else {
                        if (top_grid.pieceLives[top_grid.grid[row][column] - 1] == 0) {
                            grid[row][column].setText("" + top_grid.grid[row][column]);
                        }
                        grid[row][column].setBackgroundColor(Color.RED);
                    }
                    grid[row][column].setEnabled(false);
                }

                grid[row][column].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Get the position of the tile
                        Resources res = v.getResources();
                        int pos = Integer.parseInt(res.getResourceEntryName(v.getId()).substring(12));
                        int row = pos / 5;
                        int col = pos % 5;

                        // Clear the previous attack position
                        if (playersAttack[0] != -1){
                            grid[playersAttack[0]][playersAttack[1]].setBackgroundColor(Color.WHITE);
                        }

                        // Set players attack to cliked tile
                        playersAttack[0] = row;
                        playersAttack[1] = col;

                        // Select this tile using background color
                        grid[row][col].setBackgroundColor(Color.BLUE);

                        // Allow the finish button to work now
                        finishButton.setEnabled(true);
                    }
                });
            }
        }

        // Initializing the bottom grid
        json = intent.getStringExtra("bottom_grid");
        bottom_grid = gson.fromJson(json, Grid.class);

        // Finish turn button
        finishButton = findViewById(R.id.top_next_button);
        // finish button onclicklistener goes to player's move board activity
        // if playersAttack is null then don't allow switch
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get players attack position
                int r = playersAttack[0];
                int c = playersAttack[1];
                // Perform that attack
                String enemysResponse = bottom_grid.attackEnemy(top_grid, r, c);

                // Change grid tile color whether or not it was hit or miss
                if (top_grid.gridHM[r][c] == 1) {
                    grid[r][c].setBackgroundColor(Color.GRAY);
                } else {
                    // If that move sunk the ship at r,c
                    // then show piece name on all corresponding tiles
                    if (top_grid.pieceLives[top_grid.grid[r][c] - 1] == 0) {
                        for (int i = 0; i < 5; i++) {
                            for (int j = 0; j < 5; j++) {
                                if (top_grid.grid[i][j] == top_grid.grid[r][c])
                                    grid[i][j].setText("" + top_grid.grid[r][c]);
                            }
                        }
                    }
                    grid[r][c].setBackgroundColor(Color.RED);
                }
                // That title can't be clicked anymore
                grid[r][c].setEnabled(false);

                // Tell user Other's response
                Toast.makeText(
                        getApplicationContext(), "Other: " + enemysResponse, Toast.LENGTH_SHORT
                ).show();

                // Can't click finish button twice
                finishButton.setEnabled(false);

                // If the enemy's response is "You win" to player
                if (enemysResponse.equals("You win!")) {
                    Intent intent = new Intent(getApplicationContext(), EndActivity.class);
                    intent.putExtra("end_message", "You win!");
                    startActivity(intent);
                    finish();
                    return;
                }

                // Timer to wait before moving to bottom activity
                new CountDownTimer(1500, 1000) {
                    public void onFinish() {
                        // Intent to start enemies turn
                        Intent intent = new Intent(getApplicationContext(), BottomActivity.class);
                        // Put The Grids in the intent
                        Gson gson = new Gson();
                        // Put the players grid in intent
                        String gridJson = gson.toJson(bottom_grid);
                        intent.putExtra("bottom_grid", gridJson);
                        // Put the opponents grid in intent
                        gridJson = gson.toJson(top_grid);
                        intent.putExtra("top_grid", gridJson);
                        // Put enemies move
                        int[] move = {(int)(Math.random() * 5), (int)(Math.random() * 5)};
                        while (bottom_grid.gridHM[move[0]][move[1]] != 0)
                        {
                            move[0] = (int)(Math.random() * 5);
                            move[1] = (int)(Math.random() * 5);
                        }
                        intent.putExtra("opponent_move", move);
                        startActivity(intent);
                        finish();
                    }

                    public void onTick(long millisUntilFinished) {
                        // millisUntilFinished    The amount of time until finished.
                    }
                }.start();
            }
        });
        finishButton.setEnabled(false);
    }
}