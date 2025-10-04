package com.example.volnet;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class RulesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);

        // Example: you can load rules dynamically if needed
        TextView rulesList = findViewById(R.id.rules_list);
        rulesList.setText("1. Each team must have 6 players on the court.\n" +
                "2. Maximum of 3 touches per side.\n" +
                "3. The ball must not touch the ground.\n" +
                "4. Matches are played best of 5 sets.\n" +
                "5. Respect fair play at all times.");
    }
}
