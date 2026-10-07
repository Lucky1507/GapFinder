package com.example.gapfinder;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    TextView scoreText;
    TextView algebraText;
    TextView calculusText;
    TextView trigonometryText;
    TextView geometryText;
    TextView recommendationText;

    Button doneButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        scoreText = findViewById(R.id.scoreText);

        algebraText = findViewById(R.id.algebraText);
        calculusText = findViewById(R.id.calculusText);
        trigonometryText = findViewById(R.id.trigonometryText);
        geometryText = findViewById(R.id.geometryText);

        recommendationText =
                findViewById(R.id.recommendationText);

        doneButton = findViewById(R.id.doneButton);

        int score =
                getIntent().getIntExtra("score", 0);

        String subject =
                getIntent().getStringExtra("subject");

        if (subject == null) {
            subject = "Mathematics";
        }

        scoreText.setText(
                "Score: " + score + "/8"
        );

        if (subject.equals("Physics")) {

            showPhysicsResult();

        } else if (subject.equals("Chemistry")) {

            showChemistryResult();

        } else {

            showMathResult();
        }

        doneButton.setOnClickListener(v -> finish());
    }

    private void showMathResult() {

        int algebraCorrect =
                getIntent().getIntExtra(
                        "algebraCorrect", 0
                );

        int algebraTotal =
                getIntent().getIntExtra(
                        "algebraTotal", 0
                );

        int calculusCorrect =
                getIntent().getIntExtra(
                        "calculusCorrect", 0
                );

        int calculusTotal =
                getIntent().getIntExtra(
                        "calculusTotal", 0
                );

        int trigonometryCorrect =
                getIntent().getIntExtra(
                        "trigonometryCorrect", 0
                );

        int trigonometryTotal =
                getIntent().getIntExtra(
                        "trigonometryTotal", 0
                );

        int geometryCorrect =
                getIntent().getIntExtra(
                        "geometryCorrect", 0
                );

        int geometryTotal =
                getIntent().getIntExtra(
                        "geometryTotal", 0
                );

        showTopicResult(
                algebraText,
                "Algebra",
                algebraCorrect,
                algebraTotal
        );

        showTopicResult(
                calculusText,
                "Calculus",
                calculusCorrect,
                calculusTotal
        );

        showTopicResult(
                trigonometryText,
                "Trigonometry",
                trigonometryCorrect,
                trigonometryTotal
        );

        showTopicResult(
                geometryText,
                "Geometry",
                geometryCorrect,
                geometryTotal
        );

        recommendationText.setText(
                getMathRecommendation(
                        algebraCorrect,
                        algebraTotal,
                        calculusCorrect,
                        calculusTotal,
                        trigonometryCorrect,
                        trigonometryTotal,
                        geometryCorrect,
                        geometryTotal
                )
        );
    }

    private void showPhysicsResult() {

        algebraText.setText(
                "Motion\n\nFocus area"
        );

        calculusText.setText(
                "Force\n\nFocus area"
        );

        trigonometryText.setText(
                "Energy\n\nFocus area"
        );

        geometryText.setText(
                "Electricity\n\nFocus area"
        );

        recommendationText.setText(
                "Recommended Practice\n\n" +
                        "Review the Physics topics " +
                        "covered in this quiz."
        );
    }

    private void showChemistryResult() {

        algebraText.setText(
                "Atoms\n\nFocus area"
        );

        calculusText.setText(
                "Chemical Reactions\n\nFocus area"
        );

        trigonometryText.setText(
                "Acids and Bases\n\nFocus area"
        );

        geometryText.setText(
                "Periodic Table\n\nFocus area"
        );

        recommendationText.setText(
                "Recommended Practice\n\n" +
                        "Review the Chemistry topics " +
                        "covered in this quiz."
        );
    }

    private void showTopicResult(
            TextView textView,
            String topic,
            int correct,
            int total
    ) {

        if (total == 0) {

            textView.setText(
                    topic + "\nNot tested"
            );

            return;
        }

        int percentage =
                (correct * 100) / total;

        if (percentage < 60) {

            textView.setText(
                    topic + "\n" +
                            "Accuracy: " + percentage + "%\n" +
                            "Needs Practice"
            );

        } else {

            textView.setText(
                    topic + "\n" +
                            "Accuracy: " + percentage + "%\n" +
                            "Strong"
            );
        }
    }

    private String getMathRecommendation(
            int algebraCorrect,
            int algebraTotal,
            int calculusCorrect,
            int calculusTotal,
            int trigonometryCorrect,
            int trigonometryTotal,
            int geometryCorrect,
            int geometryTotal
    ) {

        int lowestPercentage = 101;
        String weakestTopic = "";

        if (algebraTotal > 0) {

            int percentage =
                    (algebraCorrect * 100) /
                            algebraTotal;

            if (percentage < lowestPercentage) {
                lowestPercentage = percentage;
                weakestTopic = "Algebra";
            }
        }

        if (calculusTotal > 0) {

            int percentage =
                    (calculusCorrect * 100) /
                            calculusTotal;

            if (percentage < lowestPercentage) {
                lowestPercentage = percentage;
                weakestTopic = "Calculus";
            }
        }

        if (trigonometryTotal > 0) {

            int percentage =
                    (trigonometryCorrect * 100) /
                            trigonometryTotal;

            if (percentage < lowestPercentage) {
                lowestPercentage = percentage;
                weakestTopic = "Trigonometry";
            }
        }

        if (geometryTotal > 0) {

            int percentage =
                    (geometryCorrect * 100) /
                            geometryTotal;

            if (percentage < lowestPercentage) {
                lowestPercentage = percentage;
                weakestTopic = "Geometry";
            }
        }

        if (weakestTopic.isEmpty()) {
            return "Great job!\nKeep practicing.";
        }

        return "Recommended Practice\n\n" +
                "Focus on " + weakestTopic + ".";
    }
}