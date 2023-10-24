package com.example.tradeguruapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

public class MentorActivity extends AppCompatActivity {

    private String[] mentorNames = {"Björn Wahlroos", "Elon Musk", "Jordan Belfort", "WallStreetBets", "Warren Buffett"};
    private ImageView mentorImageView;
    private Spinner mentorSpinner;
    private Button selectBtn;
    private Integer selectedPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentor);

        // Initialize UI elements
        mentorImageView = findViewById(R.id.mentorImageView);
        mentorSpinner = findViewById(R.id.mentorSpinner);
        selectBtn = findViewById(R.id.selectBtn);

        // Create an ArrayAdapter to populate the mentor selection Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mentorNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mentorSpinner.setAdapter(adapter);
        mentorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Handle mentor selection from the Spinner
                String selectedMentor = mentorNames[position];
                int mentorImageResource = getMentorImageResource(selectedMentor);
                mentorImageView.setImageResource(mentorImageResource);
                selectedPosition = mentorSpinner.getSelectedItemPosition();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });

        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the "Select" button click
                SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("mentor", (int) selectedPosition);
                System.out.println("Selected mentor: " + selectedPosition);
                editor.apply();
                Toast.makeText(MentorActivity.this, "You chose " + mentorNames[selectedPosition] + " to be your mentor.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private int getMentorImageResource(String mentorName) {
        // Get the resource ID for the mentor's image based on their name
        if ("Björn Wahlroos".equals(mentorName)) {
            return R.drawable.bjornwahlroos;
        } else if ("Elon Musk".equals(mentorName)) {
            return R.drawable.elonmusk;
        } else if ("Jordan Belfort".equals(mentorName)) {
            return R.drawable.jordanbelfort;
        } else if ("WallStreetBets".equals(mentorName)) {
            return R.drawable.wallstreetbets;
        } else if ("Warren Buffett".equals(mentorName)) {
            return R.drawable.warrenbuffett;
        } else {
            return R.drawable.default_mentor_image;
        }
    }
}
