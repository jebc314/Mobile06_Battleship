package com.cuijeb.battleship;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class EndActivity extends AppCompatActivity {

    // For displaying the message
    private String message;

    // Textview for displaying message
    private TextView messageTextView;

    // Restart game button
    private Button restartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        // Getting intent from the grid activities
        Intent intent = getIntent();
        // Getting the end message (lost or win) from the intent
        message = intent.getStringExtra("end_message");

        // Get and set the message to user
        messageTextView = findViewById(R.id.endmessage_textview);
        messageTextView.setText(message);

        // Get button and send back to main activity
        restartButton = findViewById(R.id.restart_button);
        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
}