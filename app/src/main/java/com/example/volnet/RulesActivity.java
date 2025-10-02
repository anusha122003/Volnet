package com.example.volnet;

import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class RulesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Ensure you use the correct layout name here
        setContentView(R.layout.activity_rules);

        // 1. Setup the Back Arrow listener
        ImageView backArrow = findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(v -> finish());

        // 2. You can optionally load the rules dynamically here,
        // but for now, the rules are hardcoded in the XML.

        // 3. (Optional) Set up Bottom Navigation here if you include it.
        // If the bottom nav is included in this layout, ensure it's selected.
    }
}

