package com.example.volnet;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import android.content.Intent;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
public class AddTeamActivity extends AppCompatActivity {

    private EditText editTeamName;
    private Button buttonAddTeam,buttonPickImage;
    private ImageView imageTeamLogo;
    private TeamDatabaseHelper teamDbHelper;

    private Uri selectedImageUri = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_team);

        teamDbHelper = new TeamDatabaseHelper(this);

        editTeamName = findViewById(R.id.editTeamName);
        buttonAddTeam = findViewById(R.id.buttonAddTeam);
        buttonPickImage = findViewById(R.id.buttonPickImage);
        imageTeamLogo = findViewById(R.id.imageTeamLogo);


        // Image picker launcher
        ActivityResultLauncher<String> pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        // Persist read permission for this URI so it can be accessed later
                        final int takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION;
                        getContentResolver().takePersistableUriPermission(uri, takeFlags);

                        selectedImageUri = uri;
                        imageTeamLogo.setImageURI(uri);
                    }
                }
        );

        buttonPickImage.setOnClickListener(v -> {
            pickImageLauncher.launch("image/*");
        });

        buttonAddTeam.setOnClickListener(v -> {
            String name = editTeamName.getText().toString().trim();

            if (name.isEmpty()) {
                Toast.makeText(this, "Enter team name", Toast.LENGTH_SHORT).show();
                return;
            }

            String logoUriString = (selectedImageUri != null) ? selectedImageUri.toString() : "";

            teamDbHelper.addTeam(name, logoUriString);
            Toast.makeText(this, "Team added!", Toast.LENGTH_SHORT).show();
        });
    }
}
