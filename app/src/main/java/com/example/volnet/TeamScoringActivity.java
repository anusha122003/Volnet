package com.example.volnet;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class TeamScoringActivity extends AppCompatActivity {

    private TextView team1Name, team2Name, score1, score2;
    private Button plus1, minus1, plus2, minus2, endMatchBtn, timeout1Btn, timeout2Btn;
    private TextView[] setViews;

    private int points1 = 0, points2 = 0;
    private int currentSet = 1;
    private final int[] setWins = {0, 0}; // sets won by team1 and team2
    private int timeoutsTeam1 = 2, timeoutsTeam2 = 2;

    private long matchId;
    private MatchDatabaseHelper matchDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_scoring);

        team1Name = findViewById(R.id.tvTeamA);
        team2Name = findViewById(R.id.tvTeamB);
        score1 = findViewById(R.id.tvScoreA);
        score2 = findViewById(R.id.tvScoreB);

        plus1 = findViewById(R.id.btnPlusA);
        minus1 = findViewById(R.id.btnMinusA);
        plus2 = findViewById(R.id.btnPlusB);
        minus2 = findViewById(R.id.btnMinusB);
        timeout1Btn = findViewById(R.id.btnTimeoutTeamA);
        timeout2Btn = findViewById(R.id.btnTimeoutTeamB);

        endMatchBtn = findViewById(R.id.btnNextSet);

        // Set views for coloring
        setViews = new TextView[]{
                findViewById(R.id.set1),
                findViewById(R.id.set2),
                findViewById(R.id.set3),
                findViewById(R.id.set4),
                findViewById(R.id.set5)
        };

        matchDbHelper = new MatchDatabaseHelper(this);
        matchId = getIntent().getLongExtra("MATCH_ID", -1);

        loadMatch();

        plus1.setOnClickListener(v -> updateScore(1, true));
        minus1.setOnClickListener(v -> updateScore(1, false));
        plus2.setOnClickListener(v -> updateScore(2, true));
        minus2.setOnClickListener(v -> updateScore(2, false));

        timeout1Btn.setOnClickListener(v -> takeTimeout(1));
        timeout2Btn.setOnClickListener(v -> takeTimeout(2));

        endMatchBtn.setOnClickListener(v -> endMatch());
    }

    private void loadMatch() {
        Cursor cursor = matchDbHelper.getMatchById(matchId);
        if (cursor.moveToFirst()) {
            team1Name.setText(cursor.getString(cursor.getColumnIndexOrThrow("team_a_name")));
            team2Name.setText(cursor.getString(cursor.getColumnIndexOrThrow("team_b_name")));
        }
        cursor.close();
    }

    private void updateScore(int team, boolean increase) {
        if (team == 1) {
            if (increase) points1++;
            else if (points1 > 0) confirmDecrement(1);
            score1.setText(String.valueOf(points1));
        } else {
            if (increase) points2++;
            else if (points2 > 0) confirmDecrement(2);
            score2.setText(String.valueOf(points2));
        }

        checkSetPoint();
        checkSetWinner();
    }

    private void confirmDecrement(int team) {
        new AlertDialog.Builder(this)
                .setTitle("Confirm")
                .setMessage("Do you want to decrease the point?")
                .setPositiveButton("Yes", (d, w) -> {
                    if (team == 1 && points1 > 0) points1--;
                    if (team == 2 && points2 > 0) points2--;
                    score1.setText(String.valueOf(points1));
                    score2.setText(String.valueOf(points2));
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void checkSetPoint() {
        if ((points1 >= 24 && points1 > points2) || (points2 >= 24 && points2 > points1)) {
            String team = points1 > points2 ? team1Name.getText().toString() : team2Name.getText().toString();
            new AlertDialog.Builder(this)
                    .setTitle("Set Point")
                    .setMessage("Set Point for " + team)
                    .setPositiveButton("OK", null)
                    .show();
        }
    }

    private void checkSetWinner() {
        if ((points1 >= 25 && points1 - points2 >= 2) ||
                (points2 >= 25 && points2 - points1 >= 2)) {

            String winner = points1 > points2 ? team1Name.getText().toString() : team2Name.getText().toString();

            if (points1 > points2) setWins[0]++; else setWins[1]++;

            setViews[currentSet - 1].setBackgroundColor(ContextCompat.getColor(this, R.color.dark_gray));

            if (currentSet == 5 || setWins[0] == 3 || setWins[1] == 3) {
                // Match ends
                endMatch();
            } else {
                // Next set
                currentSet++;
                points1 = 0;
                points2 = 0;
                score1.setText("0");
                score2.setText("0");
                setViews[currentSet - 1].setBackgroundColor(ContextCompat.getColor(this, R.color.light_dark));
                timeoutsTeam1 = 2;
                timeoutsTeam2 = 2;

                if (currentSet == 5) {
                    endMatchBtn.setText(R.string.end_match);
                }
            }
        }
    }

    private void takeTimeout(int team) {
        if (team == 1 && timeoutsTeam1 == 0) {
            showMessage("Team " + team1Name.getText() + " has no timeouts left!");
            return;
        }
        if (team == 2 && timeoutsTeam2 == 0) {
            showMessage("Team " + team2Name.getText() + " has no timeouts left!");
            return;
        }

        if (team == 1) timeoutsTeam1--; else timeoutsTeam2--;

        showTimeoutDialog(team);
    }

    private void showTimeoutDialog(int team) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Timeout - " + (team == 1 ? team1Name.getText() : team2Name.getText()));

        final TextView timerView = new TextView(this);
        timerView.setTextSize(24);
        timerView.setPadding(30, 30, 30, 30);
        builder.setView(timerView);

        builder.setNegativeButton("Cancel", (d, w) -> d.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();

        CountDownTimer timer = new CountDownTimer(5 * 60 * 1000, 1000) {
            boolean paused = false;
            long timeRemaining = 5 * 60 * 1000;

            @Override
            public void onTick(long millisUntilFinished) {
                if (!paused) {
                    timeRemaining = millisUntilFinished;
                    int minutes = (int) (timeRemaining / 1000) / 60;
                    int seconds = (int) (timeRemaining / 1000) % 60;
                    timerView.setText(String.format("%02d:%02d", minutes, seconds));
                }
            }

            @Override
            public void onFinish() {
                timerView.setText("00:00");
                dialog.dismiss();
            }
        }.start();
    }

    private void showMessage(String msg) {
        new AlertDialog.Builder(this)
                .setMessage(msg)
                .setPositiveButton("OK", null)
                .show();
    }

    private void endMatch() {
        String winner;
        if (setWins[0] > setWins[1]) {
            winner = team1Name.getText().toString();
        } else {
            winner = team2Name.getText().toString();
        }

        matchDbHelper.endMatch(matchId, winner);

        new AlertDialog.Builder(this)
                .setTitle("Match Ended")
                .setMessage("🎉 Congratulations " + winner + " 🎉")
                .setPositiveButton("OK", (d, w) -> {
                    Intent intent = new Intent(TeamScoringActivity.this, HistoryActivity.class);
                    startActivity(intent);
                    finish();
                })
                .show();
    }
}
