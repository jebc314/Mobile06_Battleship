
package com.cuijeb.battleship;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.gson.Gson;


public class BottomActivity extends AppCompatActivity {

    // The grid's textviews
    private TextView[][] grid;

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

    // Shared preferences
    private SharedPreferences sharedPreferences;

    // Background
    private ConstraintLayout constraintLayout;

    // Finish turn button
    private Button finishButton;

    // Table board
    private TableLayout tableLayout;
    // Table rows
    private TableRow[] tableRows;
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


        // Initializing the top grid
        json = intent.getStringExtra("top_grid");
        top_grid = gson.fromJson(json, Grid.class);

        // Getting enemy attack
        enemysAttack = intent.getIntArrayExtra("opponent_move");

        // Make the grid display
        grid = new TextView[bottom_grid.grid.length][bottom_grid.grid.length];
        // Get table layout
        tableLayout = findViewById(R.id.bottom_board_layout);
        // Reset table layout
        tableLayout.removeAllViews();

        // Getting the table rows
        tableRows = new TableRow[grid.length];
        TableLayout.LayoutParams taLayout = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
        for (int i = 0; i < grid.length; i++)
        {
            TableRow tr = new TableRow(this);
            tr.setLayoutParams(taLayout);
            tableLayout.addView(tr);
            tableRows[i] = tr;
        }
        // Calculate "table width" in pixels
        float tableWidth = 375 * this.getResources().getDisplayMetrics().density;
        //Log.d("JEB", ""+ tableWidth);
        // Margins in pixels
        float textviewMargin = this.getResources().getDisplayMetrics().density;

        // Table Row Layout params
        int textviewWidth = (int)(tableWidth / grid.length - 2 * textviewMargin);
        TableRow.LayoutParams tr = new TableRow.LayoutParams(textviewWidth, textviewWidth);
        tr.setMargins((int)textviewMargin,(int)textviewMargin,(int)textviewMargin,(int)textviewMargin);

        // getting grid pieces
        for (int row = 0; row < grid.length; row++) {
            for (int column = 0; column < grid.length; column++) {
                // Make textview
                TextView tv = new TextView(this);
                tv.setLayoutParams(tr);
                tv.setBackgroundColor(Color.WHITE);
                int textSize = 250 / grid.length;
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);

                // Add textview to table row and grid
                tableRows[row].addView(tv);
                grid[row][column] = tv;

                // Set grid's value
                if (bottom_grid.grid[row][column] != 0) {
                    grid[row][column].setText("" + bottom_grid.grid[row][column]);
                }

                if (bottom_grid.gridHM[row][column] != 0) {
                    if (bottom_grid.gridHM[row][column] == 1) {
                        grid[row][column].setBackgroundColor(Color.GRAY);
                    } else {
                        grid[row][column].setBackgroundColor(Color.RED);
                    }
                }
            }
        }

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

        // get sharedpreferences
        sharedPreferences = getSharedPreferences("Settings", Context.MODE_PRIVATE);

        // Constraint layout
        constraintLayout = findViewById(R.id.bottom_constraintlayout);

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
        TextView labelTextView = findViewById(R.id.bottom_board_title);
        switch (textColor) {
            case "black":
                labelTextView.setTextColor(Color.BLACK);
                finishButton.setTextColor(Color.BLACK);
                for (TextView[] r: grid)
                    for (TextView c: r)
                        c.setTextColor(Color.BLACK);
                break;
            case "white":
                labelTextView.setTextColor(Color.WHITE);
                finishButton.setTextColor(Color.WHITE);
                break;
            case "blue":
                labelTextView.setTextColor(Color.BLUE);
                finishButton.setTextColor(Color.BLUE);
                for (TextView[] r: grid)
                    for (TextView c: r)
                        c.setTextColor(Color.BLUE);
                break;
        }

        // Timer to wait before showing enemy attack
        new CountDownTimer(1500, 1000) {
            public void onFinish() {
                // When timer is finished
                // Execute your code here
                // enemy attack player
                String playersResponse =
                        top_grid.attackEnemy(bottom_grid, enemysAttack[0], enemysAttack[1]);

                // Display attack
                if (bottom_grid.gridHM[enemysAttack[0]][enemysAttack[1]] != 0) {
                    if (bottom_grid.gridHM[enemysAttack[0]][enemysAttack[1]] == 1) {
                        grid[enemysAttack[0]][enemysAttack[1]].setBackgroundColor(Color.GRAY);
                    } else {
                        grid[enemysAttack[0]][enemysAttack[1]].setBackgroundColor(Color.RED);
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