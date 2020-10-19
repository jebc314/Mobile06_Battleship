
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

import java.util.HashMap;


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

    // Constraint layout
    private ConstraintLayout constraintLayout;

    // Shared preferences
    private SharedPreferences sharedPreferences;

    // Table board
    private TableLayout tableLayout;
    // Table rows
    private TableRow[] tableRows;

    // Map with textviews and positions
    private HashMap<View, Integer> textViewPositions;

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

        // Initializing the bottom grid
        json = intent.getStringExtra("bottom_grid");
        bottom_grid = gson.fromJson(json, Grid.class);

        // Make the grid display
        grid = new TextView[bottom_grid.grid.length][bottom_grid.grid.length];
        // Get table layout
        tableLayout = findViewById(R.id.top_board_layout);
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

        // Make the map
        textViewPositions  = new HashMap<>();

        for (int row = 0; row < grid.length; row++) {
            for (int column = 0; column < grid.length; column++) {
                /*
                // Get's ID of textviews and puts reference in grid textview array
                String id = "top_textView" + (row * grid.length + column);
                int resID = getResources().getIdentifier(id, "id", getPackageName());
                grid[row][column] = findViewById(resID);
                 */
                // Make textview
                TextView tv = new TextView(this);
                tv.setLayoutParams(tr);
                tv.setBackgroundColor(Color.WHITE);
                int textSize = 250 / grid.length;
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);

                // Add textview to table row and grid
                tableRows[row].addView(tv);
                grid[row][column] = tv;
                textViewPositions.put(tv, row * grid.length + column);

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
                        int pos = textViewPositions.get(v);
                        int row = pos / grid.length;
                        int col = pos % grid.length;

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

        // Finish turn button
        finishButton = findViewById(R.id.top_next_button);

        // get sharedpreferences
        sharedPreferences = getSharedPreferences("Settings", Context.MODE_PRIVATE);

        // Constraint layout
        constraintLayout = findViewById(R.id.top_constraintlayout);

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
        TextView labelTextView = findViewById(R.id.top_board_title);
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
                        for (int i = 0; i < grid.length; i++) {
                            for (int j = 0; j < grid.length; j++) {
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
                        int[] move = {(int)(Math.random() * grid.length), (int)(Math.random() * grid.length)};
                        while (bottom_grid.gridHM[move[0]][move[1]] != 0)
                        {
                            move[0] = (int)(Math.random() * grid.length);
                            move[1] = (int)(Math.random() * grid.length);
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