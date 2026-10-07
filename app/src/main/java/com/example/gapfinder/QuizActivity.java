package com.example.gapfinder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class QuizActivity extends AppCompatActivity {

    TextView questionNumber;
    TextView questionText;
    ProgressBar quizProgress;

    TextView topicText;

    RadioGroup optionsGroup;

    RadioButton option1;
    RadioButton option2;
    RadioButton option3;
    RadioButton option4;

    Button nextButton;

    int currentQuestion = 0;
    int score = 0;

    String weakestTopic = "";
    String selectedSubject = "Mathematics";

    String[] topics;

    String[] questions;

    String[][] options;

    int[] correctAnswers;

    int[] questionOrder = {
            0, 1, 2, 3, 4, 5, 6, 7
    };

    int[] topicCorrect = new int[4];
    int[] topicTotal = new int[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        weakestTopic = getIntent().getStringExtra("weakestTopic");
        selectedSubject =
                getIntent().getStringExtra("subject");

        if (selectedSubject == null) {
            selectedSubject = "Mathematics";
        }
        loadSubjectQuestions();

        if (weakestTopic == null) {
            weakestTopic = "";
        }

        questionNumber = findViewById(R.id.questionNumber);
        questionText = findViewById(R.id.questionText);
        quizProgress = findViewById(R.id.quizProgress);
        optionsGroup = findViewById(R.id.optionsGroup);

        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);

        topicText = findViewById(R.id.topicText);
        nextButton = findViewById(R.id.nextButton);

        createAdaptiveQuestionOrder();

        if (!weakestTopic.isEmpty()) {
            Toast.makeText(
                    this,
                    "Adaptive Quiz: Focus on " + weakestTopic,
                    Toast.LENGTH_LONG
            ).show();
        }

        showQuestion();

        nextButton.setOnClickListener(v -> checkAnswer());
    }

    private void loadSubjectQuestions() {

        if (selectedSubject.equals("Physics")) {

            topics = new String[]{
                    "Motion",
                    "Motion",
                    "Force",
                    "Force",
                    "Energy",
                    "Energy",
                    "Electricity",
                    "Electricity"
            };

            questions = new String[]{
                    "What is the SI unit of velocity?",
                    "A car travels 60 km in 2 hours. What is its average speed?",
                    "What is the SI unit of force?",
                    "What force pulls objects toward Earth?",
                    "What is the SI unit of energy?",
                    "Which form of energy is stored in a stretched spring?",
                    "What is the SI unit of electric current?",
                    "Which device is used to measure electric current?"
            };

            options = new String[][]{
                    {"Meter", "Meter per second", "Kilogram", "Newton"},
                    {"20 km/h", "30 km/h", "60 km/h", "120 km/h"},
                    {"Joule", "Newton", "Watt", "Pascal"},
                    {"Friction", "Gravity", "Magnetism", "Tension"},
                    {"Newton", "Watt", "Joule", "Volt"},
                    {"Kinetic", "Chemical", "Elastic potential", "Thermal"},
                    {"Volt", "Ohm", "Ampere", "Watt"},
                    {"Voltmeter", "Ammeter", "Thermometer", "Barometer"}
            };

            correctAnswers = new int[]{
                    1, 1, 1, 1, 2, 2, 2, 1
            };

        } else if (selectedSubject.equals("Chemistry")) {

            topics = new String[]{
                    "Atoms",
                    "Atoms",
                    "Chemical Reactions",
                    "Chemical Reactions",
                    "Acids and Bases",
                    "Acids and Bases",
                    "Periodic Table",
                    "Periodic Table"
            };

            questions = new String[]{
                    "What is the smallest unit of an element?",
                    "Which particle has a negative charge?",
                    "What is produced when two or more substances chemically combine?",
                    "What type of reaction releases heat?",
                    "What is the pH of a neutral solution?",
                    "Which substance is acidic?",
                    "What is the chemical symbol for oxygen?",
                    "How many periods are in the modern periodic table?"
            };

            options = new String[][]{
                    {"Molecule", "Atom", "Cell", "Compound"},
                    {"Proton", "Neutron", "Electron", "Nucleus"},
                    {"Mixture", "Compound", "Solution", "Element"},
                    {"Endothermic", "Exothermic", "Neutral", "Physical"},
                    {"0", "5", "7", "14"},
                    {"Lemon juice", "Pure water", "Soap solution", "Baking soda solution"},
                    {"Ox", "O", "Og", "C"},
                    {"5", "6", "7", "8"}
            };

            correctAnswers = new int[]{
                    1, 2, 1, 1, 2, 0, 1, 2
            };

        } else {

            // Mathematics

            topics = new String[]{
                    "Algebra",
                    "Algebra",
                    "Calculus",
                    "Calculus",
                    "Trigonometry",
                    "Trigonometry",
                    "Geometry",
                    "Geometry"
            };

            questions = new String[]{
                    "What is the value of x if 2x + 4 = 10?",
                    "What is the value of x if x + 7 = 12?",
                    "What is the derivative of x²?",
                    "What is the derivative of 3x²?",
                    "What is the value of sin 90°?",
                    "What is the value of cos 0°?",
                    "What is the area of a circle with radius 7 cm? (Use π = 22/7)",
                    "What is the area of a rectangle with length 5 cm and width 4 cm?"
            };

            options = new String[][]{
                    {"2", "3", "4", "5"},
                    {"3", "4", "5", "6"},
                    {"x", "2x", "x²", "2"},
                    {"3x", "6x", "6", "9x"},
                    {"0", "1", "-1", "1/2"},
                    {"0", "1", "-1", "1/2"},
                    {"44 cm²", "154 cm²", "49 cm²", "22 cm²"},
                    {"9 cm²", "15 cm²", "20 cm²", "25 cm²"}
            };

            correctAnswers = new int[]{
                    1, 2, 1, 1, 1, 1, 1, 2
            };
        }
    }

    private void createAdaptiveQuestionOrder() {

        if (!weakestTopic.isEmpty()) {
            return;
        }

        int[] newOrder = new int[questions.length];
        int position = 0;

        // Put weak-topic questions first
        for (int i = 0; i < topics.length; i++) {

            if (topics[i].equals(weakestTopic)) {
                newOrder[position] = i;
                position++;
            }
        }

        // Add remaining questions
        for (int i = 0; i < topics.length; i++) {

            if (!topics[i].equals(weakestTopic)) {
                newOrder[position] = i;
                position++;
            }
        }

        questionOrder = newOrder;
    }

    private void showQuestion() {

        int questionIndex = questionOrder[currentQuestion];

        questionNumber.setText(
                "Question " + (currentQuestion + 1) +
                        " / " + questions.length
        );
        quizProgress.setProgress(currentQuestion + 1);

        questionText.setText(questions[questionIndex]);
        topicText.setText(topics[questionIndex]);

        option1.setText(options[questionIndex][0]);
        option2.setText(options[questionIndex][1]);
        option3.setText(options[questionIndex][2]);
        option4.setText(options[questionIndex][3]);

        optionsGroup.clearCheck();
    }

    private void checkAnswer() {

        int selectedId = optionsGroup.getCheckedRadioButtonId();

        if (selectedId == -1) {

            Toast.makeText(
                    this,
                    "Please select an answer",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        int selectedAnswer;

        if (selectedId == R.id.option1) {
            selectedAnswer = 0;
        } else if (selectedId == R.id.option2) {
            selectedAnswer = 1;
        } else if (selectedId == R.id.option3) {
            selectedAnswer = 2;
        } else {
            selectedAnswer = 3;
        }

        int questionIndex =
                questionOrder[currentQuestion];

        int topicIndex =
                getTopicIndex(topics[questionIndex]);

        topicTotal[topicIndex]++;

        boolean isCorrect =
                selectedAnswer == correctAnswers[questionIndex];

        if (isCorrect) {
            score++;
            topicCorrect[topicIndex]++;
        }

        showAnswerFeedback(
                isCorrect,
                questionIndex
        );
    }

    private int getTopicIndex(String topic) {

        if (topic.equals("Algebra")) {
            return 0;
        } else if (topic.equals("Calculus")) {
            return 1;
        } else if (topic.equals("Trigonometry")) {
            return 2;
        } else {
            return 3;
        }
    }

    private void saveLearningMemory() {

        SharedPreferences preferences =
                getSharedPreferences(
                        "GapFinderMemory",
                        MODE_PRIVATE
                );

        int quizCount =
                preferences.getInt("quizCount", 0);

        int bestScore =
                preferences.getInt("bestScore", 0);

        quizCount++;

        if (score > bestScore) {
            bestScore = score;
        }

        SharedPreferences.Editor editor =
                preferences.edit();

        editor.putInt("lastScore", score);
        editor.putString("weakestTopic", weakestTopic);
        editor.putString("lastSubject", selectedSubject);

        editor.putInt("quizCount", quizCount);
        editor.putInt("bestScore", bestScore);

        editor.apply();
    }
    private String getWeakestTopic() {

        int lowestPercentage = 101;
        int fewestAttempts = Integer.MAX_VALUE;

        String weakestTopic = "None";

        String[] topicNames = {
                "Algebra",
                "Calculus",
                "Trigonometry",
                "Geometry"
        };

        for (int i = 0; i < topicCorrect.length; i++) {

            if (topicTotal[i] == 0) {
                continue;
            }

            int percentage =
                    (topicCorrect[i] * 100) /
                            topicTotal[i];

            if (percentage < lowestPercentage) {

                lowestPercentage = percentage;
                fewestAttempts = topicTotal[i];
                weakestTopic = topicNames[i];

            } else if (
                    percentage == lowestPercentage &&
                            topicTotal[i] < fewestAttempts
            ) {

                fewestAttempts = topicTotal[i];
                weakestTopic = topicNames[i];
            }
        }

        return weakestTopic;
    }

    private void showAnswerFeedback(
            boolean isCorrect,
            int questionIndex
    ) {

        String title;
        String message;

        if (isCorrect) {

            title = "Correct!";

            message =
                    "Great job!\n\n" +
                            "You selected the correct answer.";

        } else {

            title = "Let's Learn";

            int correctAnswer =
                    correctAnswers[questionIndex];

            String correctText =
                    options[questionIndex][correctAnswer];

            message =
                    "Your answer was incorrect.\n\n" +
                            "Correct answer:\n" +
                            correctText + "\n\n" +
                            getExplanation(questionIndex);
        }

        new android.app.AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(
                        "Continue",
                        (dialog, which) -> moveToNextQuestion()
                )
                .setCancelable(false)
                .show();
    }

    private void moveToNextQuestion() {

        currentQuestion++;

        if (currentQuestion < questions.length) {

            showQuestion();

        } else {

            saveLearningMemory();

            Intent intent =
                    new Intent(
                            QuizActivity.this,
                            ResultActivity.class
                    );

            intent.putExtra(
                    "subject",
                    selectedSubject
            );

            intent.putExtra(
                    "score",
                    score
            );

            intent.putExtra(
                    "algebraCorrect",
                    topicCorrect[0]
            );

            intent.putExtra(
                    "algebraTotal",
                    topicTotal[0]
            );

            intent.putExtra(
                    "calculusCorrect",
                    topicCorrect[1]
            );

            intent.putExtra(
                    "calculusTotal",
                    topicTotal[1]
            );

            intent.putExtra(
                    "trigonometryCorrect",
                    topicCorrect[2]
            );

            intent.putExtra(
                    "trigonometryTotal",
                    topicTotal[2]
            );

            intent.putExtra(
                    "geometryCorrect",
                    topicCorrect[3]
            );

            intent.putExtra(
                    "geometryTotal",
                    topicTotal[3]
            );

            startActivity(intent);

            finish();
        }
    }

    private String getExplanation(int questionIndex) {

        if (selectedSubject.equals("Mathematics")) {

            switch (questionIndex) {

                case 0:
                    return "Solve 2x + 4 = 10. " +
                            "Subtract 4 and divide by 2, giving x = 3.";

                case 1:
                    return "Solve x + 7 = 12. " +
                            "Subtract 7 from both sides, giving x = 5.";

                case 2:
                    return "The derivative of x² is 2x.";

                case 3:
                    return "Using the power rule, " +
                            "the derivative of 3x² is 6x.";

                case 4:
                    return "sin 90° equals 1.";

                case 5:
                    return "cos 0° equals 1.";

                case 6:
                    return "Area = πr². " +
                            "Using r = 7 and π = 22/7, " +
                            "the area is 154 cm².";

                case 7:
                    return "Area = length × width. " +
                            "5 × 4 = 20 cm².";
            }

        } else if (selectedSubject.equals("Physics")) {

            switch (questionIndex) {

                case 0:
                    return "Velocity is measured in meters per second (m/s).";

                case 1:
                    return "Average speed = distance ÷ time. " +
                            "60 ÷ 2 = 30 km/h.";

                case 2:
                    return "The SI unit of force is the Newton (N).";

                case 3:
                    return "Gravity is the force that attracts objects toward Earth.";

                case 4:
                    return "The SI unit of energy is the Joule (J).";

                case 5:
                    return "A stretched spring stores elastic potential energy.";

                case 6:
                    return "Electric current is measured in Amperes (A).";

                case 7:
                    return "An ammeter is used to measure electric current.";
            }

        } else if (selectedSubject.equals("Chemistry")) {

            switch (questionIndex) {

                case 0:
                    return "An atom is the smallest unit of an element.";

                case 1:
                    return "Electrons carry a negative electric charge.";

                case 2:
                    return "A compound is formed when substances " +
                            "chemically combine.";

                case 3:
                    return "An exothermic reaction releases heat.";

                case 4:
                    return "A neutral solution has a pH of 7.";

                case 5:
                    return "Lemon juice contains acids, " +
                            "so it is acidic.";

                case 6:
                    return "The chemical symbol for oxygen is O.";

                case 7:
                    return "The modern periodic table contains 7 periods.";
            }
        }

        return "Review this concept and try a similar question again.";
    }
}