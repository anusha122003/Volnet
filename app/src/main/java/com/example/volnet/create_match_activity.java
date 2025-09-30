package com.example.volnet;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;


public class create_match_activity extends AppCompatActivity {

    private Button buttonTeam1, buttonTeam2, btnCreate;
    private TeamDatabaseHelper teamHelper;
    private MatchDatabaseHelper matchDbHelper;
    private Team selectedTeam1, selectedTeam2;
    private EditText dateEditText, timeEditText;

    private ImageView team1Logo, team2Logo;
    private LinearLayout team1Layout, team2Layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_match);

        teamHelper = new TeamDatabaseHelper(this);
        matchDbHelper = new MatchDatabaseHelper(this);

        buttonTeam1 = findViewById(R.id.buttonTeam1);
        buttonTeam2 = findViewById(R.id.buttonTeam2);
        btnCreate = findViewById(R.id.createMatchButton);
        dateEditText = findViewById(R.id.dateEditText);
        timeEditText = findViewById(R.id.timeEditText);
        team1Logo = findViewById(R.id.team1Logo);
        team2Logo = findViewById(R.id.team2Logo);
        team1Layout = findViewById(R.id.team1Layout);
        team2Layout = findViewById(R.id.team2Layout);


        // Team Selection
        buttonTeam1.setOnClickListener(v -> showTeamSelector(1));
        buttonTeam2.setOnClickListener(v -> showTeamSelector(2));

        // Date/Time Picker
        dateEditText.setOnClickListener(v -> showDatePicker(dateEditText));
        timeEditText.setOnClickListener(v -> showTimePicker(timeEditText));


        // Create Match
        btnCreate.setOnClickListener(v -> {
            if (selectedTeam1 != null && selectedTeam2 != null) {

                if (selectedTeam1.getId() == selectedTeam2.getId()) {
                    Toast.makeText(this, "Please select two different teams", Toast.LENGTH_SHORT).show();
                    return;
                }
                String date = dateEditText.getText().toString();
                String time = timeEditText.getText().toString();

                if (date.isEmpty()) {
                    Toast.makeText(this, "Please select a date", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (time.isEmpty()) {
                    Toast.makeText(this, "Please select a time", Toast.LENGTH_SHORT).show();
                    return;
                }

                long matchId = matchDbHelper.createMatch(
                        selectedTeam1.getName(),
                        selectedTeam1.getLogo(),  // Or appropriate getter for the logo
                        selectedTeam2.getName(),
                        selectedTeam2.getLogo(),
                        date,
                        time);

                if (matchId == -1) {
                    Toast.makeText(this, "Failed to create match", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(create_match_activity.this, TeamScoringActivity.class);
                intent.putExtra("MATCH_ID", matchId);
                startActivity(intent);
            }
            else {
                Toast.makeText(this, "Please select both teams", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showTeamSelector(int teamNumber) {
        List<Team> teams = teamHelper.getAllTeams();
        SelectTeamBottomSheet bottomSheet = new SelectTeamBottomSheet(
                teams,
                team -> {
                    // Check if same team selected for both buttons
                    if (teamNumber == 1 && selectedTeam2 != null && team.getId() == selectedTeam2.getId()) {
                        Toast.makeText(this, "This team is already selected as Team 2", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (teamNumber == 2 && selectedTeam1 != null && team.getId() == selectedTeam1.getId()) {
                        Toast.makeText(this, "This team is already selected as Team 1", Toast.LENGTH_SHORT).show();
                        return;
                    }



                    if (teamNumber == 1) {
                        selectedTeam1 = team;
                        buttonTeam1.setText(team.getName());
                        Glide.with(this)
                                .load(team.getLogo()) // logo is expected to be a path or URL
                                .into(team1Logo); // Ensure getLogo() returns drawable resource ID
                        team1Logo.setVisibility(View.VISIBLE);  // Make sure logo is visible!
                        buttonTeam1.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.red));  // Custom color
                    } else {
                        selectedTeam2 = team;
                        buttonTeam2.setText(team.getName());
                        Glide.with(this)
                                .load(team.getLogo())
                                .into(team2Logo);
                        team2Logo.setVisibility(View.VISIBLE);  // Make sure logo is visible!
                        buttonTeam2.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.red));
                    }

                    if (selectedTeam1 != null && selectedTeam2 != null) {
                        btnCreate.setEnabled(true);
                        btnCreate.setBackgroundTintList(
                                ContextCompat.getColorStateList(this, R.color.red)
                        );
                    }
                }
        );
        bottomSheet.show(getSupportFragmentManager(), "SelectTeamBottomSheet");
    }

    private void showDatePicker(EditText target) {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    String selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%d",
                            dayOfMonth, month + 1, year);
                    target.setText(selectedDate);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    private void showTimePicker(EditText target) {
        Calendar calendar = Calendar.getInstance();
        new TimePickerDialog(this,
                (view, hourOfDay, minute) -> {
                    String selectedTime = String.format(Locale.getDefault(), "%02d:%02d",
                            hourOfDay, minute);
                    target.setText(selectedTime);
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
        ).show();
    }
}
