package com.example.gapfinder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button startQuizButton;
    Button mathButton;
    Button physicsButton;
    Button chemistryButton;

    TextView memoryText;
    TextView progressText;

    String weakestTopic = "";
    String lastSubject = "";
    String selectedSubject = "Mathematics";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startQuizButton = findViewById(R.id.startQuizButton);

        mathButton = findViewById(R.id.mathButton);
        physicsButton = findViewById(R.id.physicsButton);
        chemistryButton = findViewById(R.id.chemistryButton);

        memoryText = findViewById(R.id.memoryText);
        progressText = findViewById(R.id.progressText);

        // Subject selection
        mathButton.setOnClickListener(v -> {
            selectedSubject = "Mathematics";
            weakestTopic = "";
            mathButton.setText("✓ Mathematics");
            physicsButton.setText("Physics");
            chemistryButton.setText("Chemistry");
        });

        physicsButton.setOnClickListener(v -> {
            selectedSubject = "Physics";
            weakestTopic = "";
            mathButton.setText("Mathematics");
            physicsButton.setText("✓ Physics");
            chemistryButton.setText("Chemistry");
        });

        chemistryButton.setOnClickListener(v -> {
            selectedSubject = "Chemistry";
            weakestTopic = "";
            mathButton.setText("Mathematics");
            physicsButton.setText("Physics");
            chemistryButton.setText("✓ Chemistry");
        });

        startQuizButton.setOnClickListener(v -> {

            Intent intent =
                    new Intent(MainActivity.this, QuizActivity.class);

            intent.putExtra("weakestTopic", weakestTopic);
            intent.putExtra("subject", selectedSubject);

            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadLearningMemory();
    }

    private void loadLearningMemory() {

        SharedPreferences preferences =
                getSharedPreferences(
                        "GapFinderMemory",
                        MODE_PRIVATE
                );

        lastSubject =
                preferences.getString(
                        "lastSubject",
                        ""
                );

        int lastScore =
                preferences.getInt("lastScore", -1);

        int quizCount =
                preferences.getInt("quizCount", 0);

        int bestScore =
                preferences.getInt("bestScore", 0);

        weakestTopic =
                preferences.getString(
                        "weakestTopic",
                        ""
                );

        if (lastScore == -1) {

            progressText.setText(
                    "Quizzes Completed: 0\n" +
                            "Best Score: --"
            );

            memoryText.setText(
                    "Welcome!\n\n" +
                            "Take your first quiz to discover your learning gaps."
            );

        } else {

            progressText.setText(
                    "Quizzes Completed: " + quizCount + "\n" +
                            "Best Score: " + bestScore + "/8\n" +
                            "Current Score: " + lastScore + "/8\n" +
                            "Weak Topic: " + weakestTopic
            );

            memoryText.setText(
                    "Welcome back!\n\n" +
                            "Last Subject: " + lastSubject + "\n" +
                            "Last Score: " + lastScore + "/8\n" +
                            "Needs Practice: " + weakestTopic + "\n\n" +
                            "Recommended: Practice " + weakestTopic
            );
        }
    }
}