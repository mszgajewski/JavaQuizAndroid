package com.mszgajewski.javaquizandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private final List<QuestionsList> questionsLists = new ArrayList<>();

    private TextView quizTimer;
    private RelativeLayout option1Layout, option2Layout, option3Layout, option4Layout;
    private TextView option1TextView, option2TextView, option3TextView, option4TextView;
    private ImageView option1Icon, option2Icon, option3Icon, option4Icon;

    private TextView totalQuestionTextView;
    private TextView currentQuestion;

    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://quizappadds-default-rtdb.europe-west1.firebasedatabase.app/");

    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        quizTimer = findViewById(R.id.quizTimer);

        option1Layout = findViewById(R.id.option1Layout);
        option2Layout = findViewById(R.id.option2Layout);
        option3Layout = findViewById(R.id.option3Layout);
        option4Layout = findViewById(R.id.option4Layout);

        option1TextView = findViewById(R.id.option1TextView);
        option2TextView = findViewById(R.id.option2TextView);
        option3TextView = findViewById(R.id.option3TextView);
        option4TextView = findViewById(R.id.option4TextView);

        option1Icon = findViewById(R.id.option1Icon);
        option2Icon = findViewById(R.id.option2Icon);
        option3Icon = findViewById(R.id.option3Icon);
        option4Icon = findViewById(R.id.option4Icon);

        totalQuestionTextView = findViewById(R.id.totalQuestionsTextView);
        currentQuestion = findViewById(R.id.currentQuestionTextView);

        final AppCompatButton nextBtn = findViewById(R.id.nextQuestionBtn);

        InstructionsDialog instructionsDialog = new InstructionsDialog(MainActivity.this);
        instructionsDialog.setCancelable(false);
        instructionsDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        instructionsDialog.show();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final int getQuizTime = Integer.parseInt(snapshot.child("time").getValue(String.class));

                for (DataSnapshot questions : snapshot.child("questions").getChildren()){

                    String getQuestion = questions.child("question").getValue(String.class);
                    String getOption1 = questions.child("option1").getValue(String.class);
                    String getOption2 = questions.child("option2").getValue(String.class);
                    String getOption3 = questions.child("option3").getValue(String.class);
                    String getOption4 = questions.child("option4").getValue(String.class);
                     int getAnswer = Integer.parseInt(questions.child("answer").getValue(String.class));

                    QuestionsList questionsList = new QuestionsList(getQuestion, getOption1, getOption2, getOption3, getOption4, getAnswer);
                    questionsLists.add(questionsList);
                }

                totalQuestionTextView.setText("/" + questionsLists.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Bład pobierania danych", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startQuizTimer(int maxTimeInSeconds) {
        countDownTimer = new CountDownTimer(maxTimeInSeconds * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long getHour = TimeUnit.MILLISECONDS.toHours(millisUntilFinished);
                long getMinute  = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);
                long getSeconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished);

                String generateTime = String.format(Locale.getDefault(),"%02d:%02d:%02d", getHour,
                        getMinute - TimeUnit.HOURS.toMinutes(getHour),
                        getSeconds - TimeUnit.MINUTES.toSeconds(getMinute));

                quizTimer.setText(generateTime);
            }

            @Override
            public void onFinish() {

            }
        };
    }
}