package com.example.volnet;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
public class AddTeamActivity extends AppCompatActivity {

    private EditText editTeamName;
    private Button buttonAddTeam;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_team);

        dbHelper = new DatabaseHelper(this);
        editTeamName = findViewById(R.id.editTeamName);
        buttonAddTeam = findViewById(R.id.buttonAddTeam);

        buttonAddTeam.setOnClickListener(v -> {
            String name = editTeamName.getText().toString().trim();
            if (!name.isEmpty()) {
                dbHelper.addTeam(name);
                Toast.makeText(this, "Team added!", Toast.LENGTH_SHORT).show();
                finish(); // go back to CreateMatchActivity
            } else {
                Toast.makeText(this, "Enter team name", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
