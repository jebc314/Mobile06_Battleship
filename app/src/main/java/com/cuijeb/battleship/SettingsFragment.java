package com.cuijeb.battleship;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SettingsFragment extends Fragment {
    // Shared preferences
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    // Views fields
    // Background color button changers
    private Button[] bkgcolorButtons = new Button[3];
    // Text color radio button changes
    private RadioButton[] textcolorButtons = new RadioButton[3];
    // Seek bar for changing grid size.
    private SeekBar seekBar;

    public SettingsFragment() {}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.settings, container, false);
    }

    // When the activity containing this was created
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Open up the shared preferences editor
        Context hostActivity = getActivity();
        if (hostActivity == null) throw new AssertionError();
        sharedPreferences = hostActivity.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        // Getting all the views
        // Also setting which setting was previously set

        // Background color buttons
        bkgcolorButtons[0] = view.findViewById(R.id.redbackground_button);
        bkgcolorButtons[1] = view.findViewById(R.id.greenbackground_button);
        bkgcolorButtons[2] = view.findViewById(R.id.bluebackground_button);
        // Previous setting
        String backgroundColor = sharedPreferences.getString("background_color", "blue");
        switch(backgroundColor) {
            case "red":
                bkgcolorButtons[0].setEnabled(false);
                bkgcolorButtons[0].setTextColor(Color.BLACK);
                break;
            case "green":
                bkgcolorButtons[1].setEnabled(false);
                bkgcolorButtons[1].setTextColor(Color.BLACK);
                break;
            case "blue":
                bkgcolorButtons[2].setEnabled(false);
                bkgcolorButtons[2].setTextColor(Color.BLACK);
                break;
        }
        // Click listener to set/save background color
        for (int i = 0; i < bkgcolorButtons.length; i++) {
            bkgcolorButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String color = v.getResources().getResourceName(v.getId());
                    color = color.substring(color.indexOf("/") + 1, color.indexOf("background"));
                    for (int j = 0; j < bkgcolorButtons.length; j++) {
                        bkgcolorButtons[j].setEnabled(true);
                        bkgcolorButtons[j].setTextColor(Color.WHITE);
                    }
                    v.setEnabled(false);
                    Button b = (Button)v;
                    b.setTextColor(Color.BLACK);
                    editor.putString("background_color", color);
                    editor.apply();
                    // Log.d("JEB_DEBUG", "Background color changed");
                }
            });
        }

        // Text Color Radio Buttons
        textcolorButtons[0] = view.findViewById(R.id.blacktext_radiobutton);
        textcolorButtons[1] = view.findViewById(R.id.whitetext_radiobutton);
        textcolorButtons[2] = view.findViewById(R.id.bluetext_radiobutton);
        // Previous setting
        String textColor = sharedPreferences.getString("text_color", "black");
        switch(textColor) {
            case "black":
                textcolorButtons[0].toggle();
                break;
            case "white":
                textcolorButtons[1].toggle();
                break;
            case "blue":
                textcolorButtons[2].toggle();
                break;
        }
        // Click listener to set/save text color
        for (int i = 0; i < textcolorButtons.length; i++) {
            textcolorButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String color = v.getResources().getResourceName(v.getId());
                    color = color.substring(color.indexOf("/") + 1, color.indexOf("text"));
                    editor.putString("text_color", color);
                    editor.apply();
                    // Log.d("JEB_DEBUG", "Background color changed");
                }
            });
        }

        // Grid Size Seek Bar
        seekBar = view.findViewById(R.id.gridsize_seekBar);
        int size = sharedPreferences.getInt("grid_size", 5);
        seekBar.setProgress(size - 5);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                editor.putInt("grid_size", progress + 5);
                editor.apply();
                Toast.makeText(getContext(), "Size set to " + (progress + 5), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}