package com.example.volnet;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class create_match_activity extends AppCompatActivity {

    private Button buttonTeam1, buttonTeam2;
    private DatabaseHelper dbHelper;
    private Team selectedTeam1, selectedTeam2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_match);

        dbHelper = new DatabaseHelper(this);

        buttonTeam1 = findViewById(R.id.buttonTeam1);
        buttonTeam2 = findViewById(R.id.buttonTeam2);
        EditText dateEditText = findViewById(R.id.dateEditText);
        EditText timeEditText = findViewById(R.id.timeEditText);

        // Team Selection Buttons
        buttonTeam1.setOnClickListener(v -> showTeamSelector(1));
        buttonTeam2.setOnClickListener(v -> showTeamSelector(2));

        // Date/Time Picker Listeners
        dateEditText.setOnClickListener(v -> showDatePicker(dateEditText));
        timeEditText.setOnClickListener(v -> showTimePicker(timeEditText));
    }

    private void showTeamSelector(int teamNumber) {
        List<Team> teams = dbHelper.getAllTeams();

        SelectTeamBottomSheet bottomSheet = new SelectTeamBottomSheet(
                teams,
                team -> {
                    if (teamNumber == 1) {
                        selectedTeam1 = team;
                        buttonTeam1.setText(team.getName());
                    } else {
                        selectedTeam2 = team;
                        buttonTeam2.setText(team.getName());
                    }

                    Button createMatchBtn = findViewById(R.id.createMatchButton);
                    if (selectedTeam1 != null && selectedTeam2 != null) {
                        createMatchBtn.setEnabled(true);
                        createMatchBtn.setBackgroundTintList(
                                ContextCompat.getColorStateList(this, R.color.red)
                        );
                        createMatchBtn.setTextColor(ContextCompat.getColor(this, R.color.white));
                    }
                }
        );

        bottomSheet.show(getSupportFragmentManager(), "SelectTeamBottomSheet");
    }

    private void showDatePicker(EditText targetEditText) {
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    String selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%d", dayOfMonth, month + 1, year);
                    targetEditText.setText(selectedDate);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.show();
    }

    private void showTimePicker(EditText targetEditText) {
        Calendar calendar = Calendar.getInstance();

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, hourOfDay, minute) -> {
                    String selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                    targetEditText.setText(selectedTime);
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
        );

        timePickerDialog.show();
    }
}
