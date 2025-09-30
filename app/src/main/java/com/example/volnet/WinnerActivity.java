package com.example.volnet;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
public class WinnerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winner);

        TextView tvWinner = findViewById(R.id.tvWinner);
        ImageView ivWinnerLogo = findViewById(R.id.ivWinnerLogo);

        String winner = getIntent().getStringExtra("winner");
        String logo = getIntent().getStringExtra("logo");

        tvWinner.setText("🎉 Congratulations " + winner + "! 🎉");

        if (logo != null && !logo.isEmpty()) {
            ivWinnerLogo.setImageURI(Uri.parse(logo));
        }
    }
}
