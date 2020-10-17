
package com.cuijeb.battleship;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;


public class BottomActivity extends AppCompatActivity {

    // The grid's textviews
    private TextView[][] grid = new TextView[5][5];

    // The Intent from Other (or Setup) Activity
    private Intent intent;
    // Grid object from intent
    private Grid bottom_grid;
    // The number array version of what is on screen
    //private int[][] numGrid = new int[5][5];
    // Pieces names like 1,2,..
    //private int[] pieceNames;
    // The respective sizes for the pieces in pieceNames
    //private int[] pieceSizes;

    // Grid object of top grid
    private Grid top_grid;

    // Enemy attack
    private int[] enemysAttack;

    // Finish turn button
    private Button finishButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom);

        // Get intent with the info from the setup screen
        intent =  getIntent();
        // Get Grid object from intent

        // Getting the grid from intent
        Gson gson = new Gson();
        String json = intent.getStringExtra("bottom_grid");
        bottom_grid = gson.fromJson(json, Grid.class);
        // Getting num grid from grid object
        // numGrid = bottom_grid.grid;
        // Getting the piece names from grid object
        // pieceNames = bottom_grid.pieceNames;
        // Getting the piece sizes from grid object
        // pieceSizes = bottom_grid.pieceSizes;

        for (int row = 0; row < 5; row++) {
            for (int column = 0; column < 5; column++) {
                // Get's ID of textviews and puts reference in grid textview array
                String id = "bottom_textView" + (row * 5 + column);
                int resID = getResources().getIdentifier(id, "id", getPackageName());
                grid[row][column] = findViewById(resID);
                // Set grid's value
                if (bottom_grid.grid[row][column] != 0) {
                    grid[row][column].setText("" + bottom_grid.grid[row][column]);
                }
                // Set grid's background color for miss or hit by opponent
                if (bottom_grid.gridHM[row][column] != 0) {
                    if (bottom_grid.gridHM[row][column] == 1) {
                        grid[row][column].setBackgroundColor(Color.GRAY);
                    } else {
                        grid[row][column].setBackgroundColor(Color.RED);
                    }
                }
            }
        }

        // Initializing the top grid
        json = intent.getStringExtra("top_grid");
        top_grid = gson.fromJson(json, Grid.class);

        // Getting enemy attack
        enemysAttack = intent.getIntArrayExtra("opponent_move");

        // Finish turn button
        finishButton = findViewById(R.id.bottom_next_button);
        // finish button onclicklistener goes to player's move board activity
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent to start game
                Intent intent = new Intent(getApplicationContext(), TopActivity.class);
                // Put Initial Grid in intent
                Gson gson = new Gson();
                String gridJson = gson.toJson(bottom_grid);
                intent.putExtra("bottom_grid", gridJson);
                // Put the opponents grid in intent
                gridJson = gson.toJson(top_grid);
                intent.putExtra("top_grid", gridJson);
                startActivity(intent);
                finish();
            }
        });
        finishButton.setEnabled(false);

        // Timer to wait before showing enemy attack
        new CountDownTimer(1500, 1000) {
            public void onFinish() {
                // When timer is finished
                // Execute your code here
                // enemy attack player
                String playersResponse =
                        top_grid.attackEnemy(bottom_grid, enemysAttack[0], enemysAttack[1]);

                // Display attack
                for (int row = 0; row < 5; row++) {
                    for (int column = 0; column < 5; column++) {
                        if (bottom_grid.gridHM[row][column] != 0) {
                            if (bottom_grid.gridHM[row][column] == 1) {
                                grid[row][column].setBackgroundColor(Color.GRAY);
                            } else {
                                grid[row][column].setBackgroundColor(Color.RED);
                            }
                        }
                    }
                }

                // If the players response is "You win" to enemy
                if (playersResponse.equals("You win!")) {
                    Intent intent = new Intent(getApplicationContext(), EndActivity.class);
                    intent.putExtra("end_message", "You lose!");
                    startActivity(intent);
                    finish();
                    return;
                }

                // Check if player lost
                // Otherwise display toast for other responses
                // Display toast
                Toast.makeText(
                        getApplicationContext(), "Player (you): " + playersResponse, Toast.LENGTH_SHORT
                ).show();
                // Allow finish turn button to work
                finishButton.setEnabled(true);
            }

            public void onTick(long millisUntilFinished) {
                // millisUntilFinished    The amount of time until finished.
            }
        }.start();
    }
}